package com.wd.daquan.chat.redpacket.pop;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wd.daquan.R;
import com.da.library.controls.pop.BasePopup;
import com.da.library.view.PwdInputView;
import com.da.library.view.custom.keyboard.KeyboardRecyclerView;
import com.da.library.view.custom.keyboard.KeybordModel;

/**
 * 支付密码支付
 * Created by Kind on 2019-05-21.
 */
public class PayPwdPop extends BasePopup<PayPwdPop> implements View.OnClickListener, KeyboardRecyclerView.OnKeyboardClickListener {

    //当前密码
    private String mCurrPwd = "";
    private OnPayPwdPopClickListener popClickListener;

    private LinearLayout popLayout;
    private TextView popTitle, popAmount, popPaymethodTypeContent;
    private PwdInputView popPwdView;
    private KeyboardRecyclerView popRecyclerview;

    public static PayPwdPop create(Context context, OnPayPwdPopClickListener popClickListener) {
        return new PayPwdPop(context, popClickListener);
    }

    private PayPwdPop(Context context, OnPayPwdPopClickListener popClickListener) {
        this.popClickListener = popClickListener;
        setContext(context);
    }

    @Override
    protected void initAttributes() {
        setContentView(R.layout.pay_pwd_pop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusAndOutsideEnable(true)
                .setBackgroundDimEnable(true)
                .setDimValue(0.5f);
    }

    @Override
    protected void initViews(View view, PayPwdPop popup) {
        popLayout = findViewById(R.id.pay_pwd_pop_layout);
        popTitle = findViewById(R.id.pay_pwd_pop_title);
        popAmount = findViewById(R.id.pay_pwd_pop_amount);

        findViewById(R.id.pay_pwd_pop_close).setOnClickListener(this);
        findViewById(R.id.pay_pwd_pop_paymethod_type).setOnClickListener(this);

        popPaymethodTypeContent = findViewById(R.id.pay_pwd_pop_paymethod_type_content);
        popPwdView = findViewById(R.id.pay_pwd_pop_pswview);
        popRecyclerview = findViewById(R.id.pay_pwd_pop_recyclerview);
        popPwdView.setCursorVisible(false);

        popRecyclerview.setOnKeyboardClickListener(this);
    }


    public void setData(String typeContent, String money) {
        popTitle.setText(typeContent);
        popAmount.setText(money);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_pwd_pop_close:
                this.dismiss();
                break;
            case R.id.pay_pwd_pop_paymethod_type:
                if (popClickListener != null) {
                    popClickListener.onPaySelectClick(v);
                }
                break;
        }
    }

    @Override
    public void onKeyboardItemClick(KeyboardRecyclerView.KeyBordTypeEnum typeEnum, BaseQuickAdapter adapter, View view, int position) {
        KeybordModel keybordModel = (KeybordModel) adapter.getItem(position);
        if (keybordModel == null) {
            return;
        }
        if (KeyboardRecyclerView.KeyBordTypeEnum.comm == typeEnum) {
            //非删除按钮
            mCurrPwd += keybordModel.key;
            popPwdView.setData(mCurrPwd);
            if (mCurrPwd.length() == popPwdView.getMaxCount()) {
                popClickListener.onPayPwdPopClick(mCurrPwd, true);
            } else {
                popClickListener.onPayPwdPopClick(mCurrPwd, false);
            }
        } else if (KeyboardRecyclerView.KeyBordTypeEnum.delete == typeEnum) {
            if (mCurrPwd.length() <= 0) {
                return;
            }
            String pwd = popPwdView.getPwd();
            mCurrPwd = pwd.substring(0, pwd.length() - 1);
            popPwdView.setData(mCurrPwd);
            popClickListener.onPayPwdPopClick(mCurrPwd, false);
        }
    }

    public interface OnPayPwdPopClickListener {
        void onPayPwdPopClick(String psw, boolean complete);

        void onPaySelectClick(View v);
    }
}