package com.wd.daquan.mine.safe;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.da.library.widget.CommTitle;

public class FreezeTypeActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener {
    private LinearLayout layout_phone;
    private CommTitle mCommTitle;
    private String phone;

    @Override
    protected MinePresenter createPresenter() {
        return null;
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.freeze_type_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        mCommTitle = findViewById(R.id.freezeTypeTitle);
        layout_phone = findViewById(R.id.freezeTypeLayoutPhoneNum);
    }

    @Override
    public void initListener() {
        layout_phone.setOnClickListener(this);
        mCommTitle.getLeftIv().setOnClickListener(this);
        mCommTitle.setTitle(getString(R.string.safe_center_freeze));
    }

    @Override
    public void initData() {
        phone = getIntent().getStringExtra(KeyValue.PHONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comm_left_iv:
                finish();
                break;
            case R.id.freezeTypeLayoutPhoneNum:
                NavUtils.gotoDoFreezeNextActivity(this, phone, KeyValue.ZERO_STRING);
                break;
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
