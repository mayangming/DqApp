package com.wd.daquan.imui.adapter.viewholder;

import android.view.View;

/**
 * 聊天页面ViewHolder的策略模式
 */
public interface ChatViewHolderStrategy{
//    public ChatViewHolderStrategy() {
//        StrategyContext.getInstance().addStrategy(getChatViewType(),getChatViewHolderClass());
//    }

    /**
     * 生成ViewHolder
     */
    RecycleBaseViewHolder createViewHolder(View view);

//    abstract Class<? extends ChatViewHolderStrategy> getChatViewHolderClass();
//
//    abstract int getChatViewType();

}