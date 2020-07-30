package com.wd.daquan.imui.adapter.viewholder;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wd.daquan.R;


/**
 * 右侧图像加载布局，带加载状态
 */

public class RightImgViewHolder extends BaseRightViewHolder{

    public ImageView itemRightImgIv;
    public RightImgViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void initView(){
        super.initView();
        itemRightImgIv = itemView.findViewById(R.id.item_right_img_iv);
    }

    @Override
    protected View childView(ViewGroup parent) {
        View rightImgView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_right_img,parent,false);
        rightImgView.setTag("chatRightImg");
        View content = addRootView(rightImgView);
        return content;
    }

    @Override
    protected Class<? extends BaseRightViewHolder> getClassType() {
        return getClass();
    }
}