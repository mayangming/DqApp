package com.wd.daquan.chat.redpacket.details;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.model.bean.Friend;
import com.da.library.controls.recyclerholder.ExBaseViewHolder;

/**
 * 红包详情
 * Created by Kind on 2019-05-21.
 */
public class RedPacketDetailsHolder extends ExBaseViewHolder<Friend> {

    private ImageView itemAvater;
    private TextView itemName, itemAmount, itemTime, itemBest;

    public RedPacketDetailsHolder(Context context, View view) {
        super(context, view);
        itemAvater = (ImageView) findViewById(R.id.redpacket_details_item_avater);
        itemName = (TextView) findViewById(R.id.redpacket_details_item_name);
        itemAmount = (TextView) findViewById(R.id.redpacket_details_item_amount);
        itemTime = (TextView) findViewById(R.id.redpacket_details_item_time);
        itemBest = (TextView) findViewById(R.id.redpacket_details_item_best);
    }

    @Override
    public void onBindData(Friend item) {

    }
}
