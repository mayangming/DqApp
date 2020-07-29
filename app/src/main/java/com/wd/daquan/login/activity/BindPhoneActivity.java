package com.wd.daquan.login.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.imui.dialog.CaptchaImgDialog;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.WxBindBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.da.library.constant.IConstant;
import com.wd.daquan.model.bean.LoginBean;
import com.wd.daquan.login.helper.LoginHelper;
import com.da.library.tools.MD5;

import java.util.HashMap;
import java.util.Map;

import static com.da.library.constant.IConstant.Login.LOGIN_TYPE;

/**
 * @Author: 方志
 * @Time: 2018/9/12 10:22
 * @Description: 微信登录绑定手机号
 */
public class BindPhoneActivity extends BaseLoginActivity implements CaptchaImgDialog.Operator {

    private EditText mCodeEt;
    private Button mBindPhoneBtn;
    private String mOpenId;
    private String mAccesToken;
    //    private CommDialog mSynsWxDataDialog;
    private LoginBean loginBean;
    private CaptchaImgDialog captchaImgDialog;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_bind_phone);
    }

    @Override
    protected void initView() {
        mTitleLayout = findViewById(R.id.bind_phone_comm_title_layout);
        mPhoneNumberEt = findViewById(R.id.bind_phone_number_et);
        mGetCodeTv = findViewById(R.id.bind_phone_get_code_tv);
        mCodeEt = findViewById(R.id.bind_phone_code_et);
        mBindPhoneBtn = findViewById(R.id.bind_phone_btn);
        mBindPhoneBtn.setEnabled(true);
    }

    @Override
    protected void initData() {
        super.initData();
        getIntentData();
        initTitle();
        initDialog();
    }

    private void initDialog() {
        captchaImgDialog = new CaptchaImgDialog();
    }

    private void getIntentData() {
        Map<String, String> mWxMap = (Map<String, String>) getIntent().getSerializableExtra(IConstant.WX.WXMAP);
//        mNickName = mWxMap.get("screen_name");
//        mGender = mWxMap.get("gender");
//        mHeadPic = mWxMap.get("profile_image_url");
        mOpenId = mWxMap.get(IConstant.WX.OPENID);
        mAccesToken = mWxMap.get(IConstant.WX.ACCESSTOKEN);
    }

    private void initTitle() {

        mTitleLayout.setTitle(getString(R.string.bind_phone_number));
        mTitleLayout.getRightTv().setVisibility(View.GONE);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mGetCodeTv.setOnClickListener(this);
        mBindPhoneBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.bind_phone_get_code_tv:
//                //获取验证码
//                getVerificationCode();
                Bundle bundle = new Bundle();
                bundle.putString(CaptchaImgDialog.PHONE_NUMBER, mPhoneNumberEt.getText().toString());
                captchaImgDialog.setArguments(bundle);
                captchaImgDialog.show(getSupportFragmentManager(), "");
                break;
            case R.id.bind_phone_btn:
                Log.e("YM", "点击事件");
                //绑定
                login();
                break;
        }
    }

    private void login() {

        String phoneNumber = mPhoneNumberEt.getText().toString().replace(" ", "");
        String code = mCodeEt.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNumber)) {
            DqToast.showShort(getString(R.string.mobile_phone_number_cannot_be_empty));
            return;
        }
        if (TextUtils.isEmpty(code)) {
            DqToast.showShort(getString(R.string.verification_code_cannot_be_empty));
            return;
        }

        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(IConstant.Login.PHONE, phoneNumber);
        hashMap.put(IConstant.Login.CODE, code);
        hashMap.put(IConstant.WX.OPENID, mOpenId);
        hashMap.put(IConstant.WX.ACCESS_TOKEN, mAccesToken);
        mPresenter.loginPassword(DqUrl.url_oauth_checkPhoneCode, hashMap);
    }

    private void getVerificationCode(String captchaImgValue) {
        String number = mPhoneNumberEt.getText().toString().replace(" ", "");
        if (DqUtils.validatePhoneNumber(number)) {
            String key = IConstant.Login.CHATDQ + number;
            String token_key = MD5.encrypt(key).toLowerCase();

            Map<String, String> hashMap = new HashMap<>();
            hashMap.put(IConstant.Login.PHONE, number);
            hashMap.put(IConstant.WX.OPENID, mOpenId);
            hashMap.put(IConstant.Login.TOKEN_KEY, token_key);
            hashMap.put(IConstant.Login.CAPTCHA, captchaImgValue);
            mPresenter.getWXVerificationCode(DqUrl.url_get_phone_msg, hashMap);
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (IConstant.OK.equals(entity.status)) {
            if (DqUrl.url_oauth_checkPhoneCode.equals(url)) {
                loginBean = (LoginBean) entity.data;
                if (null != loginBean) {
                    LoginHelper.saveCurrentUserInfo(loginBean, "");
//                    synchronousWxData(loginBean);
                    // 默认同步微信
                    Map<String, String> hashMap = new HashMap<>();
                    hashMap.put(IConstant.Login.UID, loginBean.uid);
                    hashMap.put(IConstant.WX.OPENID, mOpenId);
                    mPresenter.getUseWeixinInfo(DqUrl.url_oauth_useWeixinInfo, hashMap);
                }
            } else if (DqUrl.url_oauth_useWeixinInfo.equals(url)) {
                WxBindBean bindBean = (WxBindBean) entity.data;
                if (null != loginBean) {
                    LoginHelper.login(getActivity(), loginBean.uid, loginBean.imToken);
                }

                if (null != bindBean) {
                    mUserInfoSp.saveString(EBSharedPrefUser.nickname, bindBean.nickName);
                    mUserInfoSp.saveString(EBSharedPrefUser.headpic, bindBean.headpic);
                }
            }
        }
    }

    @Override
    public void cancel() {

    }

    @Override
    public void sure() {
        //获取验证码
//        getVerificationCode(value);
    }
//    private void synchronousWxData(final LoginBean loginBean) {
//
//        mSynsWxDataDialog = new CommDialog(this);
//        mSynsWxDataDialog.setTitleVisible(false);
//        mSynsWxDataDialog.setDescCenter();
//        mSynsWxDataDialog.setDesc(getString(R.string.whether_synchronous_wechat_data));
//        mSynsWxDataDialog.setOkTxt(getString(R.string.synchronous));
//        mSynsWxDataDialog.setOkTxtColor(getResources().getColor(R.color.color_4768f3));
//        mSynsWxDataDialog.show();
//
//        mSynsWxDataDialog.setDialogListener(new DialogListener() {
//            @Override
//            public void onCancel() {
//                LoginHelper.saveCurrentUserInfo(loginBean, "");
//                LoginHelper.login(getActivity(), loginBean.uid, loginBean.imToken);
//            }
//
//            @Override
//            public void onOk() {
//                Map<String, String> hashMap = new HashMap<>();
//                hashMap.put(IConstant.Login.UID, loginBean.uid);
//                mPresenter.getUseWeixinInfo(DqUrl.url_oauth_useWeixinInfo, hashMap);
//            }
//        });
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (null != mSynsWxDataDialog) {
//            mSynsWxDataDialog.dismiss();
//            mSynsWxDataDialog = null;
//        }
//    }

}
