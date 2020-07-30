package com.wd.daquan.imui.adapter.viewholder;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;

/**
 * 左侧红包布局
 */
public class LeftRedPackageViewHolder extends BaseLeftViewHolder{
    public TextView leftRedPackageContent;
    public ImageView leftRedPackageBg;
    public ImageView leftRedPackageIcon;
    public TextView leftRedPackageStatus;
    public TextView leftRedPackageSource;
    public LeftRedPackageViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void initView(){
        super.initView();
        leftRedPackageContent = itemView.findViewById(R.id.item_left_red_package_tv);
        leftRedPackageBg = itemView.findViewById(R.id.item_left_red_package_bg);
        leftRedPackageIcon = itemView.findViewById(R.id.item_left_red_package_icon);
        leftRedPackageStatus = itemView.findViewById(R.id.item_left_red_package_status);
        leftRedPackageSource = itemView.findViewById(R.id.item_left_red_package_source);
    }
    @Override
    protected View childView(ViewGroup parent) {
        View leftRedPackage = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_red_package,parent,false);
        leftRedPackage.setTag("leftChatRed");
        View content = addRootView(leftRedPackage);
        return content;
    }

    @Override
    protected Class<? extends BaseLeftViewHolder> getClassType() {
        return getClass();
    }
}