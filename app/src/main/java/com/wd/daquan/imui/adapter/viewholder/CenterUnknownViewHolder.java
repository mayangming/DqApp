package com.wd.daquan.imui.adapter.viewholder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wd.daquan.R;


/**
 * 未知消息类型
 */
public class CenterUnknownViewHolder extends RecycleBaseViewHolder implements ChatViewHolderStrategy{

    public TextView centerTextContent;
    public CenterUnknownViewHolder(@NonNull View itemView) {
        super(itemView);
        initView();
    }

    public void initView(){
        centerTextContent = itemView.findViewById(R.id.item_center_unknown_tv);
    }

    @Override
    public RecycleBaseViewHolder createViewHolder(View view) {
        View centerTextView = LayoutInflater.from(view.getContext()).inflate(R.layout.item_center_unknown,(ViewGroup)view,false);
        return new CenterUnknownViewHolder(centerTextView);
    }
}