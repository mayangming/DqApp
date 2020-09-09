package com.wd.daquan.mine.activity;

import android.view.View;
import android.widget.TextView;

import com.da.library.utils.BigDecimalUtils;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.mine.presenter.WalletCloudPresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.UserCloudWallet;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;

import java.util.HashMap;

/**
 * 零钱页面
 */
public class WalletCloudActivity extends DqBaseActivity<WalletCloudPresenter, DataBean> implements QCObserver {

    private View walletContainer;//斗圈零钱外布局
    private View withdraw;//提现
    private View record;//提现记录
    private View bindWx;//绑定微信
    private View payPsdSet;//提现密码
    private TextView walletCloudBalance;//零钱余额
    private TextView walletCloudAccumulatedIncome;//收益总额
    private UserCloudWallet userCloudWallet = new UserCloudWallet();
    @Override
    protected WalletCloudPresenter createPresenter() {
        return new WalletCloudPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_wallet_cloud);
    }

    @Override
    protected void initView() {
        walletContainer = findViewById(R.id.wallet_container);
        mTitleDqLayout = findViewById(R.id.toolbar);
        withdraw = findViewById(R.id.wallet_withdraw);
        record = findViewById(R.id.wallet_record);
        bindWx = findViewById(R.id.wallet_bind_wx);
        payPsdSet = findViewById(R.id.wallet_pay_psd_set);
        walletCloudBalance = findViewById(R.id.wallet_cloud_balance);
        walletCloudAccumulatedIncome = findViewById(R.id.wallet_cloud_accumulatedIncome);
        withdraw.setOnClickListener(this);
        record.setOnClickListener(this);
        bindWx.setOnClickListener(this);
        payPsdSet.setOnClickListener(this);
        walletContainer.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onMessage(String key, Object value) {
        switch (key){
            case MsgType.WITHDRAW_PWD_RESULT:{
                refreshUserCloudWallet();
                break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshUserCloudWallet();
    }

    private void refreshUserCloudWallet(){
        mPresenter.getUserCloudWallet(DqUrl.url_user_cloud_wallet,new HashMap<>());
    }


    public void onClick(View view){
        super.onClick(view);
        switch (view.getId()){
            case R.id.wallet_withdraw:
                if (userCloudWallet.getBalance() == 0){
                    DqToast.showShort("余额不足无法提现！");
                    break;
                }

                if (!userCloudWallet.isPwdIsSet()){
                    DqToast.showShort("请先设置密码才可以提现！");
                    //提现密码
                    NavUtils.gotoPayPasswordActivityActivity(this);
                    break;
                }

                NavUtils.gotoWithdrawActivity(this,userCloudWallet.getBalance());
                break;
            case R.id.wallet_record:
                NavUtils.gotoWithdrawRecordActivity(view.getContext());
                break;
            case R.id.wallet_bind_wx:
                NavUtils.gotoBindWxActivity(this);
                //绑定微信
                break;
            case R.id.wallet_pay_psd_set:
                //提现密码
                NavUtils.gotoPayPasswordActivityActivity(this);
                break;
            case R.id.wallet_container:
                NavUtils.gotoWithdrawRecordActivity(view.getContext());
                break;
        }
    }

    private void updateData(UserCloudWallet userCloudWallet){
        String accumulate = "累计收益 %s元";
        if (null == userCloudWallet){
            walletCloudBalance.setText("0.00");
            walletCloudAccumulatedIncome.setText(String.format(accumulate, "0.00"));
        }else {
            String balanceValue = BigDecimalUtils.penny2Dollar(userCloudWallet.getBalance()).toPlainString();
            String accumulatedValue = BigDecimalUtils.penny2Dollar(userCloudWallet.getAccumulatedIncome()).toPlainString();
            walletCloudBalance.setText(balanceValue);
            walletCloudAccumulatedIncome.setText(String.format(accumulate, accumulatedValue));
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (DqUrl.url_user_cloud_wallet.equals(url)){
            userCloudWallet = (UserCloudWallet)entity.data;
            updateData(userCloudWallet);
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        if (DqUrl.url_user_cloud_wallet.equals(url)){
            if (entity.result != 16001){
                DqToast.showShort("网络异常~");
            }
            updateData(null);
        }
    }
}