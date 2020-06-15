package com.wd.daquan.imui.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.wd.daquan.R;
import com.wd.daquan.bean.ChatBottomMoreBean;
import com.wd.daquan.imui.adapter.viewholder.ChatBottomMoreViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 底部聊天更多的布局
 */
public class ChatBottomAdapter extends RecycleBaseAdapter<ChatBottomMoreViewHolder>{
    private List<ChatBottomMoreBean> chatBottomMoreBeans = new ArrayList<>();

    public ChatBottomAdapter() {
    }

    public ChatBottomAdapter(List<ChatBottomMoreBean> chatBottomMoreBeans) {
        this.chatBottomMoreBeans = chatBottomMoreBeans;
    }

    public void setChatBottomMoreBeans(List<ChatBottomMoreBean> chatBottomMoreBeans) {
        this.chatBottomMoreBeans = chatBottomMoreBeans;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return chatBottomMoreBeans.size();
    }

    @NonNull
    @Override
    public ChatBottomMoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        super.onCreateViewHolder(parent, viewType);
        View view = inflater.inflate(R.layout.item_chat_bottom_more,parent,false);
        return new ChatBottomMoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatBottomMoreViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ChatBottomMoreBean chatBottomMoreBean = chatBottomMoreBeans.get(position);
        int resId;
        if (-1 == chatBottomMoreBean.getResId()){
            resId = R.mipmap.ic_launcher;
        }else {
            resId = chatBottomMoreBean.getResId();
        }
        holder.item_chat_bottom_icon.setImageResource(resId);
        holder.item_chat_bottom_title.setText(chatBottomMoreBean.getTitle());
    }
}