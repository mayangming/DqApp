package com.wd.daquan.mine.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;

public class AlipayRedDetailsHeaderHolder extends RecyclerView.ViewHolder{

    public ImageView avatar;
    public TextView name;
    public TextView blessing;
    public TextView money;
    public TextView moneyIns;
    public TextView ins;
    public TextView listTitle;

    public AlipayRedDetailsHeaderHolder(View view) {
        super(view);
        avatar = view.findViewById(R.id.itemAlipayRedDetailsHeaderAvatar);
        name = view.findViewById(R.id.itemAlipayRedDetailsHeaderName);
        blessing = view.findViewById(R.id.itemAlipayRedDetailsHeaderBlessing);
        money = view.findViewById(R.id.itemAlipayRedDetailsHeaderMoney);
        moneyIns = view.findViewById(R.id.itemAlipayRedDetailsHeaderMoneyIns);
        ins = view.findViewById(R.id.itemAlipayRedDetailsHeaderIns);
        listTitle = view.findViewById(R.id.itemAlipayRedDetailsHeaderListTitle);
    }

}
