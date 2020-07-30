package com.wd.daquan.imui.adapter.viewholder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;


/**
 * 抽象的RecycleBaseViewHolder类
 * ViewHolder仅仅做为布局展示，不做为数据展示和处理，点击事件也不处理
 */
public abstract class RecycleBaseViewHolder extends RecyclerView.ViewHolder{
    public RecycleBaseViewHolder(@NonNull View itemView) {
        super(itemView);
        initView();
    }

    /**
     * 这里面数据会执行两次，当第一次执行时候，查找的控件是不存在的。第二次时候查找的控件才会存在
     */
    public void initView(){

    }
}