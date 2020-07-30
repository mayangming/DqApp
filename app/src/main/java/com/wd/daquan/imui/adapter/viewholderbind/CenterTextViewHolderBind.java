package com.wd.daquan.imui.adapter.viewholderbind;


import androidx.lifecycle.LifecycleObserver;

import com.dq.im.bean.im.MessageTextBean;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.model.TeamMessageBaseModel;
import com.dq.im.type.ImType;
import com.google.gson.Gson;
import com.wd.daquan.imui.adapter.viewholder.CenterTextViewHolder;
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder;

/**
 * 中间文本内容填充
 */
public class CenterTextViewHolderBind implements ChatViewHolderBindStrategy, LifecycleObserver {
    private Gson gson = new Gson();

    @Override
    public LifecycleObserver bindViewHolder(RecycleBaseViewHolder holder, ImMessageBaseModel imMessageBaseModel, ImType chatType) {
        CenterTextViewHolder rightTextViewHolder = (CenterTextViewHolder) holder;
        if (ImType.P2P == chatType){
            P2PMessageBaseModel p2PMessageBaseModel = (P2PMessageBaseModel) imMessageBaseModel;
            setP2PCenterTextData(rightTextViewHolder,p2PMessageBaseModel);
        }else if (ImType.Team == chatType){
            TeamMessageBaseModel teamMessageBaseModel = (TeamMessageBaseModel) imMessageBaseModel;
            setTeamCenterTextData(rightTextViewHolder,teamMessageBaseModel);
        }
        return this;
    }

    private void setP2PCenterTextData(CenterTextViewHolder centerTextViewHolder, P2PMessageBaseModel p2PMessageBaseModel){
        String content = p2PMessageBaseModel.getSourceContent();
        MessageTextBean messageTextBean = gson.fromJson(content,MessageTextBean.class);
        centerTextViewHolder.centerTextContent.setText(messageTextBean.getDescription());
    }

    private void setTeamCenterTextData(CenterTextViewHolder centerTextViewHolder, TeamMessageBaseModel teamMessageBaseModel){
        String content = teamMessageBaseModel.getSourceContent();
        MessageTextBean messageTextBean = gson.fromJson(content,MessageTextBean.class);
        centerTextViewHolder.centerTextContent.setText(messageTextBean.getDescription());
    }

}