package com.wd.daquan.mine.safe;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.wd.daquan.mine.bean.FreezeTypeEntity;
import com.da.library.widget.CommTitle;

import java.util.LinkedHashMap;

public class DoAboutFreezeActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener{

    private EditText edit_phone;
    private TextView btn_contine;
    private TextView txt_type;
    private CommTitle mCommTitle;
    private QCSharedPrefManager manager;
    private String cur_phoneNum;
    private String txt_phoneNum;
    private String type;

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.do_about_freeze_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        mCommTitle = findViewById(R.id.doAboutFreezeTitle);
        type = getIntent().getStringExtra(KeyValue.ABOUT_FREEZE_TYPE);
        edit_phone= findViewById(R.id.doAboutFreezeEtPhone);
        btn_contine= findViewById(R.id.doAboutFreezeBtn);
        txt_type= findViewById(R.id.doAboutFreezeTxtIns);
    }

    @Override
    public void initListener() {
        mCommTitle.getLeftIv().setOnClickListener(this);
        btn_contine.setOnClickListener(this);
        edit_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String contents = charSequence.toString();
                int length = contents.length();
                if (length == 4) {
                    if (contents.substring(3).equals(" ")) { // -
                        contents = contents.substring(0, 3);
                        edit_phone.setText(contents);
                        edit_phone.setSelection(contents.length());
                    } else { // +
                        contents = contents.substring(0, 3) + " " + contents.substring(3);
                        edit_phone.setText(contents);
                        edit_phone.setSelection(contents.length());
                    }
                } else if (length == 9) {
                    if (contents.substring(8).equals(" ")) { // -
                        contents = contents.substring(0, 8);
                        edit_phone.setText(contents);
                        edit_phone.setSelection(contents.length());
                    } else {// +
                        contents = contents.substring(0, 8) + " " + contents.substring(8);
                        edit_phone.setText(contents);
                        edit_phone.setSelection(contents.length());
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void initData() {
        manager= QCSharedPrefManager.getInstance();
        cur_phoneNum = manager.getKDPreferenceUserInfo().getString(EBSharedPrefUser.phone,"");
        if(KeyValue.ZERO_STRING.equals(type)){
            mCommTitle.setTitle(getString(R.string.about_freeze_freeze));
        }else if(KeyValue.ONE_STRING.equals(type)){
            mCommTitle.setTitle(getString(R.string.about_freeze_unfreeze));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comm_left_iv:
                finish();
                break;
            case R.id.doAboutFreezeBtn:
                txt_phoneNum = edit_phone.getText().toString().trim();
                txt_phoneNum = txt_phoneNum.replace(" ","");
                if (DqUtils.validatePhoneNumber(txt_phoneNum,this)) {
                    LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
                    linkedHashMap.put("phone", txt_phoneNum);
                    linkedHashMap.put("imei", DqUtils.deviceId(DoAboutFreezeActivity.this));
                    mPresenter.getWhether_block(DqUrl.url_whether_block, linkedHashMap);
                }
                break;
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        if (KeyValue.Code.TOKEN_ERR == code){
            DqUtils.bequit(entity,this);
        }else {
            DqToast.showShort(entity.content);
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (DqUrl.url_whether_block.equals(url)) {
            if (0 == code) {
                if (KeyValue.ZERO_STRING.equals(type)) {//冻结
                    FreezeTypeEntity dataSelf = (FreezeTypeEntity) entity.data;
                    if (KeyValue.ZERO_STRING.equals(dataSelf.whether_block)) {//不可以
                        NavUtils.gotoFreezeTypeActivity(this, txt_phoneNum);
                    } else if (KeyValue.ONE_STRING.equals(dataSelf.whether_block)) {//可以
                        //2018/9/13 差最后完成的界面
//                        NavUtils.gotoBlockDone(this, etnum);
                    }
                }else if (KeyValue.ONE_STRING.equals(type)) {//解冻
                    FreezeTypeEntity dataSelf = (FreezeTypeEntity) entity.data;
                    if (KeyValue.ZERO_STRING.equals(dataSelf.whether_unblock)) {//不可以
                        NavUtils.gotoDoFreezeNextActivity(this, txt_phoneNum, KeyValue.ONE_STRING);
                    } else if  (KeyValue.ONE_STRING.equals(dataSelf.whether_unblock)){//可以
                        //2018/9/13 差最后完成的界面
//                        NavUtils.gotoUnfreezeDone(this, etnum);
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KeyValue.IntentCode.REQUEST_CODE_EXIT && resultCode == RESULT_OK){
            setResult(RESULT_OK);
            finish();
        }
    }
}
