package com.wd.daquan.imui.adapter.viewholderbind;


import android.arch.lifecycle.LifecycleObserver;

import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.type.ImType;
import com.google.gson.Gson;
import com.wd.daquan.imui.adapter.viewholder.CenterUnknownViewHolder;
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder;

/**
 * 未知内容填充
 */
public class CenterUnknownViewHolderBind implements ChatViewHolderBindStrategy, LifecycleObserver {
    private Gson gson = new Gson();

    @Override
    public LifecycleObserver bindViewHolder(RecycleBaseViewHolder holder, ImMessageBaseModel imMessageBaseModel, ImType chatType) {
        CenterUnknownViewHolder rightTextViewHolder = (CenterUnknownViewHolder) holder;
        rightTextViewHolder.centerTextContent.setText("[未知类型]");
//        P2PMessageBaseModel p2PMessageBaseModel = (P2PMessageBaseModel) imMessageBaseModel;
//        setCenterTextData(rightTextViewHolder,p2PMessageBaseModel);
        return this;
    }

//    private void setCenterTextData(CenterTextViewHolder centerTextViewHolder, P2PMessageBaseModel p2PMessageBaseModel){
//        String content = p2PMessageBaseModel.getSourceContent();
//        MessageTextBean messageTextBean = gson.fromJson(content,MessageTextBean.class);
//        centerTextViewHolder.centerTextContent.setText(messageTextBean.getDescription());
//    }

}