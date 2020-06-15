package com.wd.daquan.mine.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.da.library.widget.CommTitle;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;

/**
 * 投诉举报入口
 * 华为应用市场需要这个功能，这个功能没有接口，仅是模拟操作
 */
public class ComplaintActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener {
    private CommTitle mCommTitle;
    private TextView complainSubmit;
    private EditText complainContent;
    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_complaint);
    }

    @Override
    protected void initView() {
        mCommTitle = findViewById(R.id.aboutActivityCommtitle);
        complainSubmit = findViewById(R.id.complain_submit);
        complainContent = findViewById(R.id.complain_content);
        mCommTitle.setTitle(R.string.complain);
        complainSubmit.setOnClickListener(this);
        mCommTitle.getLeftIv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.complain_submit:
                String content = complainContent.getEditableText().toString();
                if (TextUtils.isEmpty(content)){
                    DqToast.showShort("投诉内容不可以为null");
                    return;
                }
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DqToast.showShort("信息提交成功，请等待处理结果!");
                        finish();
                    }
                },300);
                break;
        }
    }
}
