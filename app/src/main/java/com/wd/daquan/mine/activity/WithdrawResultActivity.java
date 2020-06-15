package com.wd.daquan.mine.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.mine.presenter.WalletCloudPresenter;
import com.wd.daquan.model.bean.DataBean;

/**
 * 提现结果页面
 */
public class WithdrawResultActivity extends DqBaseActivity<WalletCloudPresenter, DataBean> {

    public final static String WITHDRAW_RESULT_STATUS = "withdrawResultStatus";
    public final static String WITHDRAW_RESULT_MESSAGE = "withdrawResultErrorMessage";

    private int withdrawResultStatus = 0;//提现结果状态
    private String withdrawResultErrorMessage = "";//提现错误信息

    private ImageView withdrawResultLogo;//提现logo
    private TextView withdrawResultTitle;//提现标题
    private TextView withdrawResultMessage;//提现结果信息
    private TextView withdrawRecord;//提现记录
    private TextView withdrawBack;//返回

    @Override
    protected WalletCloudPresenter createPresenter() {
        return new WalletCloudPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_withdraw_result);

    }

    @Override
    protected void initView() {
        mTitleDqLayout = findViewById(R.id.toolbar);
        withdrawResultLogo = findViewById(R.id.withdraw_result_logo);
        withdrawResultTitle = findViewById(R.id.withdraw_result_title);
        withdrawResultMessage = findViewById(R.id.withdraw_result_message);
        withdrawRecord = findViewById(R.id.withdraw_record);
        withdrawBack = findViewById(R.id.withdraw_back);
        withdrawRecord.setOnClickListener(this);
        withdrawBack.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        withdrawResultStatus = getIntent().getIntExtra(WITHDRAW_RESULT_STATUS,0);
        withdrawResultErrorMessage = getIntent().getStringExtra(WITHDRAW_RESULT_MESSAGE);
        if (0 == withdrawResultStatus){//提现成功
            withdrawResultLogo.setImageResource(R.mipmap.withdraw_result_success);
            withdrawResultTitle.setText("提现成功");
            withdrawResultMessage.setText("我们将在2小时内，将提现金额转入您的微信账户");
            withdrawRecord.setVisibility(View.VISIBLE);
        }else {//提现异常
            withdrawResultLogo.setImageResource(R.mipmap.withdraw_result_fail);
            withdrawResultTitle.setText("提现异常");
            withdrawResultMessage.setText(withdrawResultErrorMessage);
            withdrawRecord.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.withdraw_record:
                NavUtils.gotoWithdrawRecordActivity(v.getContext());
                break;
            case R.id.withdraw_back:
                finish();
                break;
        }
    }
}