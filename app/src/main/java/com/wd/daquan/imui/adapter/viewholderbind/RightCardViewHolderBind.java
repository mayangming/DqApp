package com.wd.daquan.imui.adapter.viewholderbind;


import android.arch.lifecycle.LifecycleObserver;
import android.util.Log;
import android.view.View;

import com.dq.im.bean.im.MessageCardBean;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.model.TeamMessageBaseModel;
import com.dq.im.type.ImType;
import com.google.gson.Gson;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.imui.adapter.viewholder.RightCardMsgViewHolder;
import com.wd.daquan.util.GlideUtil;

/**
 * 右侧名片内容填充
 */
public class RightCardViewHolderBind extends BaseRightViewHolderBind<RightCardMsgViewHolder>{
    private Gson gson = new Gson();

    @Override
    public LifecycleObserver bindViewHolder(RightCardMsgViewHolder rightCardMsgViewHolder, ImMessageBaseModel imMessageBaseModel, ImType chatType) {
        if (ImType.P2P == chatType) {
            P2PMessageBaseModel p2PMessageBaseModel = (P2PMessageBaseModel) imMessageBaseModel;
            setP2PRightCardData(rightCardMsgViewHolder, p2PMessageBaseModel);
        } else if (ImType.Team == chatType) {
            TeamMessageBaseModel teamMessageBaseModel = (TeamMessageBaseModel) imMessageBaseModel;
            setTeamRightCardData(rightCardMsgViewHolder, teamMessageBaseModel);
        }
        return super.bindViewHolder(rightCardMsgViewHolder, imMessageBaseModel, chatType);
    }

    private void setP2PRightCardData(RightCardMsgViewHolder rightCardMsgViewHolder, P2PMessageBaseModel p2PMessageBaseModel){
        String content = p2PMessageBaseModel.getSourceContent();
        MessageCardBean messageCardBean = gson.fromJson(content,MessageCardBean.class);
        rightCardMsgViewHolder.mName.setText(messageCardBean.nickName);
        rightCardMsgViewHolder.dqNum.setText(messageCardBean.dqNum);
//        rightCardMsgViewHolder.mTips.setText(messageCardBean.extra);
        Log.e("YM","名片内容:"+messageCardBean.toString());
        GlideUtil.loadNormalImgByNet(rightCardMsgViewHolder.itemView.getContext(),messageCardBean.headPic,rightCardMsgViewHolder.mIcon);
        rightCardMsgViewHolder.itemView.setOnClickListener(new CardDetails(messageCardBean.userId,p2PMessageBaseModel.getType()));
    }

    private void setTeamRightCardData(RightCardMsgViewHolder rightCardMsgViewHolder, TeamMessageBaseModel teamMessageBaseModel){
        String content = teamMessageBaseModel.getSourceContent();
        MessageCardBean messageCardBean = gson.fromJson(content,MessageCardBean.class);
        rightCardMsgViewHolder.mName.setText(messageCardBean.nickName);
        rightCardMsgViewHolder.dqNum.setText(messageCardBean.dqNum);
//        rightCardMsgViewHolder.mTips.setText(messageCardBean.extra);
        GlideUtil.loadNormalImgByNet(rightCardMsgViewHolder.itemView.getContext(),messageCardBean.headPic,rightCardMsgViewHolder.mIcon);
        rightCardMsgViewHolder.itemView.setOnClickListener(new CardDetails(messageCardBean.userId,teamMessageBaseModel.getType()));
    }


    class CardDetails implements View.OnClickListener{
        private String userId;
        private String imType;
        public CardDetails(String userId,String imType) {
            this.userId = userId;
            this.imType = imType;
        }

        @Override
        public void onClick(View v) {
            NavUtils.gotoUserInfoActivity(v.getContext(), userId, ImType.P2P.getValue());
        }
    }
}