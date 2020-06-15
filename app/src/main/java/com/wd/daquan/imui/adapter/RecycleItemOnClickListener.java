package com.wd.daquan.imui.adapter;

import android.view.View;

/**
 * RecycleView的列表
 * 如果想要触发item点击事件需要重写RecycleBaseAdapter中的onBindViewHolder
 */
public interface RecycleItemOnClickListener {
    void onItemClick(View view, int position);
}