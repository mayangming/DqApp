package com.wd.daquan.imui.adapter.viewholder;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;


/**
 * 左侧基础布局
 */
public abstract class BaseLeftViewHolder extends BaseChatViewHolder{
    public ImageView leftHeadIcon;
    public ImageView leftVipIcon;
    public TextView leftUserName;
    public BaseLeftViewHolder(@NonNull View itemView) {
        super(itemView);
    }
    public void initView() {
        super.initView();
        leftHeadIcon = itemView.findViewById(R.id.item_left_head_icon);
        leftVipIcon = itemView.findViewById(R.id.item_left_head_icon_vip);
        leftUserName = itemView.findViewById(R.id.item_left_user_name);
    }

    @Override
    protected View addRootView(View view) {
        ViewGroup viewGroup = (ViewGroup)view;
        ViewGroup rootView = (ViewGroup) LayoutInflater.from(view.getContext()).inflate(R.layout.item_left_base,viewGroup,false);
        ViewGroup baseRightContainer = rootView.findViewById(R.id.base_left_container);
        baseRightContainer.addView(view);
        return super.addRootView(rootView);
    }

    @Override
    protected Class<? extends BaseLeftViewHolder> getClassType() {
        return getClass();
    }
}