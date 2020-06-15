package com.wd.daquan.imui.adapter.viewholderbind;


import android.arch.lifecycle.LifecycleObserver;
import android.view.View;

import com.dq.im.bean.im.MessageCardBean;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.model.TeamMessageBaseModel;
import com.dq.im.type.ImType;
import com.google.gson.Gson;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.imui.adapter.viewholder.LeftCardMsgViewHolder;
import com.wd.daquan.util.GlideUtil;

/**
 * 左侧名片内容填充
 * 个人和群组的布局是一样的，不过这里还是做下区分
 */
public class LeftCardViewHolderBind extends BaseLeftViewHolderBind<LeftCardMsgViewHolder> {
    private Gson gson = new Gson();

    @Override
    public LifecycleObserver bindViewHolder(LeftCardMsgViewHolder cardMsgViewHolder, ImMessageBaseModel imMessageBaseModel, ImType chatType) {
        if (ImType.P2P == chatType) {
            P2PMessageBaseModel p2PMessageBaseModel = (P2PMessageBaseModel) imMessageBaseModel;
            setP2PLeftCardData(cardMsgViewHolder, p2PMessageBaseModel);
        } else if (ImType.Team == chatType) {
            TeamMessageBaseModel teamMessageBaseModel = (TeamMessageBaseModel) imMessageBaseModel;
            setTeamLeftCardData(cardMsgViewHolder, teamMessageBaseModel);
        }
        return super.bindViewHolder(cardMsgViewHolder, imMessageBaseModel, chatType);
    }

    private void setP2PLeftCardData(LeftCardMsgViewHolder leftCardMsgViewHolder, P2PMessageBaseModel p2PMessageBaseModel){
        String content = p2PMessageBaseModel.getSourceContent();
        MessageCardBean messageCardBean = gson.fromJson(content,MessageCardBean.class);
        leftCardMsgViewHolder.mName.setText(messageCardBean.nickName);
//        leftCardMsgViewHolder.mTips.setText(messageCardBean.extra);
        leftCardMsgViewHolder.dqNum.setText(messageCardBean.dqNum);
        GlideUtil.loadNormalImgByNet(leftCardMsgViewHolder.itemView.getContext(),messageCardBean.headPic,leftCardMsgViewHolder.mIcon);
        leftCardMsgViewHolder.itemView.setOnClickListener(new CardDetails(messageCardBean.userId,p2PMessageBaseModel.getType()));
    }

    private void setTeamLeftCardData(LeftCardMsgViewHolder leftCardMsgViewHolder, TeamMessageBaseModel teamMessageBaseModel){
        String content = teamMessageBaseModel.getSourceContent();
        MessageCardBean messageCardBean = gson.fromJson(content,MessageCardBean.class);
        leftCardMsgViewHolder.mName.setText(messageCardBean.nickName);
        leftCardMsgViewHolder.dqNum.setText(messageCardBean.dqNum);
//        leftCardMsgViewHolder.mTips.setText(messageCardBean.extra);
        GlideUtil.loadNormalImgByNet(leftCardMsgViewHolder.itemView.getContext(),messageCardBean.headPic,leftCardMsgViewHolder.mIcon);
        leftCardMsgViewHolder.itemView.setOnClickListener(new CardDetails(messageCardBean.userId,teamMessageBaseModel.getType()));
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