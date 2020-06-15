package com.wd.daquan.chat.redpacket.transfer;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.da.library.tools.Utils;
import com.da.library.utils.CommUtil;
import com.da.library.widget.CommTitle;
import com.wd.daquan.R;
import com.wd.daquan.chat.redpacket.RedPacketPresenter;
import com.wd.daquan.chat.redpacket.pop.PayPwdPop;
import com.wd.daquan.chat.redpacket.pop.PayVerificationCodePop;
import com.wd.daquan.chat.redpacket.transfer.bean.Transfer;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.log.DqToast;

/**
 * 1对1转帐
 * Created by Kind on 2019-05-20.
 */
public class TransferP2PAct extends DqBaseActivity<RedPacketPresenter, DataBean> {

    private ImageView p2pAvater;
    private TextView p2pName;
    private EditText p2pMoney;
    private LinearLayout p2pAmountInsufficient;
    private TextView p2pRecharge;
    private EditText p2pDesc;

    private Dialog mDialog;
    private PayPwdPop payPwdPop;

    private String uid;

    private String inputMoney;//输入框的金额

    @Override
    protected RedPacketPresenter createPresenter() {
        return new RedPacketPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.transfer_p2p_act);
    }

    @Override
    protected void initView() {
        setTitle();

        p2pAvater = findViewById(R.id.transfer_p2p_avater);
        p2pName = findViewById(R.id.transfer_p2p_name);
        p2pMoney = findViewById(R.id.transfer_p2p_money);

        p2pAmountInsufficient = findViewById(R.id.transfer_p2p_amount_insufficient);
        p2pRecharge = findViewById(R.id.transfer_p2p_recharge);
        p2pDesc = findViewById(R.id.transfer_p2p_desc);

//        startActivity(new Intent(this, RedPacketDetailsAct.class));

        payPwdPop = PayPwdPop.create(this, new PayPwdPop.OnPayPwdPopClickListener() {
            @Override
            public void onPayPwdPopClick(String psw, boolean complete) {
                if (complete) {
//                    payMoney(psw);
                }
            }

            @Override
            public void onPaySelectClick(View v) {
                //银行卡切换，目前没有用到
            }
        }).apply();

    }

    /**
     * 标题
     */
    private void setTitle() {
        CommTitle baseTitle = findViewById(R.id.base_title);
        baseTitle.setTitle(getString(R.string.transfer_transfer));
        baseTitle.setLeftVisible(View.VISIBLE);
        baseTitle.getLeftIv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferP2PAct.this.finish();
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.transfer_p2p_gransfer).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        uid = getIntent().getStringExtra(KeyValue.Transfer.UID);

        Friend friend = FriendDbHelper.getInstance().getFriend(uid);
        p2pName.setText(friend.getName());
        GlideUtils.load(TransferP2PAct.this, friend.getHeadpic(), p2pAvater);
    }


//    /**
//     * 支付密码完成后
//     *
//     * @param psw
//     */
//    private void payMoney(String psw) {
//        String greetings = p2pDesc.getText().toString().trim();
//        if(TextUtils.isEmpty(greetings)){
//            greetings = ConfigManager.getInstance().getGreetings();
////            if (TextUtils.isEmpty(greetings)) {
////                greetings = ApplicationHelper.getStringById(R.string.red_greetings);
////            }
//        }
//
//        mPresenter.onTransfer(inputMoney, greetings, uid, psw);
//    }

    private boolean isMoneyEmpty(){
        String money = p2pMoney.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            inputMoney = "";
            DqToast.showShort("转账金额不能为空");
            return true;
        }
        inputMoney = CommUtil.dformat(money);
        return false;
    }

    int o = 0;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.transfer_p2p_gransfer:

//                startActivity(new Intent(this, TransferP2PReceiveAct.class));
//                showPayVerificationCodePop(null);
                if(0 == o){
                    return;
                }

                if (Utils.isFastDoubleClick(500)) {
                    return;
                }

                if(isMoneyEmpty()){
                    return;
                }

//                if (Double.parseDouble(str) >= mOneOneMinAmount) {
//                    showAtLocation(str);
//                } else {
//                    DialogUtil.showRedpocket(this, "金额不能低于" + mOneOneMinAmount + "元").show();
//                }
                showPayPsd(inputMoney);
                break;
//            case R.id.tvgorecharge:
//                if (bindcard == 0) {
//                    DialogUtil.showPurseDialog(this, getString(R.string.before_recharge), getString(R.string.cancel), getString(R.string.go_add), cardHoldname).show();
//                } else {
//                    NavUtils.gotoRecharge(this);
//                }
//                break;
        }
    }


    /**
     * 显示密码支付pop
     */
    private void showPayPsd(String money){
        payPwdPop.setData("转账", money);
        payPwdPop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        if (DqUrl.url_pay_thbhh_oeutu.equals(url)) {
            mDialog = DialogUtils.showPayError(this, entity.getContent(), bottomDialogListener);
            mDialog.setCancelable(false);
            mDialog.show();

        }
    }

    private DialogUtils.BottomDialogListener bottomDialogListener = new DialogUtils.BottomDialogListener() {
        @Override
        public void onClick(int type) {
            switch (type) {
                case R.id.tv_cancel://重试
                    showPayPsd(inputMoney);
                    break;
                case R.id.tv_confirm://忘记密码
//                    NavUtils.gotoForgetPay(mActivity);
                    break;
            }
        }
    };

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (DqUrl.url_pay_thbhh_oeutu.equals(url)) {
            Transfer transfer = (Transfer) entity.data;
            showPayVerificationCodePop(transfer);
        }
    }


    private void showPayVerificationCodePop(Transfer transfer){
        PayVerificationCodePop payVerCodePop = new PayVerificationCodePop(this, new PayVerificationCodePop.OnPayVerificationCodePop() {
            @Override
            public void onPayVerificationPopClick(int type, String psw, Transfer transfer) {

            }
        });
        payVerCodePop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        payVerCodePop.setData(PayVerificationCodePop.PAY_TYPE_TRANSFER_P2P, "转账", inputMoney, transfer);

    }

}
