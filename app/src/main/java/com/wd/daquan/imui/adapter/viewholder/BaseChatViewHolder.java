package com.wd.daquan.imui.adapter.viewholder;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wd.daquan.R;

import java.lang.reflect.Constructor;

/**
 * 聊天item基础布局
 *
 */
public abstract class BaseChatViewHolder extends RecycleBaseViewHolder implements ChatViewHolderStrategy{
    public TextView chatTime;//显示聊天时间的控件
    public BaseChatViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @CallSuper
    @Override
    public void initView() {
        super.initView();
        chatTime = itemView.findViewById(R.id.chat_time);
    }

    /**
     * 添加基础聊天布局
     * @param view
     * @return
     */
    @Override
    public RecycleBaseViewHolder createViewHolder(View view) {
        View childView = childView((ViewGroup) view);
        BaseChatViewHolder baseChatViewHolder = null;
        Class<? extends BaseChatViewHolder> childClass = getClassType();
        try {
            Constructor<? extends BaseChatViewHolder> cons = childClass.getConstructor(View.class);
            baseChatViewHolder = cons.newInstance(childView);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseChatViewHolder;
    }

    /**
     * 这里不用子类返回布局Id的方式进行确定子布局内容，是为了返回View，可以使用户对抽象View进行修改
     * @return View
     */
    protected View childView(ViewGroup parent){

        return null;
    }

    /**
     * 添加公共布局
     * @param view 即将进行包装的控件
     * @return 完成公共布局包装的控件
     */
    protected View addRootView(View view){
        ViewGroup viewGroup = (ViewGroup)view;
        ViewGroup rootView = (ViewGroup) LayoutInflater.from(view.getContext()).inflate(R.layout.item_chat_base,viewGroup,false);
        ViewGroup container = rootView.findViewById(R.id.container);
        container.addView(view);
        return rootView;
    }

    /**
     * 确定子类的类型
     */
    protected  Class<? extends BaseChatViewHolder> getClassType(){
        return null;
    }
}