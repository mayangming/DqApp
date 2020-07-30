package com.wd.daquan.imui.adapter.viewholderbind;


import androidx.lifecycle.LifecycleObserver;

import com.dq.im.bean.im.MessageRedPackageBean;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.model.TeamMessageBaseModel;
import com.dq.im.type.ImType;
import com.google.gson.Gson;
import com.wd.daquan.R;
import com.wd.daquan.imui.adapter.viewholder.RightRedPackageViewHolder;
import com.wd.daquan.imui.type.RedPackageStatus;

/**
 * 右侧红包内容填充
 */
public class RightRedPackageViewHolderBind extends BaseRightViewHolderBind<RightRedPackageViewHolder> {
    private Gson gson = new Gson();

    @Override
    public LifecycleObserver bindViewHolder(RightRedPackageViewHolder redPackageViewHolder, ImMessageBaseModel imMessageBaseModel, ImType chatType) {
        if (ImType.P2P == chatType) {
            P2PMessageBaseModel p2PMessageBaseModel = (P2PMessageBaseModel) imMessageBaseModel;
            setP2PRightTextData(redPackageViewHolder, p2PMessageBaseModel);
        } else if (ImType.Team == chatType) {
            TeamMessageBaseModel teamMessageBaseModel = (TeamMessageBaseModel) imMessageBaseModel;
            setTeamRightTextData(redPackageViewHolder, teamMessageBaseModel);
        }
        return super.bindViewHolder(redPackageViewHolder, imMessageBaseModel, chatType);
    }

    private void setP2PRightTextData(RightRedPackageViewHolder rightTextViewHolder, P2PMessageBaseModel p2PMessageBaseModel){
        String content = p2PMessageBaseModel.getSourceContent();
        MessageRedPackageBean messageTextBean = gson.fromJson(content,MessageRedPackageBean.class);
        String statusContent = RedPackageStatus.getRedPackageStatusDescription(messageTextBean.getStatus());
        rightTextViewHolder.redPackageStatus.setText(statusContent);
        RedPackageStatus redPackageStatus = RedPackageStatus.getRedPackageStatus(messageTextBean.getStatus());
        switch (redPackageStatus){
            case RED_PACKAGE_UN_RECEIVE:
                rightTextViewHolder.redPackageBg.setImageResource(R.mipmap.im_red_package_bubble_right_orange);
                rightTextViewHolder.redPackageIcon.setImageResource(R.mipmap.im_red_package_no_open_icon);
                break;
            case RED_PACKAGE_RECEIVED:
                rightTextViewHolder.redPackageBg.setImageResource(R.mipmap.im_red_package_bubble_right_grey);
                rightTextViewHolder.redPackageIcon.setImageResource(R.mipmap.im_red_package_opened_icon);
                break;
        }
    }

    private void setTeamRightTextData(RightRedPackageViewHolder rightTextViewHolder, TeamMessageBaseModel teamMessageBaseModel){
        String content = teamMessageBaseModel.getSourceContent();
        MessageRedPackageBean messageTextBean = gson.fromJson(content,MessageRedPackageBean.class);
        String statusContent = RedPackageStatus.getRedPackageStatusDescription(messageTextBean.getStatus());
//        String contentText = statusContent.concat("->金额:").concat(messageTextBean.getMoney());
//        rightTextViewHolder.rightTextContent.setText(contentText);
        rightTextViewHolder.redPackageStatus.setText(statusContent);
        RedPackageStatus redPackageStatus = RedPackageStatus.getRedPackageStatus(messageTextBean.getStatus());
        switch (redPackageStatus){
            case RED_PACKAGE_UN_RECEIVE:
                rightTextViewHolder.redPackageBg.setImageResource(R.mipmap.im_red_package_bubble_right_orange);
                rightTextViewHolder.redPackageIcon.setImageResource(R.mipmap.im_red_package_no_open_icon);
                break;
            case RED_PACKAGE_RECEIVED:
                rightTextViewHolder.redPackageBg.setImageResource(R.mipmap.im_red_package_bubble_right_grey);
                rightTextViewHolder.redPackageIcon.setImageResource(R.mipmap.im_red_package_opened_icon);
                break;
        }
    }
}