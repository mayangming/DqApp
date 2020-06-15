package com.wd.daquan.contacts.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wd.daquan.R;

/**
 * @Author: 方志
 * @Time: 2018/9/14 11:43
 * @Description:
 */
public class NewFriendHolder extends RecyclerView.ViewHolder {

    public RelativeLayout layout;
    public View headOutline;
    public ImageView portrait;
    public TextView name;
    public TextView agree;
    public TextView remark;

    public NewFriendHolder(View itemView) {
        super(itemView);
        layout = itemView.findViewById(R.id.item_container_rl);
        headOutline = itemView.findViewById(R.id.item_portrait_head_outline);
        portrait = itemView.findViewById(R.id.item_portrait_iv);
        name = itemView.findViewById(R.id.item_name_tv);
        agree = itemView.findViewById(R.id.item_add_tv);
        remark = itemView.findViewById(R.id.item_remark_tv);
    }
}
