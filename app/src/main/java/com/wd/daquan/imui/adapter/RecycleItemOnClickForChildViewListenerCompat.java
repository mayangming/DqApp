package com.wd.daquan.imui.adapter;

import android.view.View;

import com.dq.im.model.ImMessageBaseModel;

/**
 * RecycleItemOnClickForChildViewListener的包装处理类
 */
public class RecycleItemOnClickForChildViewListenerCompat{
    static RecycleItemOnClickForChildViewListener recycleItemOnClickForChildViewListener;
    public static void setRecycleItemOnClickForChildViewListener(RecycleItemOnClickForChildViewListener recycleItemOnClickForChildViewListener){
        RecycleItemOnClickForChildViewListenerCompat.recycleItemOnClickForChildViewListener = recycleItemOnClickForChildViewListener;
    }

    public static RecycleItemOnClickForChildViewListener getRecycleItemOnClickForChildViewListener(){
        return recycleItemOnClickForChildViewListener;
    }
    public static class SimpleChildViewListener implements RecycleItemOnClickForChildViewListener{
        @Override
        public void onItemForChildClick(View view, int position) {

        }

        @Override
        public void onItemForChildClick(View view, ImMessageBaseModel imMessageBaseModel) {

        }
    }
}