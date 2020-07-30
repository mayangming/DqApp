package com.wd.daquan.imui.adapter.viewholder;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wd.daquan.R;


/**
 * 中间为红包类型的文本加载布局
 */
public class CenterRedPackageTextViewHolder extends RecycleBaseViewHolder implements ChatViewHolderStrategy{

    public TextView centerTextContent;
    public CenterRedPackageTextViewHolder(@NonNull View itemView) {
        super(itemView);
        initView();
    }

    public void initView(){
        centerTextContent = itemView.findViewById(R.id.item_center_red_package_text_tv);
    }

    @Override
    public RecycleBaseViewHolder createViewHolder(View view) {
        View centerTextView = LayoutInflater.from(view.getContext()).inflate(R.layout.item_center_red_package_text,(ViewGroup)view,false);
        return new CenterRedPackageTextViewHolder(centerTextView);
    }
}