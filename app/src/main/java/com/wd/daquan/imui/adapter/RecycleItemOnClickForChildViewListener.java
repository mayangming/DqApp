package com.wd.daquan.imui.adapter;

import android.view.View;

import com.dq.im.model.ImMessageBaseModel;

/**
 * RecycleView中item的子控件的点击事件，可以通过该接口精确锁定是item中哪个控件触发的点击事件
 */
public interface RecycleItemOnClickForChildViewListener {
    /**
     * @param view 子控件View,可以通过Tag或者id来进行数据操作
     * @param position 该Item所在的position位置
     */
    void onItemForChildClick(View view, int position);

    /**
     * @param view 子控件View,可以通过Tag或者id来进行数据操作
     * @param position 该Item所在的position位置
     */
    void onItemForChildClick(View view, ImMessageBaseModel imMessageBaseModel);
}