package com.wd.daquan.mine.activity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.da.library.listener.DialogListener;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.login.helper.WXLoginHelper;
import com.wd.daquan.login.listener.WXLoginListener;
import com.wd.daquan.mine.presenter.WalletCloudPresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.WXLoginEntity;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 绑定微信页面
 */
public class BindWxActivity extends DqBaseActivity<WalletCloudPresenter, DataBean> implements WXLoginListener {

    private String phone;
    private String uid;
    private String openId;
    private WXLoginEntity mWxLoginEntity;
    private TextView wxBindTip;
    private View bindBtn;//绑定按钮
    private View unBindBtn;//接触绑定按钮
    private ImageView weChatIconIv;//微信用户头像
    @Override
    protected WalletCloudPresenter createPresenter() {
        return new WalletCloudPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_bind_wx);
    }

    @Override
    protected void initView() {
        mTitleDqLayout = findViewById(R.id.toolbar);
        wxBindTip = findViewById(R.id.wx_bind_tip);
        bindBtn = findViewById(R.id.wx_bind_status);
        unBindBtn = findViewById(R.id.wx_unbind_status);
        weChatIconIv = findViewById(R.id.we_chat_icon_iv);
        bindBtn.setOnClickListener(this);
        unBindBtn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        QCSharedPrefManager mSharedPrefManager = QCSharedPrefManager.getInstance();
        EBSharedPrefUser mEBSharedPrefUser = mSharedPrefManager.getKDPreferenceUserInfo();
        uid = mEBSharedPrefUser.getString(EBSharedPrefUser.uid, "");
        phone=mEBSharedPrefUser.getString(EBSharedPrefUser.phone,"");

        if(mPresenter != null){
            LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("uid", uid);
            mPresenter.getBindWeixinStatus(DqUrl.url_oauth_bindWeixinStatus, linkedHashMap);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.wx_bind_status:
                WXLoginHelper.umWxLogin(this, this);
                break;
            case R.id.wx_unbind_status:
                synch();
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
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (DqUrl.url_oauth_bindWeixinStatus.equals(url)) {
            mWxLoginEntity = (WXLoginEntity) entity.data;
            ModuleMgr.getCenterMgr().putWeChatIconUrl(mWxLoginEntity.wx_head_icon);
            ModuleMgr.getCenterMgr().putWeChatName(mWxLoginEntity.wx_nickname);
            status(mWxLoginEntity.status);
        } else if (DqUrl.url_oauth_cancelBindWeixin.equals(url)) {
            if (0 == code) {
                DqToast.showShort(entity.content);
                status(0);//移除绑定
                mWxLoginEntity = null;
            }

        }else if (DqUrl.url_oauth_afreshBindWeixin.equals(url)) {
            if (0 == code) {
                DqToast.showShort(entity.content);
                mWxLoginEntity = (WXLoginEntity) entity.data;
                status(mWxLoginEntity.status);
            } else if (113507 == code) {
                DqToast.showShort(entity.content);
            }

        }
    }
    //状态
    private void status(int status){
        Log.e("YM","发送状态:");
        MsgMgr.getInstance().sendMsg("weChatBindStatus",status);
        switch (status) {
            case 1://已绑定
                phone=mWxLoginEntity.phone;
                wxBindTip.setText(ModuleMgr.getCenterMgr().getWeChatName());
                bindBtn.setVisibility(View.GONE);
                unBindBtn.setVisibility(View.VISIBLE);
                weChatIconIv.setImageResource(R.mipmap.wechat_big_logo);
                Log.e("YM","微信头像:"+ModuleMgr.getCenterMgr().getWeChatIconUrl());
                GlideUtils.loadHeader(DqApp.sContext,ModuleMgr.getCenterMgr().getWeChatIconUrl(), weChatIconIv);
                break;
            case 0://未绑定
                wxBindTip.setText("绑定微信用于斗圈零钱提现");
                bindBtn.setVisibility(View.VISIBLE);
                unBindBtn.setVisibility(View.GONE);
                weChatIconIv.setImageResource(R.mipmap.wechat_big_logo);
                break;
        }
    }
    @Override
    public void loginWX(Map<String, String> map) {
        openId = map.get("openid");
        String accessToken = map.get("accessToken");
        String headIcon = map.get("iconurl");
        String weChatName = map.get("name");
//        for (Map.Entry<String, String> entry : map.entrySet()) {
////            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//            Log.e("YM","获取微信用户资料的key:"+entry.getKey()+"-->value:"+entry.getValue());
//        }
        if(mPresenter != null){
            LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("access_token", accessToken);
            linkedHashMap.put("openid", openId);
            linkedHashMap.put("uid", uid);
            linkedHashMap.put("phone", phone);
            linkedHashMap.put("wx_nickname", weChatName);
            linkedHashMap.put("wx_head_icon", headIcon);
            mPresenter.bindWXAgain(DqUrl.url_oauth_afreshBindWeixin, linkedHashMap);
        }
        ModuleMgr.getCenterMgr().putWeChatIconUrl(headIcon);
        ModuleMgr.getCenterMgr().putWeChatName(weChatName);
    }
}
