package com.wd.daquan.mine.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wd.daquan.R;

public class AlipayRedListHolder extends RecyclerView.ViewHolder{

    public TextView time;
    public TextView name;
    public TextView money;
    public LinearLayout layout;

    public AlipayRedListHolder(View view) {
        super(view);
        layout = view.findViewById(R.id.itemRedPacketLayout);
        time = view.findViewById(R.id.itemRedPacketTxtTime);
        name = view.findViewById(R.id.itemRedPacketTxtName);
        money = view.findViewById(R.id.itemRedPacketTxtMoney);
    }

}
