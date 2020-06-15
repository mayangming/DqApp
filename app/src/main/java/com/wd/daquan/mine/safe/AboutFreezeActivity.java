package com.wd.daquan.mine.safe;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.da.library.widget.CommTitle;


public class AboutFreezeActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener {
    private TextView btn_doTask;
    private TextView txt_first;
    private TextView txt_second;
    private CommTitle mCommTitle;
    private String type;//0冻结 1解冻
    @Override
    protected MinePresenter createPresenter() {
        return null;
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.about_freeze_activity);
    }

    @Override
    protected void init() {
        type = getIntent().getStringExtra(KeyValue.ABOUT_FREEZE_TYPE);
    }

    @Override
    public void initView() {
        mCommTitle =  findViewById(R.id.aboutFreezeActivityTitle);
        btn_doTask = findViewById(R.id.aboutFreezeActivityBtn);
        txt_first = findViewById(R.id.aboutFreezeActivityTxtFirst);
        txt_second = findViewById(R.id.aboutFreezeActivityTxtSecond);
    }

    @Override
    public void initListener() {
        btn_doTask.setOnClickListener(this);
        mCommTitle.getLeftIv().setOnClickListener(this);
    }

    @Override
    public void initData() {
        if(KeyValue.ZERO_STRING.equals(type)){
            mCommTitle.setTitle(getString(R.string.about_freeze_freeze));
            txt_first.setText(getString(R.string.about_freeze_freeze_ins_f));
            txt_second.setText(getString(R.string.about_freeze_freeze_ins_s));
            btn_doTask.setText(getString(R.string.about_freeze_freeze_start));
        }else if(KeyValue.ONE_STRING.equals(type)){
            mCommTitle.setTitle(getString(R.string.about_freeze_unfreeze));
            txt_first.setText(getString(R.string.safe_center_unfreeze_ins));
            txt_second.setText(getString(R.string.about_freeze_unfreeze_ins_s));
            btn_doTask.setText(getString(R.string.about_freeze_unfreeze_start));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comm_left_iv:
                finish();
                break;
            case R.id.aboutFreezeActivityBtn:
                NavUtils.gotoDoAboutFreezeActivity(this, type);
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
