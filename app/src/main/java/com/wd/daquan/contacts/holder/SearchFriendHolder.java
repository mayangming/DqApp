package com.wd.daquan.contacts.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wd.daquan.R;

/**
 * @Author: 方志
 * @Time: 2018/9/6 12:06
 * @Description: 联系人
 */
public class SearchFriendHolder extends RecyclerView.ViewHolder{

    // 头像
    public ImageView portrait;
    // 好友备注
    public TextView friendRemark;
    // 好友昵称
    public TextView nickname;
    // 好友昵称
    public LinearLayout nameLl;


    public SearchFriendHolder(View view) {
        super(view);
        portrait = view.findViewById(R.id.item_portrait_iv);
        friendRemark = view.findViewById(R.id.item_user_name_tv);
        nickname = view.findViewById(R.id.item_nick_name_tv);
        nameLl = view.findViewById(R.id.item_name_ll);
    }

}
