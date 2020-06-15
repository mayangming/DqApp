package com.wd.daquan.chat.ait;

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
public class AitHolder extends RecyclerView.ViewHolder {

    public RelativeLayout rlyt;
    public ImageView portrait = null;
    public TextView name = null;
    public TextView nameNum = null;

    public AitHolder(View view) {
        super(view);
        this.portrait = view.findViewById(R.id.ait_item_portrait_iv);
        this.name = view.findViewById(R.id.ait_item_name_tv);
        this.nameNum = view.findViewById(R.id.ait_item_name_num_tv);
        this.rlyt = view.findViewById(R.id.ait_item_container_rl);
    }

}
