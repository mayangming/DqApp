package com.wd.daquan.imui.adapter.viewholder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;

/**
 * 右侧基础布局
 */
public abstract class BaseRightViewHolder extends BaseChatViewHolder{
    public ImageView rightHeadIcon;
    public ImageView rightHeadIconVip;//Vip标识
    public View messageLoadingView;//加载状态的控件
    public ImageView messageFailView;//加载状态的控件
    public TextView rightUserName;//右侧昵称

    public BaseRightViewHolder(@NonNull View itemView) {
        super(itemView);
    }
    /**
     * 构造方法中会执行两次(因为是使用反射实例化的该类),第一次没有填充内容，第二次时候才会填充内容,所以把initView()放在构造方法中时候会执行两次，如果想解决的话可以放在createViewHolder()方法里
     */
    public void initView() {
        super.initView();
        messageLoadingView = itemView.findViewById(R.id.message_loading);
        messageFailView = itemView.findViewById(R.id.message_fail);
        rightHeadIcon = itemView.findViewById(R.id.item_right_head_icon);
        rightHeadIconVip = itemView.findViewById(R.id.item_right_head_icon_vip);
        rightUserName = itemView.findViewById(R.id.item_right_name);
    }

    @Override
    public RecycleBaseViewHolder createViewHolder(View view) {
        return super.createViewHolder(view);
    }

    @Override
    protected View addRootView(View view) {
        ViewGroup viewGroup = (ViewGroup)view;
        ViewGroup rootView = (ViewGroup) LayoutInflater.from(view.getContext()).inflate(R.layout.item_right_base,viewGroup,false);
        ViewGroup baseRightContainer = rootView.findViewById(R.id.base_right_container);
        baseRightContainer.addView(view);
//        rootView.addView(view);
        return super.addRootView(rootView);
    }

    @Override
    protected Class<? extends BaseRightViewHolder> getClassType() {
        return getClass();
    }
}