package com.wd.daquan.login.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.da.library.constant.IConstant;
import com.wd.daquan.login.helper.LoginHelper;
import com.da.library.tools.MD5;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 方志
 * @Time: 2018/9/11 17:21
 * @Description: 设置用户登录密码
 */
public class SetLoginPasswordActivity extends BaseLoginActivity{

    private EditText mPasswordEt;
    private EditText mPasswordAgainEt;
    private Button mConfirmBtn;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_set_login_password);
    }

    @Override
    protected void initView() {
        mPasswordEt = findViewById(R.id.password_et);
        mPasswordAgainEt = findViewById(R.id.password_again_et);
        mConfirmBtn = findViewById(R.id.confirm_btn);

    }

    @Override
    protected void initListener() {
        toolbarBack();
        toolbarRightTvOnClick(view -> {
            login();
        });

        mConfirmBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirm_btn) {
            onConfirmClick();
        }
    }

    private void onConfirmClick() {
        String password = mPasswordEt.getText().toString().trim();
        String passwordAgain = mPasswordAgainEt.getText().toString().trim();

        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordAgain)) {
            if (password.length() >= 6 && passwordAgain.length() >= 6) {
                if (password.equals(passwordAgain)) {
                    if (DqUtils.validate(password)) {
                        String newPassword = MD5.encrypt(IConstant.Password.PWD_MD5 + password, true);
                        setLoginPassword(newPassword);
                    }
                } else {
                    DqToast.showShort("输入密码不一致");
                }
            } else {
                DqToast.showShort("输入密码位数最少6位");
            }
        } else {
            DqToast.showShort("密码不能为空");
        }
    }

    private void setLoginPassword(String newPassword) {
        String phone = mUserInfoSp.getString(EBSharedPrefUser.phone, "");
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(IConstant.Login.PHONE, phone);
        hashMap.put(IConstant.Login.MSG, "");
        hashMap.put(IConstant.Login.PWD, newPassword);
        hashMap.put(IConstant.Login.TYPE, "3");
        hashMap.put(IConstant.Login.OLD_PWD, "");

        mPresenter.setPassword(DqUrl.url_set_pwd, hashMap);
    }

    private void login() {
        String account = mUserInfoSp.getString(EBSharedPrefUser.uid, "");
        String im_token = mUserInfoSp.getString(EBSharedPrefUser.im_token, "");
        LoginHelper.login(this, account, im_token);
    }


    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if(DqUrl.url_set_pwd.equals(url)) {
            DqToast.showShort(entity.content);
            login();
        }
    }
}
