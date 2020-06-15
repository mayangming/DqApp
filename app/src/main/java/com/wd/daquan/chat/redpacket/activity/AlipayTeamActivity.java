package com.wd.daquan.chat.redpacket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.da.library.tools.Utils;
import com.da.library.widget.CommTitle;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.chat.bean.AlipayEntity;
import com.wd.daquan.chat.redpacket.RedPacketPresenter;
import com.wd.daquan.chat.redpacket.pay.PayResult;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.constant.ResponseCode;
import com.wd.daquan.common.helper.DialogHelper;
import com.wd.daquan.common.utils.CashierInputFilter;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.log.DqToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: dukangkang
 * @date: 2018/9/12 18:11.
 * @description: todo ...
 */
public class AlipayTeamActivity extends DqBaseActivity<RedPacketPresenter, DataBean> implements View.OnClickListener {

    public static final String NORMAL_TYPE = "改为普通红包";
    public static final String RANDOM_TYPE = "改为随机红包";

    private static final double MAX_AMOUNT = 200;
    private static final double MIN_AMOUNT = 0.01;

    private static final int SDK_PAY_FLAG = 1;

    // 专属名称
    private String mExclusiveName = "";
    // 专属ID
    private String mExclusiveId = "";


    private DecimalFormat mDecimalFormat;

    private CommTitle mCommTitle = null;
    private TextView mRechargeTv = null;
    private EditText mTotalMoneyEt = null;
    private TextView mMoneyTips = null;
    private EditText mRedpacketNumEt = null;
    private LinearLayout mRechargeLlyt = null;
    private TextView mExchangeTv = null;
    private TextView mExchangeTitleTv = null;
    private RelativeLayout mSelectMemberRlyt = null;
    private TextView mSelectMember = null;
    private LinearLayout mGroupTipsLlyt = null;
    private TextView mGroupTips = null;
    private EditText mBlessingEt = null;
    private TextView mMoney = null;
    private TextView mSendTv = null;
    private TextView mBottomTips = null;

