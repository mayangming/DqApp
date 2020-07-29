package com.wd.daquan.login.activity;

import android.Manifest;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.da.library.constant.IConstant;
import com.netease.nim.uikit.support.permission.MPermission;
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
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;

import java.util.HashMap;
import java.util.Map;

import static com.da.library.constant.IConstant.Login.LOGIN_TYPE;


/**
 * @Author: 方志
 * @Time: 2018/9/11 15:36
 * @Description: 验证码登录
 */
public class LoginCodeActivity extends BaseLoginActivity implements WXLoginListener, CaptchaImgDialog.Operator {

    private View mLoginPasswordTv;
    private String mSdkLogin;
    private CheckBox userPrivacyCb;
    private TextView protocolTv;
    private View weChatLoginIv;
    private Map<String, String> mWxMap = new HashMap<>();
    private CaptchaImgDialog captchaImgDialog;
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_login_code);
    }

    @Override
    protected void initView() {
        mPhoneNumberEt = findViewById(R.id.login_code_phone_number_et);
        mGetCodeTv = findViewById(R.id.login_code_get_code_tv);
        mPasswordEt = findViewById(R.id.login_code_code_et);
        mClearPhoneIv = findViewById(R.id.cleat_iv);
        mConfirmBtn = findViewById(R.id.login_code_btn);
        mLoginPasswordTv = findViewById(R.id.login_password_tv);
        userPrivacyCb = findViewById(R.id.user_privacy_cb);
        protocolTv = findViewById(R.id.protocol_tv);
        weChatLoginIv = findViewById(R.id.wechat_login_iv);
        initProtocolContent();
        initDialog();
    }

    /**
     * 基本权限管理
     */
    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储卡写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE,//存储卡读取权限
            Manifest.permission.READ_PHONE_STATE,//手机状态
    };

    private void requestBasicPermission() {
        MPermission.with(this)
                .permissions(BASIC_PERMISSIONS)
                .request();
    }

    @Override
    protected void init() {
        super.init();
        requestBasicPermission();
        ModuleMgr.getCenterMgr().setInstall(true);
    }

    @Override
    protected void initData() {
        super.initData();
        String phoneNum = ModuleMgr.getCenterMgr().getPhoneNumber();
        if (!TextUtils.isEmpty(phoneNum)) {
            String target = format(phoneNum);
            mPhoneNumberEt.setText(target);
            mPhoneNumberEt.setSelection(target.length());
        }
    }

    private void initDialog(){
        captchaImgDialog = new CaptchaImgDialog();
    }

    @Override
    protected void initListener() {
        super.initListener();
        toolbarBack();
        toolbarRightTvOnClick(view -> NavUtils.gotoRegisterActivity(this));
        mGetCodeTv.setOnClickListener(this);
        mLoginPasswordTv.setOnClickListener(this);
        protocolTv.setOnClickListener(this);
        weChatLoginIv.setOnClickListener(this);
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
            case R.id.login_code_get_code_tv:
                Bundle bundle = new Bundle();
                bundle.putString(CaptchaImgDialog.PHONE_NUMBER,mPhoneNumberEt.getText().toString());
                captchaImgDialog.setArguments(bundle);
                captchaImgDialog.show(getSupportFragmentManager(),"");
                break;
            case R.id.login_code_btn:
                if (!isAgreeProtocol()){
                    DqToast.showShort("必须同意用户协议后才能登录该应用！");
                    return;
                }
                //登录
                login(mPhoneNumberEt, mPasswordEt, "2");
//                NavUtils.gotoMainActivity(this);
                break;
            case R.id.login_password_tv:
                //使用密码登录
                NavUtils.gotoLoginPasswordActivity(this);
                break;
            case R.id.wechat_login_iv:
                if (!isAgreeProtocol()){
                    DqToast.showShort("必须同意用户协议后才能登录该应用！");
                    return;
                }
                WXLoginHelper.umWxLogin(this, this);
                break;
//            case R.id.user_agreement_tv:
//                //QcToastUtil.showToast(this, "查看用户使用协议");
//                NavUtils.gotoWebviewActivity(this, DqUrl.url_register_agreement, getString(R.string.user_service_agreement));
//                break;
        }
    }

    /* 是否同意协议 */
    private boolean isAgreeProtocol(){
        return userPrivacyCb.isChecked();
    }
    /**
     * 微信登录
     */
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
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if(DqUrl.url_oauth_getUserInfo.equals(url)) {
            if(null == entity.data) {
                NavUtils.gotoBindPhoneActivity(this, mWxMap);
            }else {
                LoginBean loginBean = (LoginBean) entity.data;
                LoginHelper.saveCurrentUserInfo(loginBean, loginBean.password);
//                ViewModelProviders.of(this).get(ApplicationViewModel.class).initRoomDataBase(loginBean.uid);
                if(TextUtils.isEmpty(mSdkLogin)) {
                    LoginHelper.login(this, loginBean.uid, loginBean.imToken);
                }else{
                    Log.e("YM","-------------->LoginCodeActivity");
                    LoginHelper.gotoMain(this);
                    finish();
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
        startCountDownTimer();
    }
}
