package com.wd.daquan.contacts.holder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wd.daquan.R;

/**
 * @Author: 方志
 * @Time: 2018/9/20 20:09
 * @Description:
 */
public class GroupHolder extends RecyclerView.ViewHolder {
    public RelativeLayout layout;
    public ImageView portrait;
    public TextView name;

    public GroupHolder(View itemView) {
        super(itemView);
        layout = itemView.findViewById(R.id.item_container_rl);
        portrait = itemView.findViewById(R.id.item_portrait_iv);
        name = itemView.findViewById(R.id.item_name_tv);
    }
}
