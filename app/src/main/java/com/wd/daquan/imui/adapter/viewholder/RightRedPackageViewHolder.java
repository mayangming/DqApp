package com.wd.daquan.imui.adapter.viewholder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;


/**
 * 右侧文本加载布局，带加载状态
 * 构造函数必须使用RightTextViewHolder(@NonNull View itemView)的方式，不可以使用其他方式的构造函数
 */

public class RightRedPackageViewHolder extends BaseRightViewHolder{

//    public TextView item_right_red_package_tv;
    public ImageView redPackageBg;
    public ImageView redPackageIcon;
    public TextView redPackageStatus;
    public TextView redPackageSource;

    public RightRedPackageViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void initView() {
        super.initView();
//        item_right_red_package_tv = itemView.findViewById(R.id.item_right_red_da);
        redPackageBg = itemView.findViewById(R.id.item_right_red_package_bg);
        redPackageIcon = itemView.findViewById(R.id.item_right_red_package_icon);
        redPackageStatus = itemView.findViewById(R.id.item_right_red_package_status);
        redPackageSource = itemView.findViewById(R.id.item_right_red_package_source);
    }
    @Override
    protected View childView(ViewGroup parent) {
        View rightRedPackage = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_right_red_package,parent,false);
        rightRedPackage.setTag("chatRightRedPackage");
        View content = addRootView(rightRedPackage);
        return content;
    }

    @Override
    protected Class<? extends BaseRightViewHolder> getClassType() {
        return getClass();
    }
}