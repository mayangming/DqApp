package com.wd.daquan.chat.single;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.da.library.adapter.CommRecyclerViewAdapter;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.chat.group.adapter.holder.ChatDetailsHolder;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.bean.Friend;

import org.jetbrains.annotations.NotNull;

public class SingleChatDetailsAdapter extends CommRecyclerViewAdapter<Friend, ChatDetailsHolder> {


    @Override
    protected ChatDetailsHolder onBindView(ViewGroup parent, int viewType) {
        return new ChatDetailsHolder(mInflater.inflate(R.layout.social_chatsetting_gridview_item, parent, false));
    }

    @Override
    protected void onBindData(@NotNull ChatDetailsHolder holder, int position) {
        int size = allList.size();
        if(size > 0 && position == 0) {
            Friend item = getItem(position);
            String name = TextUtils.isEmpty(item.friend_remarks) ? item.nickname : item.getFriend_remarks();
            holder.name.setText(name);
            GlideUtils.loadHeader(DqApp.sContext, item.getHeadpic(), holder.avatar);
            if (item.isVip()){
                holder.headOutline.setVisibility(View.VISIBLE);
            }else {
                holder.headOutline.setVisibility(View.GONE);
            }
        }else {
            holder.name.setText("");
            holder.avatar.setImageResource(R.mipmap.comm_add_btn);
            holder.headOutline.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(view -> {
            if(getListener() != null) {
                getListener().onItemClick(null, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }
}
