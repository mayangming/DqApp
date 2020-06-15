package com.wd.daquan.imui.adapter;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder;


/**
 * 基础的RecycleViewAdapter基类
 * 如果想要触发item点击事件需要重写RecycleBaseAdapter中的onBindViewHolder
 * @param <VH>
 */
public abstract class RecycleBaseAdapter<VH extends RecycleBaseViewHolder> extends RecyclerView.Adapter<VH>{
    protected Context context;
    protected LayoutInflater inflater;
    protected RecycleItemOnClickListener recycleItemOnClickListener;
    protected RecycleItemOnLongClickListener recycleItemOnLongClickListener;
    protected RecycleItemOnClickForChildViewListener recycleItemOnClickForChildViewListener;//子控件的点击事件监听
    public void setRecycleItemOnClickListener(RecycleItemOnClickListener recycleItemOnClickListener) {
        this.recycleItemOnClickListener = recycleItemOnClickListener;
    }

    public void setRecycleItemOnLongClickListener(RecycleItemOnLongClickListener recycleItemOnLongClickListener) {
        this.recycleItemOnLongClickListener = recycleItemOnLongClickListener;
    }

    public void setRecycleItemOnClickForChildViewListener(RecycleItemOnClickForChildViewListener recycleItemOnClickForChildViewListener) {
        this.recycleItemOnClickForChildViewListener = recycleItemOnClickForChildViewListener;
    }

    //注解主要是防止子类不调用父方法
    @CallSuper
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (null == context){
            context = parent.getContext();
        }
        if (null == inflater){
            inflater = LayoutInflater.from(context);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != recycleItemOnClickListener){
                    recycleItemOnClickListener.onItemClick(holder.itemView,position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != recycleItemOnLongClickListener){
                    return recycleItemOnLongClickListener.onItemLongClick(v,position);
                }
                return false;
            }
        });
//        holder.setRecycleItemOnClickForChildViewListener(recycleItemOnClickForChildViewListener);
    }
}