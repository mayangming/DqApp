package com.wd.daquan.imui.activity;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.da.library.utils.AESUtil;
import com.da.library.utils.BigDecimalUtils;
import com.da.library.widget.CommTitle;
import com.dq.im.bean.im.MessageRedPackageBean;
import com.dq.im.type.ImType;
import com.lzj.pass.dialog.PayPassDialog;
import com.lzj.pass.dialog.PayPassView;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.imui.presentter.RedPackagePresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.UserCloudWallet;
import com.wd.daquan.model.bean.WxPayBody;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;
import com.wd.daquan.third.helper.TeamHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 红包发送页面
 */
public class RedPackageSendActivity extends DqBaseActivity<RedPackagePresenter, DataBean> implements QCObserver {

    public static final String RED_PACKAGE_TYPE = "redPackageType";//红包类型
    public static final String RED_PACKAGE_DATA = "redPackageData";//红包数据

    private static String redPackageType = "1";//1,个人 2:群组

    private EditText amountEdt;
    private EditText amountCountEdt;
    private EditText back;//备注
    private TextView moneyShow;//输入金额展示
    private TextView sendRedPackageBtn;
    private View red_package_count_rl;
    private TextView userRedPackageType;//用户红包类型Logo
    private TextView red_package_money;//用户红包类型名称
    private TextView group_member_count;//群成员数量
    private String account = "";//账号
    private int memberCountInt = 0;//群成员数量
    private CommTitle commTitle;
    private MessageRedPackageBean messageRedPackageBean = new MessageRedPackageBean();
    private UserCloudWallet userCloudWallet = new UserCloudWallet();
    @Override
    protected RedPackagePresenter createPresenter() {
        return new RedPackagePresenter();
    }
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_red_package_send);
    }
    public void initView(){
        red_package_count_rl = findViewById(R.id.red_package_count_rl);
        group_member_count = findViewById(R.id.group_member_count);
        userRedPackageType = findViewById(R.id.user_package_type);
        red_package_money = findViewById(R.id.red_package_money);
        commTitle = findViewById(R.id.red_package_send_title);
        moneyShow = findViewById(R.id.red_package_money_show);
        amountEdt = findViewById(R.id.red_package_money_input);
        amountCountEdt = findViewById(R.id.red_package_amount_count);
        sendRedPackageBtn = findViewById(R.id.send_red_package);
        back = findViewById(R.id.red_package_money_back);
        sendRedPackageBtn.setOnClickListener(this::onClick);
        amountEdt.setFilters(new InputFilter[]{new CashierInputFilter()});
        amountEdt.addTextChangedListener(new MyTextWatch());
    }

    public void initData(){
        MsgMgr.getInstance().attach(this);
        redPackageType = getIntent().getStringExtra(RED_PACKAGE_TYPE);
        account = getIntent().getStringExtra(RED_PACKAGE_DATA);
        if (ImType.P2P.getValue().equals(redPackageType)){
            red_package_count_rl.setVisibility(View.GONE);
            userRedPackageType.setVisibility(View.GONE);
            red_package_money.setText("单个金额");
            group_member_count.setVisibility(View.GONE);
        }else {
            group_member_count.setVisibility(View.VISIBLE);
            red_package_count_rl.setVisibility(View.VISIBLE);
            userRedPackageType.setVisibility(View.VISIBLE);
            red_package_money.setText("总金额");
            Log.e("YM","群Id:"+account);
            String memberCount = TeamHelper.getMemberCount(account);
            memberCountInt = Integer.valueOf(memberCount);
            group_member_count.setText("本群共"+memberCount+"人");
        }

        messageRedPackageBean.setType(redPackageType);
        mPresenter.getUserCloudWallet(DqUrl.url_user_cloud_wallet,new HashMap<>());
        commTitle.setTitle("发红包");
        commTitle.getLeftIv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 更新UI
     * @param userCloudWallet
     */
    private void upDataUI(UserCloudWallet userCloudWallet){
        String accumulate = "红包余额 %s元";
        String balanceValue = BigDecimalUtils.penny2Dollar(userCloudWallet.getBalance()).toPlainString();
    }
    public void onClick(View view){
        Log.e("YM","点击事件");
        switch (view.getId()){
            case R.id.send_red_package:
                String amountStr = amountEdt.getText().toString();
                if (TextUtils.isEmpty(amountStr)){
                    Toast.makeText(view.getContext(),"金额不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                String description = back.getText().toString();
                if (TextUtils.isEmpty(description)){
                    description = "恭喜发财，大吉大利";
                }
                messageRedPackageBean.setDescription(description);
                double amount = Double.parseDouble(amountStr);
                messageRedPackageBean.setMoney(String.valueOf(amount));
                messageRedPackageBean.setCount("1");
                if (ImType.Team.getValue().equals(redPackageType)){
                    String amountCountStr = amountCountEdt.getText().toString();
                    if (TextUtils.isEmpty(amountCountStr)){
                        Toast.makeText(view.getContext(),"数量不能为空",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int amountCount = Integer.parseInt(amountCountStr);
                    Log.e("YM","红包数量:"+amountCount);
                    messageRedPackageBean.setCount(String.valueOf(amountCount));
                    if (memberCountInt < amountCount){
                        Toast.makeText(view.getContext(),"红包数量不能大于群组成员数量",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (amountCount <= 0){
                        Toast.makeText(view.getContext(),"红包数量不能小于0",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Log.e("YM","零钱余额:"+userCloudWallet.getBalance());
                Log.e("YM","红包金额(元):"+amount);
                if (amount < 1){
                    Toast.makeText(view.getContext(),"红包金额不能小于0.01元",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (amount * 100 < userCloudWallet.getBalance()){//当支付金额小于零钱余额时候，直接发送出去，后台收到这条消息时候会直接根据红包消息去扣除零钱余额
                    messageRedPackageBean.setRedType(MessageRedPackageBean.RED_PACKAGE_CHANGE);
//                    MsgMgr.getInstance().sendMsg(MsgType.CHAT_RED_PACKAGE, messageRedPackageBean);
//                    finish();
                    showPayPwdPop();
                }else {
                    messageRedPackageBean.setRedType(MessageRedPackageBean.RED_PACKAGE_WX);
                    Map<String,String> params = new HashMap<>();
                    params.put("userId", ModuleMgr.getCenterMgr().getUID());
                    params.put("money",messageRedPackageBean.getMoney());
                    params.put("count",messageRedPackageBean.getCount());
                    params.put("couponPayType",messageRedPackageBean.getRedType()+"");
                    params.put("description",messageRedPackageBean.getDescription());
                    params.put("pwd","");
                    Set<String> ketSet = params.keySet();
                    for (String key : ketSet){
                        String value = params.get(key);
                        DqLog.e("YM--红包->key:"+key+"--->value:"+value);
                    }
                    mPresenter.getWeChatPayOrderInfo(DqUrl.url_pay_wechat_order_red_package,params);

                }
//                getIntent().putExtra(RED_PACKAGE_DATA,messageRedPackageBean);

//                setResult(Activity.RESULT_OK,getIntent());

                break;
        }
    }
    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (DqUrl.url_user_cloud_wallet.equals(url)){
            userCloudWallet = (UserCloudWallet)entity.data;
            Log.e("YM","获取的零钱信息:"+userCloudWallet.toString());
            upDataUI(userCloudWallet);
        }else if (DqUrl.url_pay_wechat_order_red_package.equals(url)){
            WxPayBody wxPayBody = (WxPayBody) entity.data;
            messageRedPackageBean.setCouponId(wxPayBody.getCouponId());
            if (MessageRedPackageBean.RED_PACKAGE_CHANGE == messageRedPackageBean.getRedType()){
                MsgMgr.getInstance().sendMsg(MsgType.CHAT_RED_PACKAGE, messageRedPackageBean);
                finish();
            }else {
                mPresenter.startWeChatPay(this,wxPayBody);
            }
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        if (DqUrl.url_user_cloud_wallet.equals(url)){
            if (entity.result != 16001){
                DqToast.showShort("网络异常~");
            }else {
                DqToast.showShort(entity.msg);
            }
//            updateData(null);
        }else if (DqUrl.url_pay_wechat_order_red_package.equals(url)){
            if (entity.result == 16001){
                DqToast.showShort("网络异常~");
            }else {
                DqToast.showShort(entity.content);
            }
        }
    }
    @Override
    public void onMessage(String key, Object value) {
        if (MsgType.RED_PACKAGE_PAY.equals(key)){//红包支付完成
//            mPresenter.requestUserInfo(DqUrl.url_oauth_useWeixinInfo,new HashMap<>());
            MsgMgr.getInstance().sendMsg(MsgType.CHAT_RED_PACKAGE, messageRedPackageBean);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MsgMgr.getInstance().detach(this);
    }

    /**
     * Created by Jackie on 2016/1/30.
     * 过滤用户输入只能为金额格式
     */
    public class CashierInputFilter implements InputFilter {
        Pattern mPattern;
        //输入的最大金额
        private static final int MAX_VALUE = Integer.MAX_VALUE;
        //小数点后的位数
        private static final int POINTER_LENGTH = 2;
        private static final String POINTER = ".";
        private static final String ZERO = "0";
        public CashierInputFilter() {
            mPattern = Pattern.compile("([0-9]|\\.)*");
        }
        /**
         * @param source  新输入的字符串
         * @param start   新输入的字符串起始下标，一般为0
         * @param end    新输入的字符串终点下标，一般为source长度-1
         * @param dest   输入之前文本框内容
         * @param dstart  原内容起始坐标，一般为0
         * @param dend   原内容终点坐标，一般为dest长度-1
         * @return     输入内容
         */
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String sourceText = source.toString();
            String destText = dest.toString();
            //验证删除等按键
            if (TextUtils.isEmpty(sourceText)) {
                return "";
            }

            Matcher matcher = mPattern.matcher(source);
            //已经输入小数点的情况下，只能输入数字
            if(destText.contains(POINTER)) {
                if (!matcher.matches()) {
                    return "";
                } else {
                    if (POINTER.equals(source)) { //只能输入一个小数点
                        return "";
                    }
                }
                //验证小数点精度，保证小数点后只能输入两位
                int index = destText.indexOf(POINTER);
                int length = dend - index;
                if (length > POINTER_LENGTH) {
                    return dest.subSequence(dstart, dend);
                }
            }else {
                Log.e("YM","已经存在的内容sourceText:"+sourceText);
                Log.e("YM","已经存在的内容destText:"+destText);
                if (destText.equals("0")) { //如果第一个数字为0，第二个不为点，就不允许输入
                    if (!sourceText.equals(".")) {//第二个不为点时候返回空
                        return "";
                    }
                }
                //没有输入小数点的情况下，只能输入小数点和数字，但首位不能输入小数点
                if (!matcher.matches()) {
                    return "";
                } else {
                    if ((POINTER.equals(source)) && TextUtils.isEmpty(destText)) {
                        return "";
                    }
                }
            }

            //验证输入金额的大小
            double sumText = Double.parseDouble(destText + sourceText);
            if (sumText > MAX_VALUE) {
                return dest.subSequence(dstart, dend);
            }
            return dest.subSequence(dstart, dend) + sourceText;
        }
    }

    private void showPayPwdPop(){
        Log.e("YM","零钱支付");
        final PayPassDialog dialog=new PayPassDialog(this);
        dialog.getPayViewPass()
                .setPayClickListener(new PayPassView.OnPayClickListener() {
                    @Override
                    public void onPassFinish(String passContent) {
                        //6位输入完成回调
//                        try {
//                            String newPwd = AESUtil.encrypt(passContent);
//                            Map<String,String> params = new HashMap<>();
//                            params.put("spbillCreateIp", NetToolsUtil.getLocalHostIp());
//                            params.put("pwd",newPwd);
//                            params.put("amount",bigDecimal.toPlainString());
//                            mPresenter.getUserCashWithdrawal(DqUrl.url_user_cash_withdrawal,params);
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
                        String pwd = "";
                        try{
                            pwd = AESUtil.encrypt(passContent);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Map<String,String> params = new HashMap<>();
                        params.put("userId", ModuleMgr.getCenterMgr().getUID());
                        params.put("money",messageRedPackageBean.getMoney());
                        params.put("count",messageRedPackageBean.getCount());
                        params.put("couponPayType",messageRedPackageBean.getRedType()+"");
                        params.put("description",messageRedPackageBean.getDescription());
                        params.put("pwd",pwd);
                        Set<String> ketSet = params.keySet();
                        for (String key : ketSet){
                            String value = params.get(key);
                            DqLog.e("YM--红包->key:"+key+"--->value:"+value);
                        }
                        mPresenter.getWeChatPayOrderInfo(DqUrl.url_pay_wechat_order_red_package,params);
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

    class MyTextWatch implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            moneyShow.setText("￥"+s.toString());
            if (TextUtils.isEmpty(s.toString())){
                sendRedPackageBtn.setBackgroundResource(R.drawable.red_package_btn_bg_efefef);
                sendRedPackageBtn.setEnabled(false);
            }else {
                sendRedPackageBtn.setBackgroundResource(R.drawable.red_package_btn_bg_f8f8f8);
                sendRedPackageBtn.setEnabled(true);
            }
        }
    }
}