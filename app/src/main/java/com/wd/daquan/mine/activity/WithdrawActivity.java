package com.wd.daquan.mine.activity;

import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.da.library.utils.AESUtil;
import com.da.library.utils.BigDecimalUtils;
import com.da.library.utils.NetToolsUtil;
import com.lzj.pass.dialog.PayPassDialog;
import com.lzj.pass.dialog.PayPassView;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.mine.presenter.WalletCloudPresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.WXLoginEntity;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.QCObserver;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 提现页面
 */
public class WithdrawActivity extends DqBaseActivity<WalletCloudPresenter, DataBean> implements QCObserver{

    public final static String WITHDRAW_BALANCE = "withdrawBalance";
    private long balance = 0;
    private View withdrawNext;
    private EditText payNumberEdt;
    private View balanceAll;//全部提现
    private TextView bindWeChatStatus;//绑定状态
    private View bindWechatRl;//绑定状态布局
    private String uid;
    private WXLoginEntity mWxLoginEntity;
//    private int amountLimit = BuildConfig.IS_DUBUG ? 1 : 100;//提现金额限制
    private int amountLimit = 100;//提现金额限制
    @Override
    protected WalletCloudPresenter createPresenter() {
        return new WalletCloudPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_withdraw);
    }

    @Override
    protected void initView() {
        mTitleDqLayout = findViewById(R.id.toolbar);
        withdrawNext = findViewById(R.id.withdraw_next);
        payNumberEdt = findViewById(R.id.withdraw_pay_num);
        balanceAll = findViewById(R.id.withdraw_balance_all);
        bindWeChatStatus = findViewById(R.id.bind_wechat_status);
        bindWechatRl = findViewById(R.id.bind_wechat_rl);
        withdrawNext.setOnClickListener(this);
        balanceAll.setOnClickListener(this);
        bindWechatRl.setOnClickListener(this);
        payNumberEdt.setFilters(new InputFilter[]{lengthFilter});
    }

    @Override
    protected void initData() {
        MsgMgr.getInstance().attach(this);
        balance = getIntent().getLongExtra(WITHDRAW_BALANCE,0);
        QCSharedPrefManager mSharedPrefManager = QCSharedPrefManager.getInstance();
        EBSharedPrefUser mEBSharedPrefUser = mSharedPrefManager.getKDPreferenceUserInfo();
        uid = mEBSharedPrefUser.getString(EBSharedPrefUser.uid, "");

        if(mPresenter != null){
            LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("uid", uid);
            mPresenter.getBindWeixinStatus(DqUrl.url_oauth_bindWeixinStatus, linkedHashMap);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.withdraw_balance_all:
                BigDecimal bigDecimal = BigDecimalUtils.penny2Dollar(balance);
                payNumberEdt.setText(bigDecimal.toPlainString());
                break;
            case R.id.withdraw_next:
                showPayPwdPop();
                break;
            case R.id.bind_wechat_rl:
                NavUtils.gotoBindWxActivity(this);
                break;
        }
    }

    @Override
    public void onMessage(String key, Object value) {
        Integer status = (Integer) value;
        Log.e("YM","接收状态:"+status);
        status(status);
    }

    //状态
    private void status(int status){
        switch (status) {
            case 1://已绑定
                bindWeChatStatus.setText("已绑定");
                break;
            case 0://未绑定
                bindWeChatStatus.setText("未绑定");
                break;
        }
    }

    private void showPayPwdPop(){
        String amount = payNumberEdt.getText().toString();
        if (TextUtils.isEmpty(amount)){
            DqToast.showShort("金额不允许为空！");
            return;
        }
        BigDecimal bigDecimal = BigDecimalUtils.dollar2Penny(amount);

        if (bigDecimal.longValue() < amountLimit){
            DqToast.showShort("金额最少提现1元！");
            return;
        }

        if (bigDecimal.longValue() > balance){
            DqToast.showShort("提现金额超过余额，无法提现！");
            return;
        }
        if (0 == mWxLoginEntity.status){
            DqToast.showShort("请先绑定微信后才可以提现！");
            return;
        }
        final PayPassDialog dialog=new PayPassDialog(this);
        dialog.getPayViewPass()
                .setPayClickListener(new PayPassView.OnPayClickListener() {
                    @Override
                    public void onPassFinish(String passContent) {
                        //6位输入完成回调
                        try {
                            String newPwd = AESUtil.encrypt(passContent);
                            Map<String,String> params = new HashMap<>();
                            params.put("spbillCreateIp", NetToolsUtil.getLocalHostIp());
                            params.put("pwd",newPwd);
                            params.put("amount",bigDecimal.toPlainString());
                            mPresenter.getUserCashWithdrawal(DqUrl.url_user_cash_withdrawal,params);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                    @Override
                    public void onPayClose() {
                        dialog.dismiss();
                        //关闭回调
                    }
                    @Override
                    public void onPayForget() {
                        dialog.dismiss();
                        //点击忘记密码回调
                        DqToast.showShort("忘记密码回调");
                    }
                });
    }

    private InputFilter lengthFilter = (source, start, end, dest, dstart, dend) -> {
        // source:当前输入的字符
        // start:输入字符的开始位置
        // end:输入字符的结束位置
        // dest：当前已显示的内容
        // dstart:当前光标开始位置
        // dent:当前光标结束位置
        if (dest.length() == 0 && source.equals(".")) {
            return "0.";
        }
        String dValue = dest.toString();
        String[] splitArray = dValue.split("\\.");
        if (splitArray.length > 1) {
            String dotValue = splitArray[1];
            if (dotValue.length() == 2 && dest.length() - dstart < 3){ //输入框小数的位数是2的情况时小数位不可以输入，整数位可以正常输入
                return "";
            }
        }
        return null;
    };

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (DqUrl.url_user_cash_withdrawal.equals(url)){
            if (0 == entity.result){
                NavUtils.gotoWithdrawResultActivity(this,entity.result,entity.msg);
                finish();
            }else {
                DqToast.showCenterLong(entity.content);
            }
        }else if (DqUrl.url_oauth_bindWeixinStatus.equals(url)) {
            mWxLoginEntity = (WXLoginEntity) entity.data;
            status(mWxLoginEntity.status);
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        if (DqUrl.url_user_cash_withdrawal.equals(url)){
            NavUtils.gotoWithdrawResultActivity(this,entity.result,entity.content);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MsgMgr.getInstance().detach(this);
    }
}