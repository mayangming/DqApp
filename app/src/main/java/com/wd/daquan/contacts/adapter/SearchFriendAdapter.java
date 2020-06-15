package com.wd.daquan.contacts.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.da.library.adapter.CommRecyclerViewAdapter;
import com.wd.daquan.R;
import com.wd.daquan.contacts.holder.SearchFriendHolder;
import com.wd.daquan.contacts.listener.ISearchFriendAdapterItemClickListener;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.bean.Friend;

import org.jetbrains.annotations.NotNull;

/**
 * @Author: 方志
 * @Time: 2018/9/26 16:58
 * @Description:
 */
public class SearchFriendAdapter extends CommRecyclerViewAdapter<Friend, SearchFriendHolder> {

    private ISearchFriendAdapterItemClickListener mListener;

    @Override
    protected SearchFriendHolder onBindView(ViewGroup parent, int viewType) {
        return new SearchFriendHolder(mInflater.inflate(R.layout.comm_adapter_item, parent, false));
    }

    @Override
    protected void onBindData(@NotNull SearchFriendHolder holder, int position) {

        Friend friend = getItem(position);
        if (null == friend) return;
        holder.nameLl.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(friend.friend_remarks)) {
            holder.friendRemark.setText(friend.nickname);
            holder.nickname.setVisibility(View.GONE);
        } else {
            holder.friendRemark.setText(friend.friend_remarks);
            holder.nickname.setText(friend.nickname);
            holder.nickname.setVisibility(View.VISIBLE);
        }

        GlideUtils.loadHeader(mContext, friend.headpic, holder.portrait);

        holder.itemView.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onItemClick(friend);
            }
        });
    }

    public void setListener(ISearchFriendAdapterItemClickListener listener) {
        this.mListener = listener;
    }
}
