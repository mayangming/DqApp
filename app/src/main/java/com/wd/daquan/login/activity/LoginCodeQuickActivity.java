package com.wd.daquan.login.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.da.library.listener.ICommListDialogListener;
import com.da.library.widget.CommonListDialog;
import com.wd.daquan.R;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.imui.dialog.CaptchaImgDialog;
import com.wd.daquan.model.sp.EBSharedPrefUser;

import java.util.ArrayList;
import java.util.List;

import static com.da.library.constant.IConstant.Login.LOGIN_TYPE;

/**
 * @Author: 方志
 * @Time: 2018/9/11 15:36
 * @Description: 验证码快捷登录
 */
public class LoginCodeQuickActivity extends BaseLoginActivity implements  CaptchaImgDialog.Operator {

    private TextView mPhoneNumberTv;
    private EditText mCodeEt;
    private Button mLoginBtn;
    private TextView mLoginPasswordTv;
    private ImageView mPortraitTv;
    private TextView mForgetPasswordTv;
    private TextView mLoginMoreTv;
    private CommonListDialog mListDialog;
    private View mBackIv;
    private CaptchaImgDialog captchaImgDialog;
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_login_code_again);
    }

    @Override
    protected void initView() {
        mBackIv = findViewById(R.id.back_iv);
        mScrollView = findViewById(R.id.scroll_view);
        mTranslationContainerRl = findViewById(R.id.translation_container_rl);
        mPhoneNumberTv = findViewById(R.id.login_code_again_phone_number_tv);
        mCodeEt = findViewById(R.id.login_again_code_et);
        mGetCodeTv = findViewById(R.id.login_again_get_code_tv);
        mLoginBtn = findViewById(R.id.login_code_again_login_btn);
        mLoginPasswordTv = findViewById(R.id.login_password_again_tv);
        mPortraitTv = findViewById(R.id.login_code_again_portrait_iv);
        mForgetPasswordTv = findViewById(R.id.login_code_again_forget_password_tv);
        mLoginMoreTv = findViewById(R.id.login_code_again_more_tv);
        initDialog();
    }
    private void initDialog(){
        captchaImgDialog = new CaptchaImgDialog();
    }
    @Override
    protected void initData() {
        super.initData();
        String phone = mUserInfoSp.getString(EBSharedPrefUser.phone, "");
        String portrait = mUserInfoSp.getString(EBSharedPrefUser.headpic, "");
        if (!TextUtils.isEmpty(phone)) {
            mPhoneNumberTv.setText(phone);
        }
        GlideUtils.loadHeader(this, portrait, mPortraitTv);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBackIv.setOnClickListener(this);
        mGetCodeTv.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
        mLoginPasswordTv.setOnClickListener(this);
        mForgetPasswordTv.setOnClickListener(this);
        mLoginMoreTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_iv:
                finish();
                break;
            case R.id.login_again_get_code_tv:
                Bundle bundle = new Bundle();
                bundle.putString(CaptchaImgDialog.PHONE_NUMBER,mPhoneNumberEt.getText().toString());
                captchaImgDialog.setArguments(bundle);
                captchaImgDialog.show(getSupportFragmentManager(),"");
                break;
            case R.id.login_code_again_login_btn:
                login(mPhoneNumberTv, mCodeEt, "2");
                break;
            case R.id.login_password_again_tv:
                //切换密码登录
                NavUtils.gotoLoginPwdAgainActivity(this);
                finish();
                break;
            case R.id.login_code_again_forget_password_tv:
                //QcToastUtil.showToast(this, "忘记密码");
                NavUtils.gotoForgetLoginPasswordActivity(this);
                break;
            case R.id.login_code_again_more_tv:
                showListDialog();
                break;
        }
    }


    private void showListDialog() {
        mListDialog = new CommonListDialog(this);
        List<String> items = new ArrayList<>();
        items.add(getString(R.string.switch_account));
        items.add(getString(R.string.register));
        //items.add(getString(R.string.security_center));

        mListDialog.setItems(items);
        mListDialog.show();

        mListDialog.setListener(mListDialogListener);
    }

    /**
     * 更多dialog监听事件
     */
    private ICommListDialogListener mListDialogListener = (item, position) -> {
        switch (position) {
            case 0://切换账号
                NavUtils.gotoLoginPasswordActivity(LoginCodeQuickActivity.this);
                break;
            case 1://注册
                NavUtils.gotoRegisterActivity(LoginCodeQuickActivity.this);
                break;
            //case 2:
            //    Toast.makeText(LoginCodeQuickActivity.this, item, Toast.LENGTH_SHORT).show();
            //   break;
        }
    };

    @Override
    public void cancel() {

    }

    @Override
    public void sure(String value) {
        //获取验证码
        getVerificationCode(mPhoneNumberTv, LOGIN_TYPE,value);
    }
}