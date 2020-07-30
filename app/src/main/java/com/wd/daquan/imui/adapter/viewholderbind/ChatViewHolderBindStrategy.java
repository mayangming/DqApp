package com.wd.daquan.imui.adapter.viewholderbind;


import androidx.lifecycle.LifecycleObserver;

import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.type.ImType;
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder;

/**
 * 聊天页面ViewHolder数据绑定的策略模式接口
 * 跟宿主页面有交互的话不可以根据holder写监听事件，可以使用RecycleItemOnClickListener接口操作集合里面的数据
 * 这里不允许操作Adapter中的数据
 * 使用泛型来确定子类RecycleBaseViewHolder的具体类型,这里确定子类具体类型是因为聊天布局会进行区分左、中、右布局，而左、中、右布局又有自己的公共数据处理，所以这里需要进行确定子类型
 */
public interface ChatViewHolderBindStrategy<T extends RecycleBaseViewHolder> {
//    public ChatViewHolderStrategy() {
//        StrategyContext.getInstance().addStrategy(getChatViewType(),getChatViewHolderClass());
//    }

    /**
     * 聊天页面ViewHolder的数据填充
     * @param chatType 聊天类型，单聊还是群聊，因为单聊和群聊的基本信息不同，以防万一，这里做区分，后面使用时候直接强转
     * @param imMessageBaseModel 填充的数据
     */
    LifecycleObserver bindViewHolder(T holder, ImMessageBaseModel imMessageBaseModel, ImType chatType);

//    abstract Class<? extends ChatViewHolderStrategy> getChatViewHolderClass();
//
//    abstract int getChatViewType();
}