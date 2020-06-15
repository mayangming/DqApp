package com.wd.daquan.chat.group.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.da.library.adapter.CommRecyclerViewAdapter;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.da.library.adapter.CommBaseSelectAdapter;
import com.wd.daquan.chat.group.adapter.holder.TeamListHolder;
import com.wd.daquan.chat.group.bean.TeamListEntity;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.bean.TeamBean;

import org.jetbrains.annotations.NotNull;

/**
 * @author: dukangkang
 * @date: 2018/9/19 20:17.
 * @description: todo ...
 */
public class TeamListAdapter extends CommRecyclerViewAdapter<TeamBean, TeamListHolder> {

    @Override
    public TeamListHolder onBindView(@NonNull ViewGroup parent, int viewType) {
        return new TeamListHolder(LayoutInflater.from(mContext).inflate(R.layout.team_list_item, null));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindData(@NotNull TeamListHolder holder, int position) {
        super.onBindData(holder, position);
        TeamBean entity = allList.get(position);

        holder.name.setText(entity.group_name);
        holder.nameNum.setText("(" + entity.getGroup_member_num() + ")");

        GlideUtils.loadHeader(DqApp.sContext, entity.group_pic, holder.portrait);
    }
}
