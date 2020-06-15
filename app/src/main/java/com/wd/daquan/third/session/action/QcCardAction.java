package com.wd.daquan.third.session.action;

import android.content.Intent;

import com.da.library.tools.Utils;
import com.dq.im.bean.im.MessageCardBean;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.constant.RequestCode;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.model.bean.Friend;

/**
 * @author: dukangkang
 * @date: 2018/9/7 18:07.
 * @description: todo ...
 */
public class QcCardAction extends BaseAction {
    /**
     * 构造函数
     */
    public QcCardAction() {
        super(R.drawable.chat_expansion_card_selector, R.string.input_panel_card);
    }

    @Override
    public void onClick() {
        if (Utils.isFastDoubleClick(500)) {
            return;
        }
        NavUtils.gotoCardActivity(getActivity(), makeRequestCode(RequestCode.CARD_TEAM));

//        QcContactAttachment contactAttachment = new QcContactAttachment();
//        contactAttachment.content = "请求添加好友";
//        contactAttachment.type = "Request";
//        contactAttachment.sourceUserId = getAccount();
//        IMMessage content = MessageBuilder.createCustomMessage(getAccount(), getSessionType(), "添加好友", contactAttachment);
//        sendMessage(content);

//        QcContactNoticeAttachment contactNoticeAttachment = new QcContactNoticeAttachment();
//        contactNoticeAttachment.targetId = getAccount();
//        contactNoticeAttachment.targetNickname = "张三";
//        contactNoticeAttachment.content = "已添加你为好友";
//        IMMessage content = MessageBuilder.createCustomMessage(getAccount(), getSessionType(), "已添加成功", contactNoticeAttachment);
//        sendMessage(content);

//        QcRpNoticeAttachment rpNoticeAttachment = new QcRpNoticeAttachment();
//        rpNoticeAttachment.sendId = "123";
//        rpNoticeAttachment.nickName = "sendName";
//        rpNoticeAttachment.receiveName = "receiveName";
//        rpNoticeAttachment.receiveUid = "456";
//        IMMessage content = MessageBuilder.createCustomMessage(getAccount(), getSessionType(), "红包消息", rpNoticeAttachment);
//        sendMessage(content);

//        public String group_id;
//        public String source_uid;
//        public String source_nickname;
//        public String content;

//        QcTeamAuditAttachment teamAuditAttachment = new QcTeamAuditAttachment();
//        teamAuditAttachment.group_id = "111";
//        teamAuditAttachment.source_uid = "222";
//        teamAuditAttachment.source_nickname = "nickName";
//        teamAuditAttachment.content = "content";
//
//        IMMessage content = MessageBuilder.createCustomMessage(getAccount(), getSessionType(), "群认证消息", teamAuditAttachment);
//        sendMessage(content);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode.CARD_TEAM) {
            Friend friend = data.getParcelableExtra(KeyValue.Ait.ENTITY);
            if (friend == null) {
                return;
            }
            MessageCardBean cardBean = new MessageCardBean();
            cardBean.setHeadPic(friend.headpic);
            cardBean.setNickName(friend.getAitName());
            cardBean.setUserId(friend.uid);
            cardBean.setDqNum(friend.dq_num);
            sendMessage(cardBean);
        }
    }
}
