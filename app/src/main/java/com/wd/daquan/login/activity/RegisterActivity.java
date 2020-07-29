package com.wd.daquan.login.activity;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.da.library.constant.IConstant;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.common.utils.SpannableStringUtils;
import com.wd.daquan.imui.dialog.CaptchaImgDialog;
import com.wd.daquan.login.helper.LoginHelper;
import com.wd.daquan.login.helper.WXLoginHelper;
import com.wd.daquan.login.listener.WXLoginListener;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.LoginBean;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.util.PhoneUtils;

import java.util.HashMap;
import java.util.Map;

import static com.da.library.constant.IConstant.Login.REGISTER_TYPE;

/**
 * @Author: 方志
 * @Time: 2018/9/11 16:42
 * @Description: 注册账号
 */
public class RegisterActivity extends BaseLoginActivity implements WXLoginListener, CaptchaImgDialog.Operator {

    private TextView mUserAgreementTv;
    private CheckBox userPrivacyCb;
    private TextView protocolTv;//服务协议
    private View registerLogin;//去登陆
    private View thirdIcon;//第三方登陆
    private CaptchaImgDialog captchaImgDialog;
    private Map<String, String> mWxMap = new HashMap<>();
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void initView() {
        mPhoneNumberEt = findViewById(R.id.register_phone_number_et);
        mClearPhoneIv = findViewById(R.id.cleat_iv);
        mGetCodeTv = findViewById(R.id.register_get_code_tv);
        mPasswordEt = findViewById(R.id.register_code_et);
        mConfirmBtn = findViewById(R.id.register_btn);
        mUserAgreementTv = findViewById(R.id.register_user_agreement_tv);
        userPrivacyCb = findViewById(R.id.user_privacy_cb);
        protocolTv = findViewById(R.id.protocol_tv);
        registerLogin = findViewById(R.id.register_login);
        thirdIcon = findViewById(R.id.third_icon);
        initProtocolContent();
        initDialog();
    }
    @Override

    protected void initData() {
        super.initData();
        initTitle();
    }
    private void initDialog(){
        captchaImgDialog = new CaptchaImgDialog();
    }
    private void initTitle() {

//        mTitleLayout.setTitle(getString(R.string.register));
//        mTitleLayout.setRightTxt(R.string.login);
//        mTitleLayout.getRightTv().setVisibility(View.VISIBLE);
//        mTitleLayout.getRightTv().setTextColor(getResources().getColor(R.color.color_4768f3));
    }

    @Override
    protected void initListener() {
        super.initListener();
        toolbarBack();
        mGetCodeTv.setOnClickListener(this);
        mUserAgreementTv.setOnClickListener(this);
        registerLogin.setOnClickListener(this);
        thirdIcon.setOnClickListener(this);
    }

