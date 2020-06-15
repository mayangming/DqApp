package com.netease.nim.uikit.common.util.notification;

import com.dq.im.type.ImType;
import com.wd.daquan.chat.helper.MuteHelper;

/**
 * 免打扰拦截器
 * */
public class NotifyNotificationFilter implements NotificationFilter{
    private String accountId;//来源Id
    private ImType messageType;//消息类型，群消息或者是单人消息

    public NotifyNotificationFilter(String accountId, ImType messageType) {
        this.accountId = accountId;
        this.messageType = messageType;
    }

    @Override
    public boolean filter() {
        if (ImType.P2P == messageType){
            boolean isNotify = MuteHelper.getInstance().isNeedMessageNotify(accountId);
            return !isNotify;
        }else if (ImType.Team == messageType){
//            return MuteHelper.getInstance().isGroupNotify(accountId);
            boolean isNotify = MuteHelper.getInstance().isGroupNotify(accountId);
            return !isNotify;
        }else {
            return false;
        }
    }
}