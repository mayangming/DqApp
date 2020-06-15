package com.wd.daquan.mine.alipay;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.WXLoginEntity;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.common.utils.DialogUtils;
import com.da.library.listener.DialogListener;
import com.wd.daquan.login.helper.WXLoginHelper;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.wd.daquan.mine.listener.WXLoginListener;
import com.da.library.widget.CommTitle;

import java.util.LinkedHashMap;
import java.util.Map;

public class AccountBlindActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener, WXLoginListener, com.wd.daquan.login.listener.WXLoginListener {

    private String phone;
    private String uid;
    private String openId;
    private TextView txt_phoneNumber;
    private TextView txt_accountNickname;
    private TextView txt_bindType;
    private ImageView img_wx;
    private RelativeLayout layout_wx;
    private CommTitle mCommTitle;
    private WXLoginEntity mWxLoginEntity;

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.account_bind_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        mCommTitle = findViewById(R.id.accountBindActivityCommtitle);
        txt_phoneNumber = findViewById(R.id.accountBindActivityTxtPhoneNum);
        txt_accountNickname = findViewById(R.id.accountBindActivityTxtName);
        layout_wx = findViewById(R.id.accountBindActivityLayoutWX);
        txt_bindType = findViewById(R.id.accountBindActivityTxtType);
        img_wx=findViewById(R.id.accountBindActivityImgWX);
        mCommTitle.setTitle(getString(R.string.setting_account_bing));
    }

    @Override
    public void initListener() {
        layout_wx.setOnClickListener(this);
        mCommTitle.getLeftIv().setOnClickListener(this);
    }

    @Override
    public void initData() {
        QCSharedPrefManager mSharedPrefManager = QCSharedPrefManager.getInstance();
        EBSharedPrefUser mEBSharedPrefUser = mSharedPrefManager.getKDPreferenceUserInfo();
        uid = mEBSharedPrefUser.getString(EBSharedPrefUser.uid, "");
        phone=mEBSharedPrefUser.getString(EBSharedPrefUser.phone,"");
        txt_phoneNumber.setText(phone);

        if(mPresenter != null){
            LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("uid", uid);
            mPresenter.getBindWeixinStatus(DqUrl.url_oauth_bindWeixinStatus, linkedHashMap);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comm_left_iv:
                finish();
                break;
            case R.id.accountBindActivityLayoutWX://微信授权
                if (null ==mWxLoginEntity){
                    WXLoginHelper.umWxLogin(this, this);
                    return;
                }
                switch (mWxLoginEntity.status) {
                    case 1:
                        synch();
                        break;
                    case 0:
                        WXLoginHelper.umWxLogin(this, this);
                        break;
                }
                break;
        }
    }
    private void synch(){
        DialogUtils.showNoBindWXDialog(this, "", getString(R.string.setting_relieve_blind), new DialogListener() {
            @Override
            public void onCancel() {}

            @Override
            public void onOk() {
                if(mPresenter != null){
                    LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
                    linkedHashMap.put("openid", mWxLoginEntity.openid);
                    linkedHashMap.put("uid", uid);
                    linkedHashMap.put("phone", phone);
                    mPresenter.cancelBindWeixin(DqUrl.url_oauth_cancelBindWeixin, linkedHashMap);
                }
            }
        });
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        DqUtils.bequit(entity, this);
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (DqUrl.url_oauth_bindWeixinStatus.equals(url)) {
            if (0 == code) {
                mWxLoginEntity = (WXLoginEntity) entity.data;
                status(mWxLoginEntity);
            }

        } else if (DqUrl.url_oauth_cancelBindWeixin.equals(url)) {
            if (0 == code) {
                DqToast.showShort(entity.content);
                img_wx.setBackgroundResource(R.mipmap.login_wechat);
                mWxLoginEntity = null;
                txt_accountNickname.setText("");
                txt_bindType.setText(getString(R.string.setting_no_blind));
            }

        } else if (DqUrl.url_oauth_afreshBindWeixin.equals(url)) {
            if (0 == code) {
                DqToast.showShort(entity.content);
                mWxLoginEntity = (WXLoginEntity) entity.data;
                status(mWxLoginEntity);
            } else if (113507 == code) {
                DqToast.showShort(entity.content);
            }

        }
    }
    //状态
    private void status(WXLoginEntity mWxLoginEntity){
        switch (mWxLoginEntity.status) {
            case 1:
                txt_accountNickname.setText(mWxLoginEntity.wx_nickname);
                phone=mWxLoginEntity.phone;
                txt_phoneNumber.setText(phone);
                txt_bindType.setText(getString(R.string.setting_blind));
                break;
            case 0:
                txt_bindType.setText(getString(R.string.setting_no_blind));
                break;
        }
    }

    @Override
    public void loginWX(Map<String, String> map) {
        openId = map.get("openid");
        String accessToken = map.get("accessToken");
        if(mPresenter != null){
            LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("access_token", accessToken);
            linkedHashMap.put("openid", openId);
            linkedHashMap.put("uid", uid);
            linkedHashMap.put("phone", phone);
            mPresenter.bindWXAgain(DqUrl.url_oauth_afreshBindWeixin, linkedHashMap);
        }
    }
}
