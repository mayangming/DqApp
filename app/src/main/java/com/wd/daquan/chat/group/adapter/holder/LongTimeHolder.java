package com.wd.daquan.chat.group.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wd.daquan.R;

/**
 * @author: dukangkang
 * @date: 2019/1/20 16:16.
 * @description: todo ...
 */
public class LongTimeHolder extends RecyclerView.ViewHolder {
    public ImageView mHead;
    public TextView mName;
    public TextView mTime;
    public TextView mGreeting; // 红包祝福语
    public TextView mRepType; // 红包类型：支付宝／斗圈
    public ImageView mAlipayTag; // 支付宝icon标示
    public RelativeLayout mRpRlyt;

    public LongTimeHolder(View view) {
        super(view);
        this.mHead = view.findViewById(R.id.longtime_rp_item_head);
        this.mName = view.findViewById(R.id.longtime_rp_item_name);
        this.mTime = view.findViewById(R.id.longtime_rp_item_time);
        this.mGreeting = view.findViewById(R.id.longtime_rp_item_rp_greetings);
        this.mRepType = view.findViewById(R.id.longtime_rp_item_rp_type);
        this.mAlipayTag = view.findViewById(R.id.longtime_rp_item_rp_alipay);
        this.mRpRlyt = view.findViewById(R.id.longtime_rp_item_rp_rlyt);
    }


}
