package com.wd.daquan.chat.group.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.wd.daquan.R;

/**
 * Author: 方志
 * Time:  15:17
 * Desc: 退出群成员列表
 * Edit：
 */
public class ExitGroupMemberViewHolder extends RecyclerView.ViewHolder {

    public RelativeLayout layout;
    public ImageView portrait;
    public LinearLayout nameLl;
    public TextView userName;
    public TextView exitType;
    public TextView time;

    public ExitGroupMemberViewHolder(View view) {
        super(view);
        layout = view.findViewById(R.id.item_container_rl);
        portrait = view.findViewById(R.id.item_portrait_iv);
        nameLl = view.findViewById(R.id.item_name_ll);
        userName = view.findViewById(R.id.item_user_name_tv);
        exitType = view.findViewById(R.id.item_nick_name_tv);
        time = view.findViewById(R.id.item_time_tv);
    }
}
