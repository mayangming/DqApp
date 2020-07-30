package com.wd.daquan.mine.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;

public class AlipayRedDetailsHolder extends RecyclerView.ViewHolder{

    public ImageView avatar;
    public TextView name;
    public TextView money;
    public TextView time;

    public AlipayRedDetailsHolder(View view) {
        super(view);
        avatar = view.findViewById(R.id.itemAlipayRedDetailsAvatar);
        name = view.findViewById(R.id.itemAlipayRedDetailsName);
        money = view.findViewById(R.id.itemAlipayRedDetailsMoney);
        time = view.findViewById(R.id.itemAlipayRedDetailsTime);
    }

}
