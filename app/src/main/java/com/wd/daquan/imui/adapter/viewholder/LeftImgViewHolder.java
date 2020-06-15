package com.wd.daquan.imui.adapter.viewholder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wd.daquan.R;


/**
 * 左侧图像布局
 */
public class LeftImgViewHolder extends BaseLeftViewHolder{
    public ImageView leftImgContent;
    public LeftImgViewHolder(@NonNull View itemView) {
        super(itemView);
        initView();
    }

    public void initView() {
        super.initView();
        leftImgContent = itemView.findViewById(R.id.item_left_img_iv);
    }
    @Override
    protected View childView(ViewGroup parent) {
        View leftImgView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_img,parent,false);
        leftImgView.setTag("leftChatImg");
        View content = addRootView(leftImgView);
        return content;
    }

    @Override
    protected Class<? extends BaseLeftViewHolder> getClassType() {
        return getClass();
    }
}