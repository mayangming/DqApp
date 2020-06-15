package com.wd.daquan.contacts.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wd.daquan.R;


/**
 * @Author: 方志
 * @Time: 2018/8/30 14:53
 * @Description:
 */
public class SelectMobileContactHolder extends RecyclerView.ViewHolder {

    public TextView letter;
    public RelativeLayout itemRl;
    public ImageView portrait;
    public CheckBox checkbox;
    public LinearLayout nameLl;
    public TextView userName;
    public TextView nickName;



    public SelectMobileContactHolder(View view) {
        super(view);
        letter = view.findViewById(R.id.item_letter_tv);
        itemRl = view.findViewById(R.id.item_container_rl);
        portrait = view.findViewById(R.id.item_portrait_iv);
        checkbox = view.findViewById(R.id.item_select_cb);
        nameLl = view.findViewById(R.id.item_name_ll);
        userName = view.findViewById(R.id.item_user_name_tv);
        nickName = view.findViewById(R.id.item_nick_name_tv);

        userName.setMaxEms(16);
        nickName.setMaxEms(16);
        portrait.setVisibility(View.GONE);
    }
}
