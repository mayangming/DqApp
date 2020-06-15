package com.wd.daquan.mine.wallet.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.model.bean.DataBean;
import com.da.library.tools.AESHelper;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.mine.listener.PayDetailListener;
import com.wd.daquan.mine.wallet.helper.OptionsHelper;
import com.wd.daquan.mine.wallet.presenter.WalletPresenter;
import com.meetqs.qingchat.pickerview.view.OptionsPickerView;
import com.da.library.tools.AppInfoUtils;
import com.da.library.tools.DateUtil;
import com.da.library.widget.CommTitle;
import com.netease.nim.uikit.common.util.string.StringUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by DELL on 2019/5/24.
 */

public class WalletVerifyActivity extends DqBaseActivity<WalletPresenter, DataBean> implements View.OnClickListener,
        PayDetailListener{
    private CommTitle mCommTitle;
    private EditText nameEditText;
    private EditText cardNoEditText;
    private TextView jobTxt;
    private TextView areaTxt;
    private EditText contactAddressEditText;
    private EditText pwdEditText;
    private EditText pwdAgainEditText;
    private TextView cardNoValidityTxt;
    private Button confirmBtn;
    private String verifyToken;
    private String proviceString;
    private String cityString;
    private String areaString;
    private OptionsPickerView mPvOptions;
    private String times;

    @Override
    protected WalletPresenter createPresenter() {
        return new WalletPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.wallet_verify_activity);
    }

    @Override
    protected void initView() {
        mCommTitle = findViewById(R.id.walletVerifyActivityTitle);
        nameEditText = findViewById(R.id.walletVerifyNameTxt);
        cardNoEditText = findViewById(R.id.walletVerifyCardNoTxt);
        jobTxt = findViewById(R.id.walletVerifyJobTxt);
        areaTxt = findViewById(R.id.walletVerifyAreaTxt);
        contactAddressEditText = findViewById(R.id.walletVerifyContactsAddressTxt);
        pwdEditText = findViewById(R.id.walletVerifyPwdTxt);
        pwdAgainEditText = findViewById(R.id.walletVerifyPwdAgainTxt);
        confirmBtn = findViewById(R.id.walletVerifyConfirmBtn);
        cardNoValidityTxt = findViewById(R.id.walletVerifyCardNoValidityTxt);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mCommTitle.getLeftIv().setOnClickListener(this);
        jobTxt.setOnClickListener(this);
        areaTxt.setOnClickListener(this);
        pwdAgainEditText.setOnClickListener(this);
        pwdAgainEditText.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        cardNoValidityTxt.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        verifyToken = getIntent().getStringExtra("params");
        OptionsHelper.Companion.get().initJsonData();

        Calendar now = Calendar.getInstance();
        int years = now.get(Calendar.YEAR);
        int date = now.get(Calendar.MONTH) + 1;
        List<String> listYear = DateUtil.getWheelYearLayer(this, now);
        List<String> listMonth = DateUtil.getWheelMonth(this);
        List<String> listDate = DateUtil.getWheelDate(this);
        mPvOptions = DialogUtils.showYearToDate(this, listYear, listMonth
                , listDate, date, this);
        times = String.valueOf(years) +  DateUtil.dateFormat(String.valueOf(date));
    }

    private void onConfirm() {
        String nameString = nameEditText.getText().toString().trim();
        String cardNoString = cardNoEditText.getText().toString().trim();
        String jobString = jobTxt.getText().toString().trim();
        String contactsAddressString = contactAddressEditText.getText().toString().trim();
        String pwdString = pwdEditText.getText().toString().trim();
        String pwdAgainString = pwdAgainEditText.getText().toString().trim();

        if (TextUtils.isEmpty(nameString)) {
            DqToast.showShort("姓名不能为空");
            return;
        }
        if (TextUtils.isEmpty(cardNoString)) {
            DqToast.showShort("身份证号不能为空");
            return;
        }
        if (TextUtils.isEmpty(jobString)) {
            DqToast.showShort("职业不能为空");
            return;
        }
        if (TextUtils.isEmpty(areaTxt.getText().toString().trim())) {
            DqToast.showShort("地区不能为空");
            return;
        }
        if (TextUtils.isEmpty(contactsAddressString)) {
            DqToast.showShort("联系地址不能为空");
            return;
        }
        if (TextUtils.isEmpty(pwdString)) {
            DqToast.showShort("密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(pwdAgainString)) {
            DqToast.showShort("密码不能为空");
            return;
        }
        if(pwdString.length() < 6 || pwdAgainString.length() < 6){
            DqToast.showShort("请设置6位密码");
            return;
        }
        if(!pwdString.equals(pwdAgainString)){
            DqToast.showShort("两次密码输入不一致");
            return;
        }

        String pwdEncrypt = StringUtil.getPwdData(pwdString);
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("username", nameString);
            jsonObject.put("no_idcard", cardNoString);
            jsonObject.put("pwd_pay", pwdEncrypt);
            jsonObject.put("addr_conn", contactsAddressString);
            jsonObject.put("oid_job", jobString);
            jsonObject.put("verify_token", verifyToken);
            jsonObject.put("addr_pro", proviceString);
            jsonObject.put("addr_city", cityString);
            jsonObject.put("addr_dist", areaString);
            jsonObject.put("source", "ANDROID");
            jsonObject.put("frms_imei", AppInfoUtils.getPhoneImei());
            jsonObject.put("pkg_name", AppInfoUtils.getPackageName());
            jsonObject.put("app_name", getResources().getString(R.string.app_name));
            jsonObject.put("exp_idcard", times);
            jsonObject.put("frms_mac_addr", AppInfoUtils.getMacAddress(this));
            jsonObject.put("frms_mechine_id", AppInfoUtils.getPseudoUniqueID());
        }catch (Exception e){
            e.printStackTrace();
        }
        LinkedHashMap<String,String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("intentUrl", AESHelper.encryptString(jsonObject.toString()));
        mPresenter.get(DqUrl.url_wallet_open, linkedHashMap);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.comm_left_iv:
                finish();
                break;
            case R.id.walletVerifyJobTxt://选择职业
                //选择职业
                NavUtils.gotoChoiceProfessionalActivity(this, KeyValue.Wallet.REQUEST_CODE_PROFESSIONAL);
                break;
            case R.id.walletVerifyAreaTxt://地区
                OptionsHelper.Companion.get().showPickerView(WalletVerifyActivity.this, new OptionsHelper.OnCityChoiceListener() {
                    @Override
                    public void getCity(@NotNull String province, @NotNull String city, @NotNull String area) {
                        proviceString = province;
                        cityString = city;
                        areaString = area;
//                        DqToast.showShort(province + city + area);
                        areaTxt.setText(province + city + area);
                    }
                });
                break;
            case R.id.walletVerifyPwdTxt://密码
                break;
            case R.id.walletVerifyPwdAgainTxt://再次输入
                break;
            case R.id.walletVerifyConfirmBtn://确认
                onConfirm();
                break;
            case R.id.walletVerifyCardNoValidityTxt:
                mPvOptions.show();
                break;
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if(DqUrl.url_wallet_open.equals(url)){
            if(0 == code){
                DqToast.showShort("开通成功");
                setResult(RESULT_OK);
            }
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
        if(KeyValue.Wallet.REQUEST_CODE_PROFESSIONAL == requestCode && RESULT_OK == resultCode){
            String jobString = data.getStringExtra(KeyValue.Wallet.CHOICE_PROFESSIONAL);
            jobTxt.setText(jobString);
        }
    }

    @Override
    public void payDetailClick(String mYear, String mMonth, String date) {
        times = mYear + mMonth + date;
        cardNoValidityTxt.setText(times);
    }
}
