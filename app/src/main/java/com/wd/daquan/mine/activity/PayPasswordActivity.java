package com.wd.daquan.mine.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.da.library.constant.IConstant;
import com.da.library.tools.MD5;
import com.da.library.utils.AESUtil;
import com.da.library.view.PwdInputView;
import com.da.library.widget.CountDownTimerUtils;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.imui.dialog.CaptchaImgDialog;
import com.wd.daquan.mine.presenter.WalletCloudPresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 该页面设置支付密码。如果之前设置过密码，再次设置会覆盖掉原先的密码
 */
public class PayPasswordActivity extends DqBaseActivity<WalletCloudPresenter, DataBean> implements CaptchaImgDialog.Operator{
    private View payPwdSet;
    private View phoneCodeRl;
    private PwdInputView pwdInputViewOnce;
    private PwdInputView pwdInputViewConfirm;
    private EditText phoneCodeEt;
    private TextView phoneCodeBtn;
    private CaptchaImgDialog captchaImgDialog;
    private CountDownTimerUtils mCountDownTimerUtils;
    @Override
    protected WalletCloudPresenter createPresenter() {
        return new WalletCloudPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_pay_pwd);
    }

    @Override
    protected void initView() {
        mTitleDqLayout = findViewById(R.id.toolbar);
        pwdInputViewOnce = findViewById(R.id.pay_pwd_once);
        pwdInputViewConfirm = findViewById(R.id.pay_pwd_confirm);
        phoneCodeRl = findViewById(R.id.bind_phone_code_rl);
        payPwdSet = findViewById(R.id.pay_pwd_set);
        phoneCodeEt = findViewById(R.id.bind_phone_code_et);
        phoneCodeBtn = findViewById(R.id.pay_phone_get_code_tv);
        payPwdSet.setOnClickListener(this);
        phoneCodeBtn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        initDialog();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.pay_pwd_set:
                setPwd();
                break;
            case R.id.pay_phone_get_code_tv:
                boolean verificationResult = verificationPwd();
                if (!verificationResult){
                    break;
                }
                String phoneNumber = ModuleMgr.getCenterMgr().getPhoneNumber();
                Bundle bundle = new Bundle();
                bundle.putString(CaptchaImgDialog.PHONE_NUMBER,phoneNumber);
                captchaImgDialog.setArguments(bundle);
                captchaImgDialog.show(getSupportFragmentManager(),"");
                break;
        }
    }

    private void initDialog(){
        captchaImgDialog = new CaptchaImgDialog();
    }
    /**
     * 获取验证码
     * @param captchaQrCode
     */
    private void getVerificationCode(String captchaQrCode){
        String phoneNumber = ModuleMgr.getCenterMgr().getPhoneNumber();
        String key = IConstant.Login.CHATDQ + phoneNumber;
        String token_key = MD5.encrypt(key).toLowerCase();

        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put(IConstant.Login.PHONE, phoneNumber);
        hashMap.put(IConstant.Login.TOKEN_KEY, token_key);
        hashMap.put(IConstant.Login.CAPTCHA, captchaQrCode);
        mPresenter.getPhoneCode(DqUrl.url_get_phone_msg,hashMap);
        //点击验证码时候就开始倒计时：不论是否接口请求成功，因为接口请求时间很长
        mCountDownTimerUtils = new CountDownTimerUtils(phoneCodeBtn, 60000, 1000, this);
        mCountDownTimerUtils.start();

    }

    /**
     * 验证密码
     */
    private boolean verificationPwd(){
        boolean verificationResult = true;
        String oncePwd = pwdInputViewOnce.getPwd();
        String confirmPwd = pwdInputViewConfirm.getPwd();
        if (TextUtils.isEmpty(oncePwd)){
            verificationResult = false;
            DqToast.showShort("密码不能为空~");
            return verificationResult;
        }
        if (!oncePwd.equals(confirmPwd)){
            verificationResult = false;
            DqToast.showShort("两次密码不一致~");
        }
        return verificationResult;
    }

    private void setPwd(){
        String oncePwd = pwdInputViewOnce.getPwd();
        String confirmPwd = pwdInputViewConfirm.getPwd();
        String phoneCode = phoneCodeEt.getText().toString();
        if (TextUtils.isEmpty(phoneCode)){
            DqToast.showShort("验证码不允许为空");
            return;
        }
        try{
            String payPwd = AESUtil.encrypt(confirmPwd);
            Map<String,String> params = new HashMap<>();
            params.put("pwd",payPwd);
            params.put("code",phoneCode);
            mPresenter.setUserTransactionPassword(DqUrl.url_user_transaction_password,params);
        }catch (Exception e){
            e.printStackTrace();
            DqToast.showShort("密码加密失败，请重新进行加密~");
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        Log.e("YM","请求结果onSuccess:"+url);
        if (DqUrl.url_user_transaction_password.equals(url)){
            if (entity.result == 0){
                DqToast.showShort("密码设置成功!");
                finish();
            }
        }else if (DqUrl.url_get_phone_msg.equals(url)){
            if (entity.result != 0){
                mCountDownTimerUtils.cancel();
                mCountDownTimerUtils.onFinish();
            }
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        if (DqUrl.url_user_transaction_password.equals(url)){
            DqToast.showShort(entity.content);
        }else if (DqUrl.url_get_phone_msg.equals(url)){
            DqToast.showShort(entity.content);
            mCountDownTimerUtils.cancel();
            mCountDownTimerUtils.onFinish();

        }
    }
    @Override
    public void cancel() {

    }

    @Override
    public void sure(String value) {
        //获取验证码
        getVerificationCode(value);
    }
}