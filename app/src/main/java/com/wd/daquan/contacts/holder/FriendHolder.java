package com.wd.daquan.contacts.holder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wd.daquan.R;

/**
 * @Author: 方志
 * @Time: 2018/9/6 12:06
 * @Description: 联系人
 */
public class FriendHolder extends RecyclerView.ViewHolder{

    //首字母
    public TextView letter;
    // 昵称
    public TextView name;
    // 头像
    public ImageView portrait;
    public ImageView head_outline;//头像外轮廓
    public RelativeLayout layout;


    public FriendHolder(View view) {
        super(view);
        letter = view.findViewById(R.id.item_letter_tv);
        layout = view.findViewById(R.id.item_container_rl);
        portrait = view.findViewById(R.id.item_portrait_iv);
        head_outline = view.findViewById(R.id.item_portrait_head_outline);
        name = view.findViewById(R.id.item_name_tv);
    }

}
