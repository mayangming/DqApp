package com.wd.daquan.login.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.da.library.constant.IConstant;
import com.da.library.tools.MD5;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.imui.dialog.CaptchaImgDialog;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 方志
 * @Time: 2018/9/11 18:46
 * @Description: 忘记登录密码
 */
public class ForgetLoginPasswordActivity extends BaseLoginActivity implements CaptchaImgDialog.Operator{

    private EditText mCodeEt;
    private EditText mInputPasswordEt;
    private EditText mInputPasswordAgainEt;
    private Button mConfirmBtn;
    private String newPassword;
    private CaptchaImgDialog captchaImgDialog;
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_forget_login_password);
    }

    @Override
    protected void initView() {
        mPhoneNumberEt = findViewById(R.id.forget_password_phone_number_et);
        mGetCodeTv = findViewById(R.id.forget_password_get_code_tv);
        mCodeEt = findViewById(R.id.forget_password_code_et);
        mInputPasswordEt = findViewById(R.id.forget_password_input_password_et);
        mInputPasswordAgainEt = findViewById(R.id.forget_password_input_password_again_et);
        mConfirmBtn = findViewById(R.id.forget_password_confirm_btn);
        initDialog();

    }

    private void initDialog(){
        captchaImgDialog = new CaptchaImgDialog();
    }
    @Override
    protected void initListener() {
        super.initListener();
        toolbarBack();
        mGetCodeTv.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);

//        mScrollView.setNestedScrollingEnabled(false);
//        mScrollView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
//            if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
//                int bottomRL = mTranslationContainerRl.getBottom() - bottom;
//                if ((mTranslationContainerRl.getBottom() - bottom) > 0) {
//                    int translationY = DensityUtil.dip2px(getActivity(), 60);
//                    ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat
//                            (mTranslationContainerRl, "translationY", 0.0f, -translationY);
//                    mAnimatorTranslateY.setDuration(300);
//                    mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
//                    mAnimatorTranslateY.start();
//                }
//
//            } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
//                if ((mTranslationContainerRl.getBottom() - oldBottom) > 0) {
//                    ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mTranslationContainerRl,
//                            "translationY", mTranslationContainerRl.getTranslationY(), 0);
//                    mAnimatorTranslateY.setDuration(300);
//                    mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
//                    mAnimatorTranslateY.start();
//                }
//            }
//        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.forget_password_get_code_tv:
                Bundle bundle = new Bundle();
                bundle.putString(CaptchaImgDialog.PHONE_NUMBER,mPhoneNumberEt.getText().toString());
                captchaImgDialog.setArguments(bundle);
                captchaImgDialog.show(getSupportFragmentManager(),"");
                break;
            case R.id.forget_password_confirm_btn:
                setLoginPassword();
                break;
        }
    }

    private void setLoginPassword() {

        String password = mInputPasswordEt.getText().toString().trim();
        String passwordConfirm = mInputPasswordAgainEt.getText().toString().trim();

        if (password.equals(passwordConfirm)) {
            //验证手机号，验证码
            if (DqUtils.validate(password)) {
                newPassword = MD5.encrypt(IConstant.Password.PWD_MD5 + password, true);
                setLoginPassword(newPassword);
            }
        } else {
            DqToast.showShort("输入密码不一致");
        }
    }

    private void setLoginPassword(String newPassword) {
        String code = mCodeEt.getText().toString().trim();
        String phoneNumber = mPhoneNumberEt.getText().toString().trim().replace(" ", "");

        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(IConstant.Login.PHONE, phoneNumber);
        hashMap.put(IConstant.Login.MSG, code);
        hashMap.put(IConstant.Login.PWD, newPassword);
        hashMap.put(IConstant.Login.TYPE, "2");
        hashMap.put(IConstant.Login.OLD_PWD, "");

        mPresenter.setPassword(DqUrl.url_forget_pwd, hashMap);
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (DqUrl.url_forget_pwd.equals(url)) {
            ModuleMgr.getCenterMgr().putPwd(newPassword);
            DqToast.showShort(getString(R.string.setting_login_pwd_pwd_set_success));
            finish();
        }
    }

    @Override
    public void cancel() {

    }

    @Override
    public void sure(String value) {
        //获取验证码
        getVerificationCode(mPhoneNumberEt, "",value);
    }
}
