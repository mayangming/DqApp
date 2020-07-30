package com.wd.daquan.imui.adapter.viewholderbind;


import androidx.lifecycle.LifecycleObserver;

import com.dq.im.bean.im.MessageRedPackageBean;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.type.ImType;
import com.google.gson.Gson;
import com.wd.daquan.R;
import com.wd.daquan.imui.adapter.viewholder.LeftRedPackageViewHolder;
import com.wd.daquan.imui.type.RedPackageStatus;

/**
 * 左侧红包内容填充
 * 个人和群组的布局是一样的，不过这里还是做下区分
 */
public class LeftRedPackageViewHolderBind extends BaseLeftViewHolderBind<LeftRedPackageViewHolder> {
    private Gson gson = new Gson();

    @Override
    public LifecycleObserver bindViewHolder(LeftRedPackageViewHolder leftRedPackageViewHolder, ImMessageBaseModel imMessageBaseModel, ImType chatType) {
        String content = imMessageBaseModel.getSourceContent();
        MessageRedPackageBean messageRedPackageBean = gson.fromJson(content, MessageRedPackageBean.class);

        if (ImType.P2P == chatType) {
            setP2PLeftTextData(leftRedPackageViewHolder, messageRedPackageBean);
        } else if (ImType.Team == chatType) {
            setTeamLeftTextData(leftRedPackageViewHolder, messageRedPackageBean);
        }
        return super.bindViewHolder(leftRedPackageViewHolder, imMessageBaseModel, chatType);
    }

    private void setP2PLeftTextData(LeftRedPackageViewHolder leftTextViewHolder, MessageRedPackageBean messageRedPackageBean){
        if (null == messageRedPackageBean){
            return;
        }
        String statusContent = RedPackageStatus.getRedPackageStatusDescription(messageRedPackageBean.getStatus());
        RedPackageStatus redPackageStatus = RedPackageStatus.getRedPackageStatus(messageRedPackageBean.getStatus());
        leftTextViewHolder.leftRedPackageStatus.setText(statusContent);
        switch (redPackageStatus){
            case RED_PACKAGE_UN_RECEIVE:
                leftTextViewHolder.leftRedPackageBg.setImageResource(R.mipmap.im_red_package_bubble_left_orange);
                leftTextViewHolder.leftRedPackageIcon.setImageResource(R.mipmap.im_red_package_no_open_icon);
                break;
            case RED_PACKAGE_RECEIVED:
                leftTextViewHolder.leftRedPackageBg.setImageResource(R.mipmap.im_red_package_bubble_left_grey);
                leftTextViewHolder.leftRedPackageIcon.setImageResource(R.mipmap.im_red_package_opened_icon);
                break;
        }
//        leftTextViewHolder.leftTextContent.setText(messageRedPackageBean.getDescription().concat(statusContent).concat("---->金额:").concat(messageRedPackageBean.getMoney()));
    }

    private void setTeamLeftTextData(LeftRedPackageViewHolder leftTextViewHolder, MessageRedPackageBean messageRedPackageBean){
        String statusContent = RedPackageStatus.getRedPackageStatusDescription(messageRedPackageBean.getStatus());
//        leftTextViewHolder.leftTextContent.setText(messageRedPackageBean.getDescription().concat(statusContent).concat("---->金额:").concat(messageRedPackageBean.getMoney()));
        RedPackageStatus redPackageStatus = RedPackageStatus.getRedPackageStatus(messageRedPackageBean.getStatus());
        leftTextViewHolder.leftRedPackageStatus.setText(statusContent);
        switch (redPackageStatus){
            case RED_PACKAGE_UN_RECEIVE:
                leftTextViewHolder.leftRedPackageBg.setImageResource(R.mipmap.im_red_package_bubble_left_orange);
                leftTextViewHolder.leftRedPackageIcon.setImageResource(R.mipmap.im_red_package_no_open_icon);
                break;
            case RED_PACKAGE_RECEIVED:
                leftTextViewHolder.leftRedPackageBg.setImageResource(R.mipmap.im_red_package_bubble_left_grey);
                leftTextViewHolder.leftRedPackageIcon.setImageResource(R.mipmap.im_red_package_opened_icon);
                break;
        }
    }

}