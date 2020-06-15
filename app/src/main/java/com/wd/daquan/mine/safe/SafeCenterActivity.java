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

public class SafeCenterActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener{
    private LinearLayout layout_freeze;
    private LinearLayout layout_unfreeze;
    private CommTitle mCommtitle;

    @Override
    protected MinePresenter createPresenter() {
        return null;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.safe_center_activity);
    }

    @Override
    public void initView() {
        mCommtitle = findViewById(R.id.safeCenterActivityTitle);
        layout_freeze = findViewById(R.id.safeCenterActivityLayoutFreeze);
        layout_unfreeze = findViewById(R.id.safeCenterActivityLayoutUnFreeze);
        mCommtitle.setTitle(getString(R.string.safe_center));
    }

    @Override
    public void initListener() {
        layout_freeze.setOnClickListener(this);
        layout_unfreeze.setOnClickListener(this);
        mCommtitle.getLeftIv().setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comm_left_iv:
                finish();
                break;
            case R.id.safeCenterActivityLayoutFreeze://冻结
                NavUtils.gotoAboutFreezeActivity(this, KeyValue.ZERO_STRING);
                break;
            case R.id.safeCenterActivityLayoutUnFreeze://解冻
                NavUtils.gotoAboutFreezeActivity(this, KeyValue.ONE_STRING);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KeyValue.IntentCode.REQUEST_CODE_EXIT && resultCode == RESULT_OK){
            finish();
        }
    }
}
