package com.wd.daquan.imui.adapter.viewholder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;


/**
 * 底部聊天布局更多的内容
 */
public class ChatBottomMoreViewHolder extends RecycleBaseViewHolder{
    public ImageView item_chat_bottom_icon;
    public TextView item_chat_bottom_title;

    public ChatBottomMoreViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void initView() {
        super.initView();
        item_chat_bottom_icon = itemView.findViewById(R.id.item_chat_bottom_icon);
        item_chat_bottom_title = itemView.findViewById(R.id.item_chat_bottom_title);
    }
}