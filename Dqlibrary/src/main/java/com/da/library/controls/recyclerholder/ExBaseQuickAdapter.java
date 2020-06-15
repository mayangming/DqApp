package com.da.library.controls.recyclerholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Kind on 2019/4/15.
 */
public abstract class ExBaseQuickAdapter<T, VH extends BaseViewHolder> extends BaseQuickAdapter<T, VH> {

    private WeakReference<Context> mWeakReference = null;

    public ExBaseQuickAdapter(List<T> data) {
        super(data);
    }

    public ExBaseQuickAdapter(Context context, List<T> data) {
        this(data);
        mWeakReference = new WeakReference<>(context);
    }

    public View inflate(ViewGroup parent, int resource) {
        return this.inflate(parent, resource, false);
    }

    public View inflate(ViewGroup parent, int resource, boolean attachToRoot) {
        return LayoutInflater.from(parent.getContext()).inflate(resource, parent, attachToRoot);
    }

    public Context getContext() {
        if (mWeakReference == null) {
            return null;
        }

        return mWeakReference.get();
    }

    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position);
    }
}