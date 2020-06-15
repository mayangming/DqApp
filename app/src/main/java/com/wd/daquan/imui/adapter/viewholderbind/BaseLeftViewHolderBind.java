package com.wd.daquan.imui.adapter.viewholderbind;

import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;
import android.support.annotation.CallSuper;

import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.type.ImType;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.imui.adapter.viewholder.BaseLeftViewHolder;
import com.wd.daquan.model.mgr.ModuleMgr;

/**
 * 左侧基础数据填充类
 */
public abstract class BaseLeftViewHolderBind<T extends BaseLeftViewHolder> extends BaseChatViewHolderBind<T>{
    String friendId = "";
    @CallSuper
    @Override
    public LifecycleObserver bindViewHolder(T t, ImMessageBaseModel imMessageBaseModel, ImType chatType) {
        String sessionType = ImType.P2P.getValue();
        setHeadIconListener(t,imMessageBaseModel,sessionType);
        return this;
    }

    private void setHeadIconListener(T holder,ImMessageBaseModel imMessageBaseModel,String sessionType){
        String userId = ModuleMgr.getCenterMgr().getUID();
        if (userId.equals(imMessageBaseModel.getFromUserId())){
            friendId = imMessageBaseModel.getToUserId();
        }else {
            friendId = imMessageBaseModel.getFromUserId();
        }
        holder.leftHeadIcon.setOnClickListener(v -> {
            Context context = v.getContext();
            NavUtils.gotoUserInfoActivity(context, friendId,sessionType);
        });
    }
}