package com.wd.daquan.contacts.holder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wd.daquan.R;

/**
 * @Author: 方志
 * @Time: 2018/9/26 10:42
 * @Description: 群组个人信息描述
 */
public class InfoDescViewHolder extends RecyclerView.ViewHolder {
    public TextView item;

    public InfoDescViewHolder(View itemView) {
        super(itemView);
        item = itemView.findViewById(R.id.tv_personal_info_desc);
    }
}