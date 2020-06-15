package com.wd.daquan.imui.adapter.viewholderbind;

import android.arch.lifecycle.LifecycleObserver;

import com.wd.daquan.imui.adapter.viewholder.BaseChatViewHolder;

/**
 * 基础数据填充类
 */
public abstract class BaseChatViewHolderBind<T extends BaseChatViewHolder> implements ChatViewHolderBindStrategy<T>, LifecycleObserver {
}