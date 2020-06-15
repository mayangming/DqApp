package com.wd.daquan.contacts.adapter;

import android.view.ViewGroup;

import com.wd.daquan.R;
import com.da.library.adapter.CommRecyclerViewAdapter;
import com.wd.daquan.contacts.holder.InfoDescViewHolder;

import org.jetbrains.annotations.NotNull;

/**
 * Author: 方志
 * Time: 2018/4/24  17:32
 * Desc: GroupPersonalInfoShowAdapter类，展示群个人信息描述的适配器
 * EditContent：
 */
public class GroupPersonalInfoShowAdapter extends CommRecyclerViewAdapter<String, InfoDescViewHolder> {

    @Override
    protected InfoDescViewHolder onBindView(ViewGroup parent, int viewType) {
        return new InfoDescViewHolder(mInflater.inflate(R.layout.cn_personal_info_show_item, parent, false));
    }

    @Override
    protected void onBindData(@NotNull InfoDescViewHolder holder, int position) {
        holder.item.setText(getItem(position));
    }


}
