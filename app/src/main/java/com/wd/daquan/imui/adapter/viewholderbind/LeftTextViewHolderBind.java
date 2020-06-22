package com.wd.daquan.imui.adapter.viewholderbind;


import android.arch.lifecycle.LifecycleObserver;
import android.util.Log;

import com.da.library.tools.AESHelper;
import com.dq.im.bean.im.MessageTextBean;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.model.TeamMessageBaseModel;
import com.dq.im.type.ImType;
import com.google.gson.Gson;
import com.wd.daquan.imui.adapter.viewholder.LeftTextViewHolder;
import com.wd.daquan.util.AESUtil;

/**
 * 左侧文本内容填充
 * 个人和群组的布局是一样的，不过这里还是做下区分
 */
public class LeftTextViewHolderBind extends BaseLeftViewHolderBind<LeftTextViewHolder> {
    private Gson gson = new Gson();

    @Override
    public LifecycleObserver bindViewHolder(LeftTextViewHolder textViewHolder, ImMessageBaseModel imMessageBaseModel, ImType chatType) {
        if (ImType.P2P == chatType) {
            P2PMessageBaseModel p2PMessageBaseModel = (P2PMessageBaseModel) imMessageBaseModel;
            setP2PLeftTextData(textViewHolder, p2PMessageBaseModel);
        } else if (ImType.Team == chatType) {
            TeamMessageBaseModel teamMessageBaseModel = (TeamMessageBaseModel) imMessageBaseModel;
            setTeamLeftTextData(textViewHolder, teamMessageBaseModel);
        }
        return super.bindViewHolder(textViewHolder, imMessageBaseModel, chatType);
    }

    private void setP2PLeftTextData(LeftTextViewHolder leftTextViewHolder, P2PMessageBaseModel p2PMessageBaseModel){
        String content = p2PMessageBaseModel.getSourceContent();
        MessageTextBean messageTextBean = gson.fromJson(content,MessageTextBean.class);
//        String text = AESHelper.decryptString(messageTextBean.getDescription());
        String text = AESUtil.decode(messageTextBean.getDescription());
        leftTextViewHolder.leftTextContent.setText(text);
    }

    private void setTeamLeftTextData(LeftTextViewHolder leftTextViewHolder, TeamMessageBaseModel teamMessageBaseModel){
        String content = teamMessageBaseModel.getSourceContent();
        MessageTextBean messageTextBean = gson.fromJson(content,MessageTextBean.class);
//        String text = AESHelper.decryptString(messageTextBean.getDescription());
        String text = AESUtil.decode(messageTextBean.getDescription());
        leftTextViewHolder.leftTextContent.setText(text);
    }

}