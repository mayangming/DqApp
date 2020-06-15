package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * @author: dukangkang
 * @date: 2019/2/25 18:03.
 * @description:
 * 处理加好友／邀请进群
 */
public class UnreadNotifyEntity implements Serializable {
    // 所有未读消息通知
    public static String UNREAD_ALL = "unread_all";
    // 添加好友
    public static String UNREAD_ADD_FRIEND = "unread_add_friend";
    // 邀请进群
    public static String UNREAD_TEAM_INVITE = "unread_team_invite";
    public String type;
    public String uid;
}
