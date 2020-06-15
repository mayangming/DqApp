package com.wd.daquan.mine.safe;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.model.bean.DataBean;
import com.da.library.constant.IConstant;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.da.library.tools.MD5;
import com.da.library.widget.CommTitle;
import com.da.library.widget.CountDownTimerUtils;

import java.util.LinkedHashMap;


public class DoFreezeNextActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener{

    private TextView btn_next;
    private TextView btn_getCode;
    private EditText edit_code;
    private TextView txt_type;
    private CommTitle mCommTitle;
    private String phoneNumber;
    private String code;
    private String type;

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.do_freeze_next_activity);
    }

    @Override
    protected void init() {
        type=getIntent().getStringExtra(KeyValue.ABOUT_FREEZE_TYPE);
        phoneNumber=getIntent().getStringExtra(KeyValue.PHONE);
    }

    @Override
    public void initView() {
        mCommTitle = findViewById(R.id.dofreezeNextTitle);
        btn_next = findViewById(R.id.dofreezeNextBtnNext);
        txt_type = findViewById(R.id.dofreezeNextTxtType);
        btn_getCode = findViewById(R.id.dofreezeNextTxtGetCode);
        edit_code= findViewById(R.id.dofreezeNextEtCode);
    }

    @Override
    public void initListener() {
        btn_next.setOnClickListener(this);
        btn_getCode.setOnClickListener(this);
        mCommTitle.getLeftIv().setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (KeyValue.ZERO_STRING.equals(type)) {
            btn_next.setText(getString(R.string.freeze_next_confirm_freeze));
            mCommTitle.setTitle(getString(R.string.safe_center_freeze));
        }else if (KeyValue.ONE_STRING.equals(type)){
            btn_next.setText(getString(R.string.freeze_next_confirm_unfreeze));
            mCommTitle.setTitle(getString(R.string.safe_center_unfreeze));
        }

        String ruNum="请输入手机"+phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7, 11)+"收到的短信验证码";
        txt_type.setText(ruNum);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comm_left_iv:
                finish();
                break;
            case R.id.dofreezeNextBtnNext:
                code = edit_code.getText().toString();
                if (!TextUtils.isEmpty(code)){
                    if (KeyValue.ZERO_STRING.equals(type)) {//冻结
                        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
                        linkedHashMap.put("phone", phoneNumber);
                        linkedHashMap.put("imei", DqUtils.deviceId(DoFreezeNextActivity.this));
                        linkedHashMap.put("device", KeyValue.ONE_STRING);
                        linkedHashMap.put("msg_code", code);
                        mPresenter.doFreezeAndUnFreeze(DqUrl.url_block, linkedHashMap);
                    }else if (KeyValue.ONE_STRING.equals(type)){//解冻
                        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
                        linkedHashMap.put("phone", phoneNumber);
                        linkedHashMap.put("imei", DqUtils.deviceId(DoFreezeNextActivity.this));
                        linkedHashMap.put("device", KeyValue.ONE_STRING);
                        linkedHashMap.put("msg_code", code);
                        mPresenter.doFreezeAndUnFreeze(DqUrl.url_unblock, linkedHashMap);
                    }
                }
                break;
            case R.id.dofreezeNextTxtGetCode:
                getCode();
                break;
        }
    }
   private void getCode(){
       if (DqUtils.validatePhoneNumber(phoneNumber,this)) {
           //获取验证码接口
           LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
           String key = IConstant.Login.CHATDQ + phoneNumber;
           String token_key = MD5.encrypt(key).toLowerCase();
           linkedHashMap.put("phone", phoneNumber);
           linkedHashMap.put("token_key", token_key);
           CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(btn_getCode, 60000,
                   1000, this);
           mCountDownTimerUtils.start();
           mPresenter.getPhoneCode(DqUrl.url_get_phone_msg, linkedHashMap);
       }
   }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        DqUtils.bequit(entity,this);
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (DqUrl.url_get_phone_msg.equals(url)) {

            if (0 == code) {
                DqToast.showShort(entity.content);
            } else {
                DqToast.showShort(entity.content);
            }

        } else if (DqUrl.url_block.equals(url)) {
            state();
        } else if (DqUrl.url_unblock.equals(url)) {
            state();
        }
    }

    private void state(){
//        EBSharedPrefManager manager= BridgeFactory.getBridge(Bridges.SHARED_PREFERENCE);
//        String status=manager.getKDPreferenceUserInfo().getString(Constants.STATE,"");
        setResult(RESULT_OK);
        finish();
    }
}
