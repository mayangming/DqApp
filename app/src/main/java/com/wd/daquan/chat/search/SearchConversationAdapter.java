package com.wd.daquan.chat.search;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.da.library.adapter.CommBaseSelectAdapter;
import com.wd.daquan.chat.bean.RecentContactEntity;
import com.wd.daquan.glide.GlideUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @author: dukangkang
 * @date: 2018/9/26 19:07.
 * @description: todo ...
 */
public class SearchConversationAdapter extends CommBaseSelectAdapter<RecentContactEntity, SearchConversationHolder> {

    @Override
    public SearchConversationHolder onBindView(@NonNull ViewGroup parent, int viewType) {
        return new SearchConversationHolder(LayoutInflater.from(mContext).inflate(R.layout.search_conversation_list_item, null));
    }

    @Override
    public void onBindData(@NotNull @NonNull SearchConversationHolder holder, int position) {
        RecentContactEntity entity = allList.get(position);
        holder.name.setText(entity.fromNick);
        GlideUtils.loadHeader(DqApp.sContext, entity.headpic, holder.portrait);

        //item点击事件
        holder.rlyt.setOnClickListener(v -> {

            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(entity);
            }
        });
    }

}
