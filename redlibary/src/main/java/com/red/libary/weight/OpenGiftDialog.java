package com.red.libary.weight;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.red.libary.R;

/**
 * 打开礼物的对话框
 */
public class OpenGiftDialog extends AppCompatDialogFragment{
    public final static String ACTION_TYPE = "type";//哪种类型，用于区分显示什么内容
    public final static int NO_LUCK = 0;//没有中奖
    public final static int LUCK = 1;//中奖
    public final static String ACTION_AMOUNT = "amount";//金额内容
    private String amount = "";//金额
    private int type = 0;
    private TextView giftAmount;
    private TextView giftTip;
    private View giftNext;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//需要这一行来解决对话框背景有白色的问题(颜色随主题变动)
        return inflater.inflate(R.layout.open_gift_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        giftAmount = view.findViewById(R.id.gift_amount);
        giftTip  = view.findViewById(R.id.gift_tip);
        giftNext = view.findViewById(R.id.gift_next);
        Bundle bundle = getArguments();
        if (null != bundle){
            amount = bundle.getString(ACTION_AMOUNT,"0");
            type = bundle.getInt(ACTION_TYPE,NO_LUCK);
        }
        if (NO_LUCK == type){
            giftAmount.setText("0");
            giftTip.setText("您没有中奖~");
        }else if (LUCK == type){
            giftAmount.setText(amount);
            giftTip.setText("已存入斗圈零钱红包");
        }

        giftNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}