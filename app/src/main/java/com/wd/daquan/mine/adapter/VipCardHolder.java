package com.wd.daquan.mine.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wd.daquan.R;

public class VipCardHolder extends RecyclerView.ViewHolder{
    public TextView vipCardTitle;
    public TextView vipCardPrice;
    public TextView vipCardRemark;
    public VipCardHolder(View itemView) {
        super(itemView);
        vipCardTitle = itemView.findViewById(R.id.vip_card_title);
        vipCardPrice = itemView.findViewById(R.id.vip_card_price);
        vipCardRemark = itemView.findViewById(R.id.vip_card_remark);
    }
}