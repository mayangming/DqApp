package com.wd.daquan.login.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Build;
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
import com.da.library.listener.DialogListener;
import com.da.library.view.DqToolbar;
import com.netease.nim.uikit.support.permission.MPermission;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.common.utils.SpannableStringUtils;
import com.wd.daquan.login.helper.CommDialogHelper;
import com.wd.daquan.login.helper.LoginHelper;
import com.wd.daquan.login.helper.WXLoginHelper;
import com.wd.daquan.login.listener.WXLoginListener;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.LoginBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 方志
 * @Time: 2018/9/7 13:46
 * @Description: 账号密码登录
 */
public class LoginPasswordActivity extends BaseLoginActivity implements WXLoginListener {

    private View mLoginCodeTv;
    private CheckBox userPrivacyCb;
    private TextView protocolTv;
    private TextView mForgetPasswordTv;
    private Map<String, String> mWxMap = new HashMap<>();

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_login_password);
    }

    @Override
    protected void init() {
        requestBasicPermission();
        ModuleMgr.getCenterMgr().setInstall(true);
    }

    @Override
    protected void initView() {
        DqToolbar toolbar = findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setTitlePadding(0);
        }
        toolbarBack();
        mPhoneNumberEt = findViewById(R.id.phone_et);
        mPasswordEt = findViewById(R.id.password_et);
        mConfirmBtn = findViewById(R.id.login_password_btn);
        mLoginCodeTv = findViewById(R.id.login_code_tv);
        mForgetPasswordTv = findViewById(R.id.forget_password_tv);
        mClearPhoneIv = findViewById(R.id.clear_phone_iv);
        mClearPwdIv = findViewById(R.id.clear_pwd_iv);
        userPrivacyCb = findViewById(R.id.user_privacy_cb);
        protocolTv = findViewById(R.id.protocol_tv);
        initProtocolContent();
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initListener() {
        super.initListener();
        toolbarRightTvOnClick(view -> NavUtils.gotoRegisterActivity(this));

        mLoginCodeTv.setOnClickListener(this);
        mForgetPasswordTv.setOnClickListener(this);
        findViewById(R.id.wechat_login_iv).setOnClickListener(this);
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
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
//            case R.id.tv_register:
//                //注册
//                NavUtils.gotoRegisterActivity(LoginPasswordActivity.this);
//                break;
            case R.id.login_password_btn:
                if (!isAgreeProtocol()){
                    DqToast.showShort("必须同意用户协议后才能登录该应用！");
                    return;
                }
                login(mPhoneNumberEt, mPasswordEt, "1");
                break;
            case R.id.login_code_tv:
                NavUtils.gotoLoginCodeActivity(this, mSdkLogin);
                finish();
                break;
            case R.id.forget_password_tv:
                NavUtils.gotoForgetLoginPasswordActivity(this);
                break;
            case R.id.wechat_login_iv:
//                DqToast.showShort(DqApp.getStringById(R.string.no_this_function));
                if (!isAgreeProtocol()){
                    DqToast.showShort("必须同意用户协议后才能登录该应用！");
                    return;
                }
                WXLoginHelper.umWxLogin(this, this);
                break;
        }
    }

    /**
     * 微信登录
     */
    @Override
    public void loginWX(Map<String, String> map) {
        String openId = map.get(IConstant.WX.OPENID);
        String accessToken = map.get(IConstant.WX.ACCESS_TOKEN);
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
                    Log.e("YM","-------------->LoginPasswordActivity");
                    LoginHelper.gotoMain(this);
                    finish();
                }
            }
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        switch (code) {
            case IConstant.Code.LOGIN_PASSWORD_ERROR_CODE:
                CommDialogHelper.getInstance().showPasswordErrorDialog(this, entity.content)
                        .setDialogListener(new DialogListener() {
                            @Override
                            public void onCancel() {
                                //login(mPhoneNumberEt, mPasswordEt, "1");
                                mPhoneNumberEt.setText("");
                                mPasswordEt.setText("");
                            }

                            @Override
                            public void onOk() {
                                NavUtils.gotoForgetLoginPasswordActivity(LoginPasswordActivity.this);
                            }
                        });
                break;
            case IConstant.Code.LOGIN_PASSWORD_ERROR_MAX_CODE:
                CommDialogHelper.getInstance().showPasswordErrorMaxDialog(this, entity.content)
                        .setDialogListener(new DialogListener() {
                            @Override
                            public void onCancel() {

                            }

                            @Override
                            public void onOk() {
                                NavUtils.gotoLoginCodeActivity(LoginPasswordActivity.this, mSdkLogin);
                            }
                        });
                break;
            default:
                DqUtils.bequit(entity, this);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommDialogHelper.getInstance().onDestroy();
    }
    /* 是否同意协议 */
    private boolean isAgreeProtocol(){
        return userPrivacyCb.isChecked();
    }
}
