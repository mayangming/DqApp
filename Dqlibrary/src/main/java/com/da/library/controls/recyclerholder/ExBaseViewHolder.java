package com.da.library.controls.recyclerholder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;
import com.chad.library.adapter.base.BaseViewHolder;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 自定义基类
 * Created by Kind on 2019/4/4.
 */
public abstract class ExBaseViewHolder<T> extends BaseViewHolder {

    private View view;
    private WeakReference<Context> mWeakReference = null;

    public ExBaseViewHolder(View view) {
        super(view);
    }

    public ExBaseViewHolder(Context context, View view) {
        super(view);
        mWeakReference = new WeakReference<>(context);
        this.view = view;
    }

    /**
     * 单个
     * @param item
     */
    public abstract void onBindData(T item);

    /**
     * 列表
     * @param item
     */
    public void onBindData(List<T> item){

    }

    @SuppressLint("ResourceType")
    public View findViewById(@IdRes int id) {
        if (id < 0 || view == null) return null;
        return view.findViewById(id);
    }

    public Context getContext() {
        if (mWeakReference == null) {
            return null;
        }

        return mWeakReference.get();
    }
}
