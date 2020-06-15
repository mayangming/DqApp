package com.wd.daquan.mine.wallet.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.model.bean.DataBean;
import com.da.library.tools.AESHelper;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.mine.wallet.bean.VericationCodeBean;
import com.wd.daquan.mine.wallet.presenter.WalletPresenter;
import com.da.library.widget.CountDownTimerUtils;
import com.netease.nim.uikit.common.util.string.StringUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class VerificationCodeActivity extends DqBaseActivity<WalletPresenter, DataBean> {

    private EditText mPhoneEditTxt;
    private EditText mCodeEditTxt;
    private TextView mSendCodeTxt;
    private Button nextBtn;
    private VericationCodeBean vericationCodeBean;

    @Override
    protected WalletPresenter createPresenter() {
        return new WalletPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.verification_code_activity);
    }

    @Override
    protected void initView() {
        mTitleLayout = findViewById(R.id.verificationCodeTitle);
        mPhoneEditTxt = findViewById(R.id.verificationCodePhoneEditText);
        mCodeEditTxt = findViewById(R.id.verificationCodeCodeEditText);
        mSendCodeTxt = findViewById(R.id.verificationCodeSend);
        nextBtn = findViewById(R.id.verificationCodeNextBtn);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        super.initListener();
        mTitleLayout.getLeftIv().setOnClickListener(this);
        mSendCodeTxt.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        switch (id){
            case R.id.comm_left_iv:
                finish();
                break;
            case R.id.verificationCodeSend:
                sendCode();
                break;
            case R.id.verificationCodeNextBtn:
//                NavUtils.gotoWalletVerifyActivity(this, "");
                registerOption();
                break;
        }
    }
    /**
     * 发送验证码
     */
    private void sendCode() {
        String phoneNum = mPhoneEditTxt.getText().toString().trim().replace(" ", "");
        if (validatePhoneNumber(phoneNum)) {
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("mobile", phoneNum);
            }catch (Exception e){
                e.printStackTrace();
            }
            LinkedHashMap<String,String> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("intentUrl", AESHelper.encryptString(jsonObject.toString()));
            mPresenter.getVerificationCode(DqUrl.url_wallet_verification_get_code, linkedHashMap);
        }
    }
    /**
     * 执行功能
     */
    private void registerOption() {
        String phone = mPhoneEditTxt.getText().toString();
        String code = mCodeEditTxt.getText().toString();

        if (TextUtils.isEmpty(phone)){
            DqToast.showShort("号码不能为空");
            return;
        }
        if (TextUtils.isEmpty(code)){
            DqToast.showShort("验证码不能为空");
            return;
        }
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("verify_token", vericationCodeBean.getVerify_token());
            jsonObject.put("verify_code", code);
        }catch (Exception e){
            e.printStackTrace();
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("intentUrl", AESHelper.encryptString(jsonObject.toString()));
        mPresenter.verfiyVerificationCode(DqUrl.url_wallet_verification_verify, hashMap);

    }



    /**
     * 验证手机号码是否合乎规则
     */
    private boolean validatePhoneNumber(String phoneNumbers) {
        String phoneNumber = phoneNumbers.replace(" ", "");
        if (!StringUtil.isMobileNO(phoneNumber)) {
            DqToast.showShort("请输入正确的手机号码");
            return false;
        }
        return true;
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (DqUrl.url_wallet_verification_get_code.equals(url)) {
            vericationCodeBean = (VericationCodeBean)entity.data;
            // 倒计时
            CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(mSendCodeTxt, 60000,
                    1000, this);
            mCountDownTimerUtils.start();
        } else if (DqUrl.url_wallet_verification_verify.equals(url)) {
            VericationCodeBean data = (VericationCodeBean)entity.data;
            NavUtils.gotoWalletVerifyActivity(this, data.getVerify_token());
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        DqUtils.bequit(entity, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(KeyValue.ONE == requestCode && RESULT_OK == resultCode){
            finish();
        }
    }
}
