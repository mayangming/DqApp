package com.wd.daquan.model.mgr;

import com.wd.daquan.model.bean.UnreadNotifyEntity;
import com.wd.daquan.model.rxbus.ModuleBase;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

//import com.netease.nimlib.sdk.StatusBarNotificationConfig;

public class AppManager implements ModuleBase, QCObserver {

    // 处理未读消息提醒（添加好友／邀请进群）
    public volatile LinkedHashSet<UnreadNotifyEntity> sList = new LinkedHashSet<>();
    // 未读消息数量
    private int messageUnread = 0;
//    // 消息通知配置
//    private StatusBarNotificationConfig notificationConfig;

    public void addFriendUnread(String uid) {
        UnreadNotifyEntity entity = new UnreadNotifyEntity();
        entity.type = UnreadNotifyEntity.UNREAD_ADD_FRIEND;
        entity.uid = uid;
        addUnreadNotify(entity);
    }

    public void addTeamInviteUnread(String uid) {
        UnreadNotifyEntity entity = new UnreadNotifyEntity();
        entity.type = UnreadNotifyEntity.UNREAD_TEAM_INVITE;
        entity.uid = uid;
        addUnreadNotify(entity);
    }

    /**
     * 添加到未读消息通知队列中
     * @param entity
     */
    private void addUnreadNotify(UnreadNotifyEntity entity) {
        if (!isContains(entity.uid)) {
            sList.add(entity);
            ModuleMgr.getCenterMgr().saveNotifyList(sList);
        }
    }

    public boolean isContainTeamInvite() {
        for(UnreadNotifyEntity entity: sList) {
            if (UnreadNotifyEntity.UNREAD_TEAM_INVITE.equals(entity.type)) {
                return true;
            }
        }
        return false;
    }

    public boolean isContainAddFriend() {
        for(UnreadNotifyEntity entity: sList) {
            if (UnreadNotifyEntity.UNREAD_ADD_FRIEND.equals(entity.type)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否包含当前UID
     * @param uid
     * @return
     */
    private boolean isContains(String uid) {
        for(UnreadNotifyEntity entity: sList) {
            if (entity.uid.equals(uid)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 清除指定未读通知类型
     * @param type
     */
    public void clearUnreadNotify(String type) {
        List<UnreadNotifyEntity> tmpList = new ArrayList<>();
        for (UnreadNotifyEntity entity: sList) {
            if (type.equals(entity.type)) {
                tmpList.add(entity);
            }
        }

        sList.removeAll(tmpList);

        ModuleMgr.getCenterMgr().saveNotifyList(sList);
    }

    public int getUnreadNotifyCount() {
        return sList.size();
    }

    public LinkedHashSet<UnreadNotifyEntity> getUnreadNotifyList() {
        if (sList.size() <= 0) {
            sList.clear();
            List<UnreadNotifyEntity> list = ModuleMgr.getCenterMgr().getNotifyList();
            if (list != null) {
                sList.addAll(list);
            }
        }
        return sList;
    }

    public int getMessageUnread() {
        return messageUnread;
    }

    public void setMessageUnread(int messageUnread) {
        this.messageUnread = messageUnread;
    }

//
//    public void setNotificationConfig(StatusBarNotificationConfig notificationConfig) {
//        this.notificationConfig = notificationConfig;
//    }

    @Override
    public void init() {
        MsgMgr.getInstance().attach(this);
    }

    @Override
    public void release() {
        MsgMgr.getInstance().detach(this);
    }

    @Override
    public void onMessage(String key, Object value) {
        if (key.equals(MsgType.MT_App_Login)) {
            if (!(Boolean) value) {//登录成功
                logout();
            }
        }
    }

    private void logout() {
//        sLinkedHashSet.clear();
        sList.clear();
        messageUnread = 0;
    }
}
