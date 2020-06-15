package com.wd.daquan.chat.group.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wd.daquan.R;

/**
 * @author: dukangkang
 * @date: 2018/9/19 20:24.
 * @description: todo ...
 */
public class TeamListHolder extends RecyclerView.ViewHolder {

    public RelativeLayout rlyt;
    public ImageView portrait = null;
    public TextView name = null;
    public TextView nameNum = null;

    public TeamListHolder(View view) {
        super(view);
        this.portrait = view.findViewById(R.id.teamlist_item_portrait_iv);
        this.name = view.findViewById(R.id.teamlist_item_name_tv);
        this.nameNum = view.findViewById(R.id.teamlist_item_name_num_tv);
        this.rlyt = view.findViewById(R.id.teamlist_item_container_rl);
    }

}
