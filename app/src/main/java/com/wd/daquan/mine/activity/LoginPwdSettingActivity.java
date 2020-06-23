package com.wd.daquan.mine.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.da.library.constant.IConstant;
import com.da.library.tools.MD5;
import com.da.library.widget.CountDownTimerUtils;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;

import java.util.LinkedHashMap;

public class LoginPwdSettingActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener{

    private TextView btn_getCode;
    private TextView btn_confirm;
    private EditText edit_code;
    private EditText edit_phone;
    private EditText edit_pwd;
    private EditText edit_pwdAgain;
    private String encryPwd;
    private CountDownTimerUtils mCountDownTimerUtils;
    @Override
    protected void setContentView() {
        setContentView(R.layout.login_pwd_setting_activity);
    }

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter();
    }

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        edit_pwd= findViewById(R.id.loginPwdSettingActivityEtPwd);
        edit_pwdAgain= findViewById(R.id.loginPwdSettingActivityEtPwdAgain);
        edit_code= findViewById(R.id.loginPwdSettingActivityEtCode);
        edit_phone= findViewById(R.id.loginPwdSettingActivityEtPhone);
        btn_getCode= findViewById(R.id.loginPwdSettingActivityBtnCode);
        btn_confirm= findViewById(R.id.loginPwdSettingActivityBtnConfirm);
    }

    @Override
    public void initListener() {
        toolbarBack();
        btn_confirm.setOnClickListener(this);
        btn_getCode.setOnClickListener(this);
    }

    @Override
    public void initData() {
        String phoneNumber = ModuleMgr.getCenterMgr().getPhoneNumber();
        edit_phone.setText(phoneNumber);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comm_left_iv:
                finish();
                break;
            case R.id.loginPwdSettingActivityBtnCode://获取验证码
                String phone=edit_phone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)){
                    DqToast.showShort(getString(R.string.phone_number_is_null));
                    return;
                }
                if (DqUtils.validatePhoneNumber(phone,this)) {
                    //获取验证码接口
                    String key = IConstant.Login.CHATDQ + phone;
                    String token_key = MD5.encrypt(key).toLowerCase();
                    LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
                    linkedHashMap.put("phone", phone);
                    linkedHashMap.put("type", KeyValue.ONE_STRING);
                    linkedHashMap.put("token_key", token_key);
                    //点击验证码时候就开始倒计时：不论是否接口请求成功，因为接口请求时间很长
                    mCountDownTimerUtils = new CountDownTimerUtils(btn_getCode, 60000, 1000, this);
                    mCountDownTimerUtils.start();
                    mPresenter.getPhoneCode(DqUrl.url_get_phone_msg, linkedHashMap);
                }
                break;
            case R.id.loginPwdSettingActivityBtnConfirm://确认
                String phones=edit_phone.getText().toString().trim();
                if (TextUtils.isEmpty(phones)){
                    DqToast.showShort(getString(R.string.phone_number_is_null));
                    return;
                }
                String etNewpwd=edit_pwd.getText().toString().trim();
                String etNewpwdAgain=edit_pwdAgain.getText().toString().trim();
                if (!TextUtils.isEmpty(etNewpwd)&!TextUtils.isEmpty(etNewpwdAgain)) {
                    if (etNewpwdAgain.length()>=6 & etNewpwd.length()>=6) {
                        if (etNewpwd.equals(etNewpwdAgain)) {
                            String code = edit_code.getText().toString();
                            //验证手机号，验证码
                            if (DqUtils.validatePhoneNumber(phones,this)) {
                                if (DqUtils.validate(etNewpwd)) {
                                    encryPwd = MD5.encrypt(IConstant.Password.PWD_MD5 + etNewpwd, true);
                                    LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
                                    linkedHashMap.put("phone", phones);
                                    linkedHashMap.put("msg", code);
                                    linkedHashMap.put("pwd", encryPwd);
                                    linkedHashMap.put("type", KeyValue.TWO_STRING);
                                    linkedHashMap.put("old_pwd", "");
                                    mPresenter.getSetPwd(DqUrl.url_set_pwd, linkedHashMap);
                                }
                            }
                        } else {
                            DqToast.showShort(getString(R.string.setting_login_pwd_pwd_inconformity));
                        }
                    }else{
                        DqToast.showShort(getString(R.string.setting_login_pwd_pwd_atleast));
                    }
                }else{
                    DqToast.showShort(getString(R.string.password_is_null));
                }
                break;
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        if (DqUrl.url_get_phone_msg.equals(url)){
            mCountDownTimerUtils.cancel();
            mCountDownTimerUtils.onFinish();
        }
        DqUtils.bequit(entity,this);
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (DqUrl.url_get_phone_msg.equals(url)) {//验证码
            if (0 != code) {
                DqToast.showShort(entity.content);
                mCountDownTimerUtils.cancel();
                mCountDownTimerUtils.onFinish();
            }
            DqToast.showShort(entity.content);
        }else  if (DqUrl.url_set_pwd.equals(url)) {//设置密码
            if (0 == code) {
                ModuleMgr.getCenterMgr().putPwd(encryPwd);
                DqToast.showShort(getString(R.string.setting_login_pwd_pwd_set_success));
                finish();
            } else {
                DqToast.showShort(entity.content);
            }
        }
    }

}
