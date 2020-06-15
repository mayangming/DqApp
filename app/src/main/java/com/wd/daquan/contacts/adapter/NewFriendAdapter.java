package com.wd.daquan.contacts.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wd.daquan.R;
import com.da.library.adapter.CommRecyclerViewAdapter;
import com.wd.daquan.model.bean.NewFriendBean;
import com.wd.daquan.contacts.holder.NewFriendHolder;
import com.wd.daquan.contacts.listener.INewFriendAdapterClickListener;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.utils.UIUtil;

import org.jetbrains.annotations.NotNull;

/**
 * @Author: 方志
 * @Time: 2018/9/14 11:33
 * @Description:
 */
public class NewFriendAdapter extends CommRecyclerViewAdapter<NewFriendBean, NewFriendHolder> {

    private INewFriendAdapterClickListener mListener;

    @Override
    protected NewFriendHolder onBindView(ViewGroup parent, int viewType) {
        return new NewFriendHolder(mInflater.inflate(R.layout.item_new_friend, parent, false));
    }

    @Override
    protected void onBindData(@NotNull NewFriendHolder holder, int position) {
        NewFriendBean item = allList.get(position);
        if (null == item) {
            return;
        }
        holder.name.setVisibility(View.VISIBLE);
        holder.agree.setVisibility(View.VISIBLE);

        holder.name.setText(item.nickname);
        holder.remark.setText(item.to_say);
        //（0:等待处理 1:同意 2:拒绝 3:忽略）
        if ("0".equals(item.response_status)) {
            setColor(holder.agree, true);
            holder.agree.setText("同意");
        } else {
            setColor(holder.agree, false);
            if ("1".equals(item.response_status)) {
                holder.agree.setText("已同意");
            } else if ("2".equals(item.response_status)) {
                holder.agree.setText("已拒绝");
            } else if ("3".equals(item.response_status)) {
                holder.agree.setText("已忽略");
            } else if ("4".equals(item.response_status)) {
                holder.agree.setText("已删除");
            }
        }

        GlideUtils.loadHeader(mContext, item.headpic, holder.portrait);

        if (item.isVip()){
            holder.headOutline.setVisibility(View.VISIBLE);
        }else {
            holder.headOutline.setVisibility(View.GONE);
        }

        holder.layout.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onItemClick(position, item);
            }
        });

        holder.layout.setOnLongClickListener(v -> {
            if (null != mListener) {
                mListener.onItemLongClick(position, item);
            }
            return true;
        });

        holder.agree.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onAgreeClick(position, item);
            }
        });
    }

    void setColor(TextView view, Boolean boo) {

        view.setTextColor(boo ? mContext.getResources().getColor(R.color.white)
                : mContext.getResources().getColor(R.color.text_title_black));
        if (boo) {
            view.setEnabled(true);
            view.setClickable(true);
            view.setBackgroundResource(R.drawable.comm_btn_4dp_corner_selector);
            view.setPadding(UIUtil.dip2px(6),0, UIUtil.dip2px(6),0);
        } else {
            view.setEnabled(false);
            view.setClickable(false);
            view.setBackgroundResource(R.color.white);
            view.setPadding(0,0,0,0);
        }

    }

    public void setListener(INewFriendAdapterClickListener mListener) {
        this.mListener = mListener;
    }
}
