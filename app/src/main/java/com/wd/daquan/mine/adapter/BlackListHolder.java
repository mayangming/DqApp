package com.wd.daquan.mine.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wd.daquan.R;

public class BlackListHolder extends RecyclerView.ViewHolder{

    public ImageView mAvatar;
    public TextView name;
    public Button btn_remove;
    public RelativeLayout layout;

    public BlackListHolder(View view) {
        super(view);
        layout = view.findViewById(R.id.itemBlackListLayout);
        mAvatar = view.findViewById(R.id.itemBlackListAvatar);
        name = view.findViewById(R.id.itemBlackListName);
        btn_remove = view.findViewById(R.id.itemBlackListBtn);
    }

}
