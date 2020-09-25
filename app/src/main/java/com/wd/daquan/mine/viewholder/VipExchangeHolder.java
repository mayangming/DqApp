package com.wd.daquan.mine.viewholder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wd.daquan.R;

public class VipExchangeHolder extends RecyclerView.ViewHolder{
    public TextView vipExchangeTitle;
    public TextView vipExchangeCount;
    public TextView vipExchangeBtn;
    public VipExchangeHolder(View itemView) {
        super(itemView);
        vipExchangeTitle = itemView.findViewById(R.id.item_vip_exchange_title);
        vipExchangeCount = itemView.findViewById(R.id.item_vip_exchange_count);
        vipExchangeBtn = itemView.findViewById(R.id.item_vip_exchange_btn);
    }
}