    private AlipayEntity mAlipayEntity = null;
    private StringBuilder mUserIdBuilder = new StringBuilder();

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
        setContentView(R.layout.alipay_rd_team_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        mCommTitle = this.findViewById(R.id.alipay_rd_team_commtitle);
        mCommTitle.setRightImageResource(R.mipmap.qc_alipay_rd_more);
        mCommTitle.setRightVisible(View.VISIBLE);

        mRechargeTv = this.findViewById(R.id.alipay_rd_team_recharge);
        mRechargeLlyt = this.findViewById(R.id.alipay_rd_team_recharge_llyt);
        mTotalMoneyEt = this.findViewById(R.id.alipay_rd_team_totalmoney_et);
        mMoneyTips = this.findViewById(R.id.alipay_rd_team_totalmoney_tips);
        mRedpacketNumEt = this.findViewById(R.id.alipay_rd_team_redpackt_num);
        mExchangeTv = this.findViewById(R.id.alipay_rd_team_exchange);
        mExchangeTitleTv = this.findViewById(R.id.alipay_rd_team_exchange_title);
        mSelectMemberRlyt = this.findViewById(R.id.alipay_rd_team_selectmember_rylt);
        mSelectMember = this.findViewById(R.id.alipay_rd_team_selectmember);
        mGroupTipsLlyt = this.findViewById(R.id.alipay_rd_team_grouptips_llyt);
        mGroupTips = this.findViewById(R.id.alipay_rd_team_groupnum_tips);

        mBlessingEt = this.findViewById(R.id.alipay_rd_team_blessing);
        mMoney = this.findViewById(R.id.alipay_rd_team_money);
        mSendTv = this.findViewById(R.id.alipay_rd_team_sendbtn);
        mBottomTips = this.findViewById(R.id.alipay_rd_team_bottom_tips);

        InputFilter[] filters = {new CashierInputFilter()};
        mTotalMoneyEt.setFilters(filters); //设置金额输入的过滤器，保证只能输入金额类型
        mTotalMoneyEt.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    protected void initListener() {
        mSendTv.setOnClickListener(this);
        mRechargeTv.setOnClickListener(this);
        mRechargeLlyt.setOnClickListener(this);

        mSelectMemberRlyt.setOnClickListener(this);
        mExchangeTv.setOnClickListener(this);
        mTotalMoneyEt.addTextChangedListener(mTextWatcher);
        mRedpacketNumEt.addTextChangedListener(mTextWatcher);
        mCommTitle.getLeftIv().setOnClickListener(this);
        mCommTitle.getRightIv().setOnClickListener(this);
    }

    private String mAccount;

    @Override
    protected void initData() {
        mDecimalFormat = new DecimalFormat("######0.00");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mAccount = bundle.getString(KeyValue.KEY_ACCOUNT);
            // 专属红包使用UserInfo
            mExclusiveName = bundle.getString(KeyValue.Exclusive.EXCLUSIVE_NAME);
            mExclusiveId = bundle.getString(KeyValue.Exclusive.EXCLUSIVE_ID);
        }

        if (!TextUtils.isEmpty(mExclusiveName)) {
            mUserIdBuilder.setLength(0);
            mUserIdBuilder.append(mExclusiveId);

            mSelectedList = new ArrayList<>();

            FriendDbHelper.getInstance().getFriend(mExclusiveId, friend -> {
                if (friend != null && !TextUtils.isEmpty(friend.getName())) {
                    mExclusiveName = friend.getName();
                    mSelectedList.add(friend);
                } else {
                    Friend tmpFriend = new Friend();
                    tmpFriend.uid = mExclusiveId;
                    tmpFriend.nickname = mExclusiveName;
                    mSelectedList.add(tmpFriend);
                }
            });

            mSelectMember.setText(mExclusiveName);
            mRedpacketNumEt.setText("" + mSelectedList.size());
            String qunNum = "已选择" + mSelectedList.size() + "人";
            mGroupTips.setText(qunNum);
            mGroupTipsLlyt.setVisibility(View.VISIBLE);
        }

        mCommTitle.setTitle("发支付宝红包");

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mCommTitle.getLeftIvId()) {
            finish();
        } else if (id == mSendTv.getId()) {
            send();
        } else if (id == mSelectMemberRlyt.getId()) {
//            NavUtils.gotoAddSelectFriend(this,targetId,sbUserId);
            NavUtils.gotoSelectFriendActivity(this, mAccount, mUserIdBuilder.toString(), false);
        } else if (id == mExchangeTv.getId()) {
            exchange();
        } else if (id == mCommTitle.getRightIvId()) {
            DialogUtils.showBottomDialog(AlipayTeamActivity.this, KeyValue.FOUR, new DialogUtils.BottomDialogButtonListener() {
                @Override
                public void checkButton(int id) {
                    if(R.id.tv_message == id){
                        NavUtils.gotoAlipayPayListActivity(AlipayTeamActivity.this);
                    }
                }
            }).show();
        }
    }

    private void exchange() {
        String money = mTotalMoneyEt.getText().toString().trim();
        String num = mRedpacketNumEt.getText().toString().trim();
        String exchange = mExchangeTv.getText().toString();

        if (RANDOM_TYPE.equals(exchange)) {
            if (!StringUtil.isEmpty(money)) {
                String moneyDouble = Utils.format(money);
                mMoney.setText(moneyDouble);
            }
            mExchangeTitleTv.setText("每人抽到的金额随机,");
            mExchangeTv.setText("改为普通红包");
            mMoneyTips.setText("总金额");
        } else {
            if (!StringUtil.isEmpty(money)) {
                if (!StringUtil.isEmpty(num)) {
                    String yuan = Double.parseDouble(money) * Double.parseDouble(num) + "";
                    String mon = Utils.format(yuan);
                    mMoney.setText(mon);
                }
            }
            mExchangeTitleTv.setText("每人收到固定金额,");
            mExchangeTv.setText("改为随机红包");
            mMoneyTips.setText("单个金额");
        }
    }

    private void send() {
        if (Utils.isFastDoubleClick(500)) {
            return;
        }
        String money = mTotalMoneyEt.getText().toString().trim();
        String num = mRedpacketNumEt.getText().toString().trim();
        String exchange = mExchangeTv.getText().toString().trim();
        if (NORMAL_TYPE.equals(exchange)) {
            if (TextUtils.isEmpty(num)) {
                DialogHelper.showDialog(this, "红包个数不能为空");
                return;
            }

            if (Double.parseDouble(num) <= 0) {
                DialogHelper.showDialog(this, "红包个数不能小于0个");
                return;
            }

            Double doumoney = Double.parseDouble(money);
            Double douetnum = Double.parseDouble(num);
            Double dou = doumoney / douetnum;
            if (dou < MIN_AMOUNT) {
                DialogHelper.showDialog(this, "单个红包不能小于" + MIN_AMOUNT + "元");
                return;
            }
            if (Double.parseDouble(num) > 100) {
                DialogHelper.showDialog(this, "个数不能超过100");
                return;
            }
            if (dou <= MAX_AMOUNT && dou >= MIN_AMOUNT) {
                createRedpacket();
            } else {
                DialogHelper.showDialog(this, "单个红包不能超过" + MAX_AMOUNT + "元");
            }
        } else if (RANDOM_TYPE.equals(exchange)) {
            if (TextUtils.isEmpty(num)) {
                DialogHelper.showDialog(this, "红包个数不能为空");
                return;
            }
            Double douNums = Double.parseDouble(num);
            Double douMoney = Double.parseDouble(money);
            if (douNums > 100) {
                DialogHelper.showDialog(this, "个数不能超过100");
                return;
            }
            if (douMoney > MAX_AMOUNT) {
                DialogHelper.showDialog(this, "单个红包不能超过" + MAX_AMOUNT + "元");
                return;
            }
            if (douNums == 0) {
                DialogHelper.showDialog(this, "红包个数不能小于0个");
                return;
            }
            if (douMoney == 0) {
                DialogHelper.showDialog(this, "单个红包不能小于0元");
                return;
            }
            if (douMoney <= MAX_AMOUNT && Double.parseDouble(num) <= 100) {
                createRedpacket();
            }
        } else {

        }

    }

    private List<Friend> mSelectedList = null;

    private void createRedpacket() {
        String money = mMoney.getText().toString().trim();
        String moneyFormat = Utils.format(money);
        String blessing = mBlessingEt.getText().toString();
        String num = mRedpacketNumEt.getText().toString().trim();

        if (StringUtil.isEmpty(blessing)) {
            blessing = DqApp.getStringById(R.string.rd_comm_blessing_hints);
        }

        String style;
        if (mExchangeTv.getText().toString().equals(RANDOM_TYPE)) {
            style = "2";
        } else {
            style = "1";
        }

        JSONArray listStr = new JSONArray();
        String redpacket_extra = "";//群id
        String exclusiveUids = "";
        if (mSelectedList == null || 0 > mSelectedList.size()) {
            exclusiveUids = "";
            redpacket_extra = "";
        } else {
            int size = mSelectedList.size();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < size; i++) {
                Friend friend = mSelectedList.get(i);
                if (friend == null) {
                    continue;
                }

                sb.append(friend.uid).append(",");

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("uid", friend.uid);
                    jsonObject.put("nickName", friend.nickname);
                    listStr.put(i, jsonObject);
                    redpacket_extra = listStr.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            exclusiveUids = sb.toString().substring(0, sb.toString().length() - 1);
        }

        sendRedPacket(mAccount, moneyFormat, blessing, "2", num, style, exclusiveUids, redpacket_extra);

    }

    /**
     * 发送群红包
     *
     * @param account
     * @param amount
     * @param blessing
     * @param type
     * @param num
     * @param style
     * @param receive_uids
     * @param redpacket_extra
     */
    private void sendRedPacket(String account, String amount, String blessing,
                               String type, String num, String style, String receive_uids, String redpacket_extra) {

        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put("group_id", account);
        hashMap.put("amount", amount);
        hashMap.put("greetings", blessing);
        hashMap.put("type", type);
        hashMap.put("num", num);
        hashMap.put("style", style);
        hashMap.put("receive_uids", receive_uids);
        hashMap.put("service_version", "2");
        hashMap.put("redpacket_extra", redpacket_extra);

        mPresenter.getGiveRedPacket(DqUrl.url_giveRedPacket, hashMap);
    }

    void setEnable(boolean enable) {
        if (enable) {
            mSendTv.setBackgroundResource(R.drawable.qc_alipay_rd_send_enable_bg);

        } else {
            mSendTv.setBackgroundResource(R.drawable.qc_alipay_rd_btn_bg);
        }
        mSendTv.setEnabled(enable);
    }


    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);

        if (DqUrl.url_giveRedPacket.equals(url)) {
            if (0 == code) {
                mAlipayEntity = (AlipayEntity) entity.data;
                payV2(mAlipayEntity);
            }
        }

    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        switch (code) {
            case ResponseCode.ALIAPY_AUTH:
                if (entity != null) {
                    DialogHelper.showDialogForAuth(getActivity(), entity.content);
                }
                break;
            case ResponseCode.EXPIRY_AUTH:
                if (entity != null) {
                    DqToast.showShort(DqApp.getStringById(R.string.expiry_auth));
                    // TODO: 2018/9/13  退出登录
                }
                break;
            default:
                if (entity != null) {
                    DqToast.showShort(entity.content);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            mSelectedList = data.getParcelableArrayListExtra(KeyValue.SelectFriend.KEY_SELECTED_LIST);
            boolean isAll = data.getBooleanExtra(KeyValue.SelectFriend.KEY_IS_ALL_MEMBER, false);
            if (mSelectedList == null || 0 >= mSelectedList.size()) {
                mSelectedList = null;
                mSelectMember.setText("群内所有人");
                mGroupTips.setText("群内所有人");
                mGroupTipsLlyt.setVisibility(View.GONE);
                mRedpacketNumEt.setText("");
                return;
            }

            StringBuffer buffer = new StringBuffer();
            if (mUserIdBuilder.length() != 0) {
                mUserIdBuilder.delete(0, mUserIdBuilder.length());
            }

            for (Friend friend : mSelectedList) {
                if (friend == null) {
                    continue;
                }
                mUserIdBuilder.append(" "+ friend.uid);
                buffer.append(" " + friend.nickname);

//                GroupUserInfo userInfos= RongUserInfoManager.getInstance().getGroupUserInfo(targetId,friend.getUserId());
//                String userId=" "+friend.getUserId();
//                friend.getName();
//                sbUserId.append(userId);
//                if (userInfos!=null){
//                    String userIn=" " + userInfos.getNickname();
//                    sb.append(userIn);
//                }else{
//                    String friendIn=" " + friend.getName();
//                    sb.append(friendIn);
//                }
            }


            if (isAll) {
                mSelectMember.setText("群内所有人");
                mGroupTipsLlyt.setVisibility(View.GONE);
                mRedpacketNumEt.setText("");
            } else {
                mSelectMember.setText(buffer.toString());
                mRedpacketNumEt.setText("" + mSelectedList.size());
                String qunNum = "已选择" + mSelectedList.size() + "人";
                mGroupTips.setText(qunNum);
                mGroupTipsLlyt.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * 支付宝支付业务
     */
    public void payV2(AlipayEntity redPayResp) {
        if (TextUtils.isEmpty(redPayResp.appid)) {
            DqToast.showShort("appid不能为空");
            return;
        }

        final String orderInfo = redPayResp.orderinfo;
        Runnable payRunnable = () -> {
            PayTask alipay = new PayTask(AlipayTeamActivity.this);
            Map<String, String> result = alipay.payV2(orderInfo, true);
            android.os.Message msg = new android.os.Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 正常红包
     */
    private void normalRedpacket() {
        String money = mTotalMoneyEt.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            setEnable(false);
            mMoney.setText(DqApp.getStringById(R.string.rd_comm_000_hints));
            return;
        }

        if (!String.valueOf(money.charAt(0)).equals(".")) {
            setEnable(true);
            String str = mDecimalFormat.format(Double.parseDouble(money));
            mMoney.setText(str);
        } else {
            setEnable(false);
        }
    }

    /**
     * 随机红包
     */
    private void randomRedpacket() {
        String money = mTotalMoneyEt.getText().toString().trim();
        String num = mRedpacketNumEt.getText().toString().trim();

        if (TextUtils.isEmpty(money)) {
            setEnable(false);
            return;
        }

        if (!String.valueOf(money.charAt(0)).equals(".")) {
            if (Double.parseDouble(money) != 0) {
                if (TextUtils.isEmpty(num)) {
                    return;
                }

                if (Double.parseDouble(num) != 0) {
                    double dou = Double.parseDouble(money);
                    double dounum = Double.parseDouble(num);
                    String str = mDecimalFormat.format(dou * dounum);
                    if (dou * dounum > 0) {
                        setEnable(true);
                        mMoney.setText(str);
                    } else {
                        setEnable(false);
                        mMoney.setText(DqApp.getStringById(R.string.rd_comm_000_hints));
                    }
                }
            }
        }
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
            String etred = mExchangeTv.getText().toString().trim();
            if (etred.equals(NORMAL_TYPE)) {
                normalRedpacket();
            } else if (etred.equals(RANDOM_TYPE)) {
                randomRedpacket();
            }
        }
    };


}
