package com.wd.daquan.imui.adapter.viewholder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wd.daquan.R;

/**
 * 右侧文本加载布局，带加载状态
 * 构造函数必须使用RightTextViewHolder(@NonNull View itemView)的方式，不可以使用其他方式的构造函数
 */

public class RightTextViewHolder extends BaseRightViewHolder{

    public TextView rightTextContent;

    /**
     * 这个会执行两次
     * @param itemView
     */
    public RightTextViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void initView() {
        super.initView();
        rightTextContent = itemView.findViewById(R.id.item_right_text_tv);
    }

    @Override
    protected View childView(ViewGroup parent) {
        View rightTextView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_right_text, parent, false);
        rightTextView.setTag("chatRightText");
        View content = addRootView(rightTextView);
        return content;
    }

    @Override
    protected Class<? extends BaseRightViewHolder> getClassType() {
        return getClass();
    }
}