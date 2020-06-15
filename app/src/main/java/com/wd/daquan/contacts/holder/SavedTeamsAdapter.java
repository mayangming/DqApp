package com.wd.daquan.contacts.holder;

import android.view.View;
import android.view.ViewGroup;

import com.wd.daquan.R;
import com.da.library.adapter.CommRecyclerViewAdapter;
import com.wd.daquan.model.bean.TeamBean;
import com.wd.daquan.contacts.listener.IAdapterItemClickListener;
import com.wd.daquan.glide.GlideUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @Author: 方志
 * @Time: 2018/9/20 20:05
 * @Description:
 */
public class SavedTeamsAdapter extends CommRecyclerViewAdapter<TeamBean, GroupHolder> {

    private IAdapterItemClickListener mListener;

    @Override
    protected GroupHolder onBindView(ViewGroup parent, int viewType) {
        return new GroupHolder(mInflater.inflate(R.layout.comm_adapter_item, parent,false));
    }

    @Override
    protected void onBindData(@NotNull GroupHolder holder, int position) {
        TeamBean item = getItem(position);
        if(null == item) return;
        holder.name.setVisibility(View.VISIBLE);
        holder.name.setText(item.group_name);
        GlideUtils.loadHeader(mContext, item.group_pic, holder.portrait);

        holder.layout.setOnClickListener(v -> {
            if(null != mListener) {
                mListener.onItemClick(position);
            }
        });
    }

    public void setListener(IAdapterItemClickListener mListener) {
        this.mListener = mListener;
    }
}
