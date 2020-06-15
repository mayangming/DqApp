package com.wd.daquan.chat.redpacket;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.chat.redpacket.pop.PayPwdPop;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.model.bean.Friend;
import com.da.library.utils.CommUtil;
import com.da.library.utils.TypeConvertUtil;
import com.da.library.widget.CommTitle;
import com.netease.nim.uikit.common.util.string.StringUtil;

import java.util.ArrayList;

/**
 * 群组发送红包
 * Created by Kind on 2019/5/15.
 */
public class RedPacketGroupSendAct extends DqBaseActivity<RedPacketPresenter, DataBean> {

    private String uid;
    private ArrayList<Friend> mSelectList = new ArrayList<>();
    private String cardMoney;

    private PayPwdPop payPwdPop;

    private CommTitle baseTitle;

    private EditText groupSendInAmount, groupSendNum;
    private TextView groupSendInKey, groupSendStateTitle, groupSendSwitchState;
    private RelativeLayout groupSendPeopleSelect;
    private TextView groupSendPeople, groupSendPeopleNum, groupSendMoney;
    private EditText groupSendGreet;

    @Override
    protected RedPacketPresenter createPresenter() {
        return new RedPacketPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.redpacket_group_send_act);
    }

    @Override
    protected void initView() {
        baseTitle = findViewById(R.id.base_title);
        setTitle();

        groupSendInKey = findViewById(R.id.redpacket_group_send_in_key);
        groupSendInAmount = findViewById(R.id.redpacket_group_send_in_amount);
        groupSendNum = findViewById(R.id.redpacket_group_send_num);
        groupSendStateTitle = findViewById(R.id.redpacket_group_send_state_title);

        groupSendSwitchState = findViewById(R.id.redpacket_group_send_switch_state);
        groupSendPeopleSelect = findViewById(R.id.redpacket_group_send_people_select);
        groupSendPeople = findViewById(R.id.redpacket_group_send_people);
        groupSendPeopleNum = findViewById(R.id.redpacket_group_send_peoplenum);
        groupSendGreet = findViewById(R.id.redpacket_group_send_greet);
        groupSendMoney = findViewById(R.id.redpacket_group_send_money);
    }

    /**
     * 标题
     */
    private void setTitle() {
        baseTitle.setTitleLayoutBackgroundColor(getResources().getColor(R.color.app_page_bg));
        baseTitle.setTitle(getString(R.string.redpacket_send_title));
        baseTitle.setTitleTextColor(getResources().getColor(R.color.color_252525));
        baseTitle.setLeftVisible(View.GONE);
        baseTitle.setLeftTxt(getString(R.string.comm_cancel), getResources().getColor(R.color.color_252525), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedPacketGroupSendAct.this.finish();
            }
        });
        baseTitle.setRightVisible(View.VISIBLE);
        baseTitle.setRightImageResource(R.mipmap.icon_redzhipy, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.redpacket_group_send_determine).setOnClickListener(this);
        groupSendPeopleSelect.setOnClickListener(this);
        groupSendSwitchState.setOnClickListener(this);

        groupSendInAmount.addTextChangedListener(watcher);
        groupSendNum.addTextChangedListener(watcher);

    }


    private TextWatcher watcher = new TextWatcher() {

        @Override

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String etmoney = groupSendInAmount.getText().toString().trim();
            String etnum = groupSendNum.getText().toString().trim();
            String etred = groupSendSwitchState.getText().toString().trim();
            if (TextUtils.isEmpty(etmoney) || "".equals(etmoney)) {
                groupSendMoney.setText(getResources().getString(R.string.redpacket_send_zero));
            } else {
                if (etred.equals("改为普通红包")) {
                    groupSendMoney.setText(CommUtil.dformat(etmoney));
                    groupSendPeopleNum.setVisibility(View.GONE);
                } else if (etred.equals("改为随机红包")) {
                    if (!StringUtil.isEmpty(etnum) && TypeConvertUtil.toDouble(etnum) > 0) {
                        double dou = Double.parseDouble(etmoney);
                        double dounum = Double.parseDouble(etnum);
                        double tmpMoney = dou * dounum;
                        groupSendPeopleNum.setVisibility(View.GONE);
                        groupSendMoney.setText(CommUtil.dformat(String.valueOf(tmpMoney)));

//                        if (tmpMoney <= TypeConvertUtil.toDouble(cardMoney)) {
//                            groupSendPeopleNum.setVisibility(View.GONE);
//                            groupSendMoney.setText(CommUtil.dformat(String.valueOf(tmpMoney)));
//                        } else {
//                            groupSendPeopleNum.setVisibility(View.VISIBLE);
//                            groupSendMoney.setText(getResources().getString(R.string.redpacket_send_zero));
//                        }
                    }
                }

            }

        }
    };

    @Override
    protected void initData() {
        uid = getIntent().getStringExtra(KeyValue.Transfer.UID);
        Friend friend = getIntent().getParcelableExtra(KeyValue.Transfer.FRIEND);
        if (friend != null) {
            mSelectList.add(friend);
        }

        if (mSelectList == null || mSelectList.size() <= 0) {
            groupSendPeople.setText("群内所有人");
            groupSendNum.setText("");
            groupSendPeopleNum.setVisibility(View.GONE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Friend tmpFriend : mSelectList) {
            sb.append(" ").append(tmpFriend.getName());
        }
        groupSendPeople.setText(sb.toString());
        groupSendNum.setText(String.valueOf(mSelectList.size()));
        groupSendPeopleNum.setText("已选择" + mSelectList.size() + "人");
        groupSendPeopleNum.setVisibility(View.VISIBLE);

        payPwdPop = PayPwdPop.create(this, new PayPwdPop.OnPayPwdPopClickListener() {
            @Override
            public void onPayPwdPopClick(String psw, boolean complete) {
                if (complete) {
                    payMoney(psw);
                }
            }

            @Override
            public void onPaySelectClick(View v) {

            }
        }).apply();
    }

    /**
     * 支付
     *
     * @param psw
     */
    private void payMoney(String psw) {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.redpacket_group_send_determine:
                payPwdPop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.redpacket_group_send_people_select:
                NavUtils.gotoSelectFriendActivity(this.getActivity(), uid, null, true);
                break;
            case R.id.redpacket_group_send_switch_state:

                String etmoney = groupSendInAmount.getText().toString().trim();
                String etnum = groupSendNum.getText().toString().trim();
                if (groupSendSwitchState.getText().toString().equals("改为随机红包")) {
                    if (!StringUtil.isEmpty(etmoney)) {
                        groupSendMoney.setText(CommUtil.dformat(etmoney));
                    }
                    groupSendStateTitle.setText("每人抽到的金额随机,");
                    groupSendSwitchState.setText("改为普通红包");
                    groupSendInKey.setText("总金额");
                } else {
                    if (!StringUtil.isEmpty(etmoney)) {
                        if (!StringUtil.isEmpty(etnum)) {
                            String yuan = Double.parseDouble(etmoney) * Double.parseDouble(etnum) + "";
                            groupSendMoney.setText(CommUtil.dformat(yuan));
                        }
                    }
                    groupSendStateTitle.setText("每人收到固定金额,");
                    groupSendSwitchState.setText("改为随机红包");
                    groupSendInKey.setText("单个金额");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        /**
         * 选择发送红包的人
         */
        if (requestCode == KeyValue.IntentCode.RESPONSE_CODE_SELECT_FRIEND) {
            ArrayList<Friend> mSelectList = data.getParcelableArrayListExtra(KeyValue.SelectFriend.KEY_SELECTED_LIST);
            boolean isAll = data.getBooleanExtra(KeyValue.SelectFriend.KEY_IS_ALL_MEMBER, false);

            if (isAll) {
                groupSendPeople.setText("群内所有人");
                groupSendNum.setText("");
                groupSendPeopleNum.setVisibility(View.GONE);

            } else {
                if (mSelectList == null || mSelectList.size() <= 0) {
                    groupSendPeople.setText("群内所有人");
                    groupSendNum.setText("");
                    groupSendPeopleNum.setVisibility(View.GONE);
                    return;
                }

                StringBuilder sb = new StringBuilder();
                for (Friend tmpFriend : mSelectList) {
                    sb.append(" ").append(tmpFriend.getName());
                }
                groupSendPeople.setText(sb.toString());
                groupSendNum.setText(String.valueOf(mSelectList.size()));
                groupSendPeopleNum.setText("已选择" + mSelectList.size() + "人");
                groupSendPeopleNum.setVisibility(View.VISIBLE);
            }

        }
    }
}
