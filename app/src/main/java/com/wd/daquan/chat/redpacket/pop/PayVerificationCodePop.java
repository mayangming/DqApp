package com.wd.daquan.chat.redpacket.pop;

import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.wd.daquan.R;
import com.wd.daquan.chat.redpacket.transfer.bean.Transfer;
import com.da.library.controls.pop.BasePopup;
import com.da.library.utils.CommUtil;
import com.da.library.view.PwdInputView;

/**
 * 支付验证码
 * Created by Kind on 2019-05-21.
 */
public class PayVerificationCodePop extends BasePopup<PayVerificationCodePop> {

    /**
     * 个人转帐
     */
    public static int PAY_TYPE_TRANSFER_P2P = 1;

    private OnPayVerificationCodePop popClickListener;

    private TextView popTitle, popAmount;
    private PwdInputView popPwdView;
    private Activity activity;

    private int type;
    private Transfer transfer;

    public static PayVerificationCodePop create(Activity activity, OnPayVerificationCodePop popClickListener) {
        return new PayVerificationCodePop(activity, popClickListener);
    }

    public PayVerificationCodePop(Activity activity, OnPayVerificationCodePop popClickListener) {
        this.activity = activity;
        this.popClickListener = popClickListener;
        setContext(activity);
    }

    @Override
    protected void initAttributes() {
        setContentView(R.layout.pay_verification_code_pop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusAndOutsideEnable(true)
                .setBackgroundDimEnable(true)
                .setDimValue(0.5f)
                .setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED)
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    protected void initViews(View view, PayVerificationCodePop popup) {
        popTitle = findViewById(R.id.pay_verification_code_pop_title);
        popAmount = findViewById(R.id.pay_verification_code_pop_amount);

        findViewById(R.id.pay_verification_code_pop_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayVerificationCodePop.this.dismiss();
            }
        });

        popPwdView = findViewById(R.id.pay_verification_code_pop_pswview);
        popPwdView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String pwd = popPwdView.getPwd();
                if (TextUtils.isEmpty(pwd)) {
                    return;
                }
                if (popPwdView.getMaxCount() <= pwd.length()) {
                    popClickListener.onPayVerificationPopClick(type, popPwdView.getPwd(), transfer);
                }
            }
        });
        CommUtil.showSoftKeyboard(popPwdView);
    }

    public void setData(int type, String typeContent, String money, Transfer transfer) {
        this.type = type;
        popTitle.setText(typeContent);
        popAmount.setText(money);
        this.transfer = transfer;
    }

    public interface OnPayVerificationCodePop {
        void onPayVerificationPopClick(int type, String psw, Transfer transfer);
    }

}