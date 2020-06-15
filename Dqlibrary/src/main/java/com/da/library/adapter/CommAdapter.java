package com.da.library.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

import com.da.library.holder.CommHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: dukang
 * @date: 2018/4/18 18:00.
 * @description: 公共适配器
 * @Edit：2018/4/28 方志， 加上holder泛型，避免子类强转, 并把父布局传递回去，初始化布局加上父布局
 */
public abstract class CommAdapter<T, VH extends CommHolder> extends BaseAdapter implements SectionIndexer {

    protected Context mContext = null;

    protected List<T> mList = new ArrayList<>();

    public CommAdapter(Context context) {
        this.mContext = context;
    }

    public void replace(List<T> list) {
        mList = list;
        notifyDataSetChanged();

    }
    public void addList(List<T> list){
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null != mList) {
            return mList.size();
        }
        return 0;
    }

    public List<T> getmList() {
        return mList;
    }

    @Override
    public T getItem(int position) {
        if (null == mList) {
            return null;
        }
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH commHolder = null;
        if (null == convertView) {
            commHolder = onCreateViewHolder(convertView, parent, position);
            convertView = commHolder.getView();
        } else {
            commHolder = (VH) convertView.getTag();
        }

        onBindViewHolder(commHolder, position);
        return convertView;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int section) {
        return section;
    }

    @Override
    public int getSectionForPosition(int position) {
        return position;
    }

    public abstract VH onCreateViewHolder(View convertView, ViewGroup parent, int position);

    public abstract void onBindViewHolder(VH holder, int position);

}