    /**
     * 初始化协议内容
     */
    private void initProtocolContent(){
        String protocolContent = getResources().getString(R.string.protocol_content);
        String userServiceContent = "《用户服务协议》";
        String userPrivacyContent = "《用户隐私协议》";
        int userServiceStartIndex = protocolContent.indexOf(userServiceContent);
        int userServiceEndIndex = userServiceStartIndex + userServiceContent.length();
        int userPrivacyStartIndex = protocolContent.indexOf(userPrivacyContent);
        int userPrivacyEndIndex = userPrivacyStartIndex + userPrivacyContent.length();
        SpannableStringBuilder sb =  SpannableStringUtils.addTextColor(protocolContent,userServiceStartIndex,userServiceEndIndex,userPrivacyStartIndex,userPrivacyEndIndex,R.color.color_5680FA);
        sb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                NavUtils.gotoWebviewActivity(widget.getContext(), DqUrl.url_register_agreement, getString(R.string.serviceUser));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                //去掉可点击文字的下划线
                ds.setUnderlineText(false);
            }
        },userServiceStartIndex,userServiceEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                NavUtils.gotoWebviewActivity(widget.getContext(), DqUrl.url_privacy_agreement, getString(R.string.privacyUser));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                //去掉可点击文字的下划线
                ds.setUnderlineText(false);
            }
        },userPrivacyStartIndex,userPrivacyEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        protocolTv.setText(sb);
        // 设置此方法后，点击事件才能生效
        protocolTv.setMovementMethod(LinkMovementMethod.getInstance());
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
//            case R.id.register_btn:
//                //跳转登录
//                startActivity(new Intent(this, LoginPasswordActivity.class));
//                break;
            case R.id.register_get_code_tv:
                String phoneNumber = mPhoneNumberEt.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)){
                    DqToast.showShort("请输入手机号!");
                    return;
                }
                phoneNumber = phoneNumber.replace(" ","");
                DqLog.e("YM,手机号为:"+phoneNumber);
                if (!PhoneUtils.isMobileNO(phoneNumber)){
                    DqToast.showShort("请输入符合规则的手机号!");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(CaptchaImgDialog.PHONE_NUMBER,mPhoneNumberEt.getText().toString());
                captchaImgDialog.setArguments(bundle);
                captchaImgDialog.show(getSupportFragmentManager(),"");
                break;
            case R.id.register_btn:
                //QcToastUtil.showToast(this, "注册");
                if (!isAgreeProtocol()){
                    DqToast.showShort("必须同意用户协议后才能注册该应用！");
                    return;
                }
                register();
                break;
            case R.id.register_user_agreement_tv:
                NavUtils.gotoWebviewActivity(this, DqUrl.url_register_agreement, getString(R.string.user_service_agreement));
                break;
            case R.id.register_login:
                NavUtils.gotoLoginPasswordActivity(v.getContext());
                break;
            case R.id.third_icon:
                if (!isAgreeProtocol()){
                    DqToast.showShort("必须同意用户协议后才能登录该应用！");
                    return;
                }
                WXLoginHelper.umWxLogin(this, this);
                break;
//            case R.id.user_service_tv:
//                NavUtils.gotoWebviewActivity(this, DqUrl.url_register_agreement, getString(R.string.serviceUser));
//                break;
//            case R.id.user_privacy_tv:
//                Log.e("YM","隐私协议:"+DqUrl.url_privacy_agreement);
//                NavUtils.gotoWebviewActivity(this, DqUrl.url_privacy_agreement, getString(R.string.privacyUser));
//                break;
        }
    }

    private void register() {

        String phoneNumber = mPhoneNumberEt.getText().toString().replace(" ", "");
        String code = mPasswordEt.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNumber)){
            DqToast.showShort(getString(R.string.mobile_phone_number_cannot_be_empty));
            return;
        }
        if (TextUtils.isEmpty(code)){
            DqToast.showShort(getString(R.string.verification_code_cannot_be_empty));
            return;
        }

        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(IConstant.Login.PHONE, phoneNumber);
        hashMap.put(IConstant.Login.MSG, code);
        mPresenter.register(DqUrl.url_reg, hashMap);
    }


    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if(DqUrl.url_reg.equals(url)) {
            DqToast.showShort(DqApp.getStringById(R.string.register_success));
            LoginBean loginBean = (LoginBean) entity.data;
            loadRegisterData(loginBean);
        }else if(DqUrl.url_oauth_getUserInfo.equals(url)) {
            if(null == entity.data) {
                NavUtils.gotoBindPhoneActivity(this, mWxMap);
            }else {
                LoginBean loginBean = (LoginBean) entity.data;
                LoginHelper.saveCurrentUserInfo(loginBean, loginBean.password);
//                ViewModelProviders.of(this).get(ApplicationViewModel.class).initRoomDataBase(loginBean.uid);
                if(TextUtils.isEmpty(mSdkLogin)) {
                    LoginHelper.login(this, loginBean.uid, loginBean.imToken);
                }else{
                    finish();
                }
            }
        }
    }

    private void loadRegisterData(LoginBean loginBean) {
        LoginHelper.saveCurrentUserInfo(loginBean, "");
        NavUtils.gotoSetPassword(RegisterActivity.this);
        finish();
    }
    /* 是否同意协议 */
    private boolean isAgreeProtocol(){
        return userPrivacyCb.isChecked();
    }

    @Override
    public void loginWX(Map<String, String> map) {
        String openId = map.get(IConstant.WX.OPENID);
        String accessToken = map.get(IConstant.WX.ACCESSTOKEN);
        mWxMap.clear();
        mWxMap.putAll(map);
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(IConstant.WX.ACCESS_TOKEN, accessToken);
        hashMap.put(IConstant.WX.OPENID, openId);
        //Log.e("fz", "hashMap : " + hashMap.toString());
        mPresenter.loginPassword(DqUrl.url_oauth_getUserInfo, hashMap);
    }

    @Override
    public void cancel() {

    }

    @Override
    public void sure() {
        //QcToastUtil.showToast(this, "获取验证码");
//        getVerificationCode(mPhoneNumberEt, REGISTER_TYPE,value);
        startCountDownTimer();
    }
}
