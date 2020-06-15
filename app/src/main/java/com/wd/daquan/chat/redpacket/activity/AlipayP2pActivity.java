package com.wd.daquan.chat.redpacket.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.chat.bean.AlipayEntity;
import com.wd.daquan.chat.redpacket.RedPacketPresenter;
import com.wd.daquan.chat.redpacket.pay.PayResult;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.utils.CashierInputFilter;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.model.log.DqToast;
import com.da.library.tools.Utils;
import com.da.library.widget.CommTitle;
import com.netease.nim.uikit.common.util.string.StringUtil;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: dukangkang
 * @date: 2018/9/12 18:10.
 * @description: todo ...
 */
public class AlipayP2pActivity extends DqBaseActivity<RedPacketPresenter, DataBean> implements View.OnClickListener {

    private static final int SDK_PAY_FLAG = 1;
    private static final double MAX_AMOUNT = 200;
    private static final double MIN_AMOUNT = 0.01;

    private DecimalFormat mDecimalFormat = null;

    private String mAccount;
    private CommTitle mCommTitle = null;
    private EditText mMoneyEt = null;
    private EditText mNumEt = null;
    private EditText mBlessingEt = null;
    private LinearLayout mRechargeLlyt = null;
    private TextView mRechargeTv = null;
    private TextView mMoneyTv = null;
    private TextView mSendTv = null;
    private TextView mBotmTips = null;

    private boolean isBindCard = false;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /*
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        DqToast.showShort("支付宝红包发送成功");
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        DqToast.showShort("支付取消");
                    }
                    break;
            }
        }
    };

    @Override
    protected RedPacketPresenter createPresenter() {
        return new RedPacketPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.alipay_rd_p2p_activity);
    }

    @Override
    protected void init() {
        mAccount = getIntent().getExtras().getString(KeyValue.KEY_ACCOUNT);
    }

    @Override
    protected void initView() {
        mCommTitle = this.findViewById(R.id.alipay_rd_p2p_commtitle);
        mCommTitle.setRightVisible(View.VISIBLE);
        mCommTitle.setRightImageResource(R.mipmap.qc_alipay_rd_more);

        mMoneyEt = this.findViewById(R.id.alipay_rd_p2p_money_edittext);
        mRechargeLlyt = this.findViewById(R.id.alipay_rd_p2p_recharge_llyt);
        mRechargeTv = this.findViewById(R.id.alipay_rd_p2p_recharge);
        mNumEt = this.findViewById(R.id.alipay_rd_p2p_num_edittext);
        mBlessingEt = this.findViewById(R.id.alipay_rd_p2p_blessing_edittext);
        mMoneyTv = this.findViewById(R.id.alipay_rd_p2p_money);
        mSendTv = this.findViewById(R.id.alipay_rd_p2p_send);
        mBotmTips = this.findViewById(R.id.alipay_rd_p2p_bottom_tips);

        InputFilter[] filters = {new CashierInputFilter()};
        mMoneyEt.setFilters(filters); //设置金额输入的过滤器，保证只能输入金额类型
        mMoneyEt.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);

        mNumEt.setInputType(InputType.TYPE_CLASS_NUMBER);

        mDecimalFormat = new DecimalFormat("######0.00");
        mBotmTips.setText(getResources().getString(R.string.alipay_rd_p2p_bottom_tips));

    }


    @Override
    protected void initListener() {
        mCommTitle.getLeftIv().setOnClickListener(this);
        mCommTitle.getRightIv().setOnClickListener(this);
        mSendTv.setOnClickListener(this);
        mRechargeTv.setOnClickListener(this);
        mMoneyEt.addTextChangedListener(mTextWatcher);
        mRechargeLlyt.setOnClickListener(this);
        mBlessingEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        mCommTitle.setTitle("发支付宝红包");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mCommTitle.getLeftIvId()) {
            finish();
        } else if (id == mSendTv.getId()) {
            send();
        } else if (id == mRechargeTv.getId()) { // 支付宝不存在充值功能
            if (isBindCard) {
                // 充值
            } else {
                // 绑卡
            }
        } else if (id == mCommTitle.getRightIvId()) {
            DialogUtils.showBottomDialog(AlipayP2pActivity.this, KeyValue.FOUR, new DialogUtils.BottomDialogButtonListener() {
                @Override
                public void checkButton(int id) {
                    if(R.id.tv_message == id){
                        NavUtils.gotoAlipayPayListActivity(AlipayP2pActivity.this);
                    }
                }
            }).show();
        }
    }

    private void send() {
        if (Utils.isFastDoubleClick(500)) {
            return;
        }
        String money = mMoneyEt.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            return;
        }
        double moneyD = Double.parseDouble(money);
        if (moneyD < MIN_AMOUNT) {
            DqToast.showShort("金额不能小于" + MIN_AMOUNT + "元");
            return;
        }

        if (moneyD > MAX_AMOUNT) {
            DqToast.showShort("金额不能超过"+MAX_AMOUNT+"元");
            return;
        }


        String moneyTarget = mDecimalFormat.format(moneyD);
        String blessing = mBlessingEt.getText().toString();

        if (StringUtil.isEmpty(blessing)) {
            blessing = DqApp.getStringById(R.string.rd_comm_blessing_hints);
        }

        sendRedPacket(mAccount, moneyTarget, blessing);
    }

    private void sendRedPacket(String account, String moneyTarget, String blessing) {
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put("receive_uids", account);
        hashMap.put("amount", moneyTarget);
        hashMap.put("greetings", blessing);
        hashMap.put("type", "1");
        hashMap.put("service_version", "2");

        mPresenter.getGiveRedPacket(DqUrl.url_giveRedPacket, hashMap);
    }


    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
    }

    private AlipayEntity mAlipayEntity = null;

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (DqUrl.url_giveRedPacket.equals(url)) {
            if (code == 0) {
                mAlipayEntity = (AlipayEntity) entity.data;
                payV2(mAlipayEntity);
            }
        }

    }

    /**
     * 支付宝支付业务
     *
     */
    public void payV2(AlipayEntity redPayResp) {
        if (TextUtils.isEmpty(redPayResp.appid)) {
            DqToast.showShort("appid不能为空");
            return;
        }

        final String orderInfo = redPayResp.orderinfo;
        Runnable payRunnable = () -> {
            PayTask alipay = new PayTask(AlipayP2pActivity.this);
            Map<String, String> result = alipay.payV2(orderInfo, true);
            android.os.Message msg = new android.os.Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    void setEnable(boolean enable) {
        if (enable) {
            mSendTv.setBackgroundResource(R.drawable.qc_alipay_rd_send_enable_bg);

        } else {
            mSendTv.setBackgroundResource(R.drawable.qc_alipay_rd_btn_bg);
        }
        mSendTv.setEnabled(enable);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String money = mMoneyEt.getText().toString().trim();
            if (TextUtils.isEmpty(money)) {
                mMoneyTv.setText(getResources().getString(R.string.alipay_rd_money_hints));
                setEnable(false);
            } else {
                if (!String.valueOf(money.charAt(0)).equals(".")) {
                    String str = mDecimalFormat.format(Double.parseDouble(money));
                    mMoneyTv.setText(str);

                    double moneyD = Double.parseDouble(money);
                    if (moneyD > 0) {
                        setEnable(true);
                    } else {
                        setEnable(false);
                    }
                }
            }
        }
    };


}
