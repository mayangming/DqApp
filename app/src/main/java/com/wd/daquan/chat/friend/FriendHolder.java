package com.wd.daquan.chat.friend;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;


/**
 * @Author: 方志
 * @Time: 2018/8/30 14:53
 * @Description:
 */
public class FriendHolder extends RecyclerView.ViewHolder {

    public TextView letter;
    public ImageView portrait;
    public CheckBox check;
    public TextView name;
    public ImageView vipIcon;
    public FriendHolder(View view) {
        super(view);
        letter = view.findViewById(R.id.item_letter_tv);
        portrait = view.findViewById(R.id.item_portrait_iv);
        check = view.findViewById(R.id.item_select_cb);
        name = view.findViewById(R.id.item_name_tv);
        vipIcon = view.findViewById(R.id.item_portrait_head_outline);
    }
}
