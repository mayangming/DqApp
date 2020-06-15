package com.wd.daquan.chat.group.holder;

import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.da.library.tools.DateUtil;
import com.dq.im.model.ImMessageBaseModel;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.chat.group.adapter.holder.BaseSearchChatMessageHolder;
import com.wd.daquan.chat.group.bean.SearchChatBean;
import com.wd.daquan.common.utils.SpannableStringUtils;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.third.session.extension.QcAlipayRpAttachment;

/**
 * @Author: 方志
 * @Time: 2019/5/13 19:37
 * @Description:
 */
public class SearchGroupTradeHolder extends BaseSearchChatMessageHolder {
    private ImageView mSenderAvatar;
    private TextView mSenderTime;
    private TextView mSenderName;
    private TextView mRdpDesc;
    private TextView mRdpState;
    private String input;

    public SearchGroupTradeHolder(View itemView) {
        super(itemView);
        mSenderAvatar = itemView.findViewById(R.id.item_group_search_message_sender_avatar);
        mSenderName = itemView.findViewById(R.id.item_group_search_message_sender_name);
        mSenderTime = itemView.findViewById(R.id.item_group_search_message_sender_time);
        mRdpDesc = itemView.findViewById(R.id.item_group_search_message_rdp_desc);
        mRdpState = itemView.findViewById(R.id.item_group_search_message_rdp_state);
    }

    @Override
    public void bindData(SearchChatBean item, int position) {
        if(item == null) {
            return;
        }
        super.bindData(item, position);
        ImMessageBaseModel imMessage = item.imMessage;
        if(imMessage == null) {
            return;
        }
        QcAlipayRpAttachment attachment = (QcAlipayRpAttachment) imMessage.getContentData();
        if(attachment == null) {
          return;
        }
        mSenderName.setText(setDisplayName(item, attachment));
        mSenderTime.setText(DateUtil.timeToString(imMessage.getCreateTime()));
        GlideUtils.loadHeader(mSenderAvatar.getContext(), attachment.getSendPic(), mSenderAvatar, R.drawable.team_avatar);
        mRdpDesc.setText(changeTextColor(attachment.getBlessing()));
        mRdpState.setText(TextUtils.isEmpty(attachment.getExtra()) ?
                DqApp.getStringById(R.string.group_search_chat_list_geted_red)
                : DqApp.getStringById(R.string.group_search_chat_list_get_red));

    }

    private String setDisplayName(SearchChatBean item, QcAlipayRpAttachment attachment) {
        return TextUtils.isEmpty(item.getRemark()) ? attachment.getSendName() : item.getRemark();
    }

    private SpannableString changeTextColor(String content){
        SpannableString changeTitle;
        if(!TextUtils.isEmpty(content) && content.contains(input)){
            changeTitle = SpannableStringUtils.addTextColor(content, content.indexOf(input), input.length() + content.indexOf(input),
                    DqApp.getColorById(R.color.app_theme_color));
        }else{
            changeTitle = new SpannableString(content);
        }
        return changeTitle;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
