package com.wd.daquan.mine.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import com.wd.daquan.R;
import com.da.library.adapter.CommRecyclerViewAdapter;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.mine.viewholder.BlackListHolder;
import com.wd.daquan.model.bean.Friend;

import org.jetbrains.annotations.NotNull;

/**
 * Created by DELL on 2018/9/13.
 */

public class BlackListAdapter extends CommRecyclerViewAdapter<Friend, RecyclerView.ViewHolder> {
    private OnBlackListListener mOnBlackListListener;

    @Override
    protected RecyclerView.ViewHolder onBindView(ViewGroup parent, int viewType) {
        return new BlackListHolder(mInflater.inflate(R.layout.item_black_list, parent, false));
    }

    @Override
    protected void onBindData(@NotNull RecyclerView.ViewHolder holder, int position) {
        Friend data = allList.get(position);
        if(data == null) {
            return;
        }
        BlackListHolder blackListHolder = (BlackListHolder) holder;
        String userName = data.getName();
        blackListHolder.name.setText(userName);
        GlideUtils.load(mContext, data.headpic, blackListHolder.mAvatar);
        blackListHolder.btn_remove.setOnClickListener(v -> {
            if(mOnBlackListListener != null) {
                mOnBlackListListener.onRemove(data);
            }
        });
        blackListHolder.layout.setOnClickListener(v -> {
            if(mOnBlackListListener != null) {
                mOnBlackListListener.onLayout(data);
            }
        });
    }

    public void setOnBlackListListener(OnBlackListListener mOnBlackListListener){
        this.mOnBlackListListener = mOnBlackListListener;
    }
    public interface OnBlackListListener{
        void onLayout(Friend data);
        void onRemove(Friend data);
    }
}
