package com.wd.daquan.chat.group.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;

public class ChatDetailsHolder extends RecyclerView.ViewHolder {

    public ImageView avatar;
    public TextView name;
    public ImageView manage;
    public View headOutline;

    public ChatDetailsHolder(View itemView) {
        super(itemView);
        avatar = itemView.findViewById(R.id.iv_avatar);
        name = itemView.findViewById(R.id.tv_username);
        manage = itemView.findViewById(R.id.iv_manage);
        headOutline = itemView.findViewById(R.id.item_single_details_head_outline);
    }
}
