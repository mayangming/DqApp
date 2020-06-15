package com.wd.daquan.imui.adapter.viewholder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wd.daquan.R;


/**
 * 中间为文本加载布局
 */
public class CenterTextViewHolder extends RecycleBaseViewHolder implements ChatViewHolderStrategy{

    public TextView centerTextContent;
    public CenterTextViewHolder(@NonNull View itemView) {
        super(itemView);
        initView();
    }

    public void initView(){
        centerTextContent = itemView.findViewById(R.id.item_center_text_tv);
    }

    @Override
    public RecycleBaseViewHolder createViewHolder(View view) {
        View centerTextView = LayoutInflater.from(view.getContext()).inflate(R.layout.item_center_text,(ViewGroup)view,false);
        return new CenterTextViewHolder(centerTextView);
    }
}