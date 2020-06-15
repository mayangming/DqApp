package com.wd.daquan.imui.adapter.viewholder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wd.daquan.R;


/**
 * 左侧文本布局
 */
public class LeftTextViewHolder extends BaseLeftViewHolder{
    public TextView leftTextContent;
    public LeftTextViewHolder(@NonNull View itemView) {
        super(itemView);
        initView();
    }

    public void initView(){
        super.initView();
        leftTextContent = itemView.findViewById(R.id.item_left_text_tv);
    }
    @Override
    protected View childView(ViewGroup parent) {
        View leftTextView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_text,parent,false);
        leftTextView.setTag("leftChatVideo");
        View content = addRootView(leftTextView);
        return content;
    }

    @Override
    protected Class<? extends BaseLeftViewHolder> getClassType() {
        return getClass();
    }
}