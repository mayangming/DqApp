package com.wd.daquan.imui.adapter.viewholderbind;


import android.arch.lifecycle.LifecycleObserver;

import com.da.library.tools.AESHelper;
import com.dq.im.bean.im.MessageTextBean;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.model.TeamMessageBaseModel;
import com.dq.im.type.ImType;
import com.google.gson.Gson;
import com.wd.daquan.imui.adapter.viewholder.RightTextViewHolder;

/**
 * 右侧文本内容填充
 */
public class RightTextViewHolderBind extends BaseRightViewHolderBind<RightTextViewHolder> {
    private Gson gson = new Gson();

    @Override
    public LifecycleObserver bindViewHolder(RightTextViewHolder rightTextViewHolder, ImMessageBaseModel imMessageBaseModel, ImType chatType) {
        if (ImType.P2P == chatType){
            P2PMessageBaseModel p2PMessageBaseModel = (P2PMessageBaseModel) imMessageBaseModel;
            setP2PRightTextData(rightTextViewHolder,p2PMessageBaseModel);
        }else if (ImType.Team == chatType){
            TeamMessageBaseModel teamMessageBaseModel = (TeamMessageBaseModel) imMessageBaseModel;
            setTeamRightTextData(rightTextViewHolder,teamMessageBaseModel);
        }
        return super.bindViewHolder(rightTextViewHolder, imMessageBaseModel, chatType);
    }

    private void setP2PRightTextData(RightTextViewHolder rightTextViewHolder, P2PMessageBaseModel p2PMessageBaseModel){
        String content = p2PMessageBaseModel.getSourceContent();
        MessageTextBean messageTextBean = gson.fromJson(content,MessageTextBean.class);
        String text = AESHelper.decryptString(messageTextBean.getDescription());
        rightTextViewHolder.rightTextContent.setText(text);
    }

    private void setTeamRightTextData(RightTextViewHolder rightTextViewHolder, TeamMessageBaseModel teamMessageBaseModel){
        String content = teamMessageBaseModel.getSourceContent();
        MessageTextBean messageTextBean = gson.fromJson(content,MessageTextBean.class);
        String text = AESHelper.decryptString(messageTextBean.getDescription());
        rightTextViewHolder.rightTextContent.setText(text);
    }

}