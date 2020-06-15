package com.wd.daquan.common.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.wd.daquan.R;
import com.da.library.adapter.CommRecyclerViewAdapter;
import com.wd.daquan.chat.friend.FriendHolder;
import com.wd.daquan.common.bean.ShareItemBean;
import com.wd.daquan.common.listener.IShareAdapterClickListener;
import com.wd.daquan.glide.GlideUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @Author: 方志
 * @Time: 2018/9/21 10:48
 * @Description:
 */
public class ShareAdapter extends CommRecyclerViewAdapter<ShareItemBean, FriendHolder> {

    private IShareAdapterClickListener mListener;

    @Override
    protected FriendHolder onBindView(ViewGroup parent, int viewType) {
        return new FriendHolder(mInflater.inflate(R.layout.comm_adapter_item, parent, false));
    }

    @Override
    protected void onBindData(@NotNull FriendHolder holder, int position) {
        ShareItemBean item = getItem(position);
        if (null == item) return;
        holder.portrait.setVisibility(View.VISIBLE);
        holder.name.setVisibility(View.VISIBLE);
        holder.name.setText(item.sessionName);
        GlideUtils.loadHeader(mContext, item.sessionPortrait, holder.portrait);
        holder.itemView.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.OnItemClick(item);
            }
        });
    }

    public void setListener(IShareAdapterClickListener listener) {
        this.mListener = listener;
    }
}
