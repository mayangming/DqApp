package com.wd.daquan.imui.adapter.viewholder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wd.daquan.R;

/**
 * 左侧卡片布局
 */
public class LeftCardMsgViewHolder extends BaseLeftViewHolder{
    public RelativeLayout mRlyt;
    public ImageView mIcon;
    public TextView mName;
    public TextView mTips;
    public TextView dqNum;
    public LeftCardMsgViewHolder(@NonNull View itemView) {
        super(itemView);
        initView();
    }
    public void initView(){
        super.initView();
        mRlyt = itemView.findViewById(R.id.card_item_llyt);
        mName = itemView.findViewById(R.id.card_item_name);
        mTips = itemView.findViewById(R.id.card_item_tips);
        mIcon = itemView.findViewById(R.id.card_item_icon);
        dqNum = itemView.findViewById(R.id.card_item_dq_num);
    }
    @Override
    protected View childView(ViewGroup parent) {
        View leftCardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_card,parent,false);
        leftCardView.setTag("leftChatCard");
        View content = addRootView(leftCardView);
        return content;
    }

    @Override
    protected Class<? extends BaseLeftViewHolder> getClassType() {
        return getClass();
    }
}