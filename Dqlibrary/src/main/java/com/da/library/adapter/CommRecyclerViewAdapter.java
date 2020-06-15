package com.da.library.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.SectionIndexer;

import com.da.library.listener.IOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: 方志
 * Time:  15:39
 * Desc: RecyclerView加载普通列表数据适配器
 * Edit：
 */
@SuppressLint("NewApi")
public abstract class CommRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>
        implements SectionIndexer {

    /**
     * 所以人员数据
     */
    protected List<T> allList = new ArrayList<>();
    /**
     * 引用对象
     */
    protected Context mContext;
    protected LayoutInflater mInflater;
    private IOnItemClickListener<T> mListener;

    public IOnItemClickListener<T> getListener() {
        return mListener;
    }

    public ItemOnClickForView<T> itemOnClickForView;

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mInflater = LayoutInflater.from(parent.getContext());
        return onBindView(parent, viewType);
    }

    protected abstract VH onBindView(ViewGroup parent, int viewType);

    public T getItem(int position){
        return allList.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        onBindData(holder, position);
    }

    protected void onBindData(@NonNull VH holder, final int position){
        holder.itemView.setOnClickListener(v -> {
            if(mListener != null) {
                mListener.onItemClick(getItem(position), position);
            }
        });
    };

    /**
     * 初始化数据
     * @param data 所以人员
     */
    public void update(List<T> data){
        if(null != data) {
            allList.clear();
            allList.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void update(T data){
        if(null != data) {
            allList.clear();
            allList.add(data);
        }
        notifyDataSetChanged();
    }

    public void addLists(List<T> data){
        if(null != data) {
            allList.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        allList.clear();
    }


    public void add(T str) {
        allList.add(str);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        allList.remove(position);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return allList.size();
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return sectionIndex;
    }

    @Override
    public int getSectionForPosition(int position) {
        return position;
    }

    public void setListener(IOnItemClickListener<T> mListener) {
        this.mListener = mListener;
    }

    public void setItemOnClickForView(){

    }

    public void setItemOnClickForView(ItemOnClickForView<T> itemOnClickForView){
        this.itemOnClickForView = itemOnClickForView;
    }

    /**
     * 针对列表中的某一个控件的点击事件
     */
    public interface ItemOnClickForView<T>{
         void itemOnClickForView(int position,T t,@IdRes int viewId);
    }

}
