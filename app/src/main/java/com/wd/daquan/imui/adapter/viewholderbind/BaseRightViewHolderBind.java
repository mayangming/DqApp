package com.wd.daquan.imui.adapter.viewholderbind;

import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.view.View;

import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.type.ImType;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.imui.adapter.RecycleItemOnClickForChildViewListenerCompat;
import com.wd.daquan.imui.adapter.viewholder.BaseRightViewHolder;
import com.wd.daquan.model.mgr.ModuleMgr;

/**
 * 右侧基础数据填充类
 */
public abstract class BaseRightViewHolderBind<T extends BaseRightViewHolder> extends BaseChatViewHolderBind<T>{

    @CallSuper
    @Override
    public LifecycleObserver bindViewHolder(T t, ImMessageBaseModel imMessageBaseModel, ImType chatType) {
        String sessionType = ImType.P2P.getValue();
        setHeadIconListener(t,imMessageBaseModel,sessionType);
        t.messageFailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != RecycleItemOnClickForChildViewListenerCompat.getRecycleItemOnClickForChildViewListener()){
                    RecycleItemOnClickForChildViewListenerCompat.getRecycleItemOnClickForChildViewListener().onItemForChildClick(v,imMessageBaseModel);
                }
            }
        });
        return this;
    }

    private void setHeadIconListener(T holder,ImMessageBaseModel imMessageBaseModel,String sessionType){
        holder.rightHeadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                NavUtils.gotoUserInfoActivity(context,ModuleMgr.getCenterMgr().getUID(),sessionType);
            }
        });
    }
}