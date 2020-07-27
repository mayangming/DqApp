package com.wd.daquan.model.bean;

import java.io.Serializable;

public class UserDynamicCommentDataListBean implements Serializable {
    /**
     * commentId : 676021531263893504
     * dynamicId : 675977653500510208
     * userId : 492484774594609152
     * friendId : null
     * desc : 675977653500510208
     * createTime : 1595324046463
     * type : 0
     */

    private long commentId;
    private long dynamicId;
    private String userId;
    private String friendId;
    private String desc;
    private long createTime;
    private String type;
    private String userNick;
    private String userHead;
    private String friendNick;
    private String friendHead;

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public long getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(long dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public String getFriendNick() {
        return friendNick;
    }

    public void setFriendNick(String friendNick) {
        this.friendNick = friendNick;
    }

    public String getFriendHead() {
        return friendHead;
    }

    public void setFriendHead(String friendHead) {
        this.friendHead = friendHead;
    }

    @Override
    public String toString() {
        return "UserDynamicCommentDataListBean{" +
                "commentId=" + commentId +
                ", dynamicId=" + dynamicId +
                ", userId='" + userId + '\'' +
                ", friendId='" + friendId + '\'' +
                ", desc='" + desc + '\'' +
                ", createTime=" + createTime +
                ", type='" + type + '\'' +
                ", userNick='" + userNick + '\'' +
                ", userHead='" + userHead + '\'' +
                ", friendNick='" + friendNick + '\'' +
                ", friendHead='" + friendHead + '\'' +
                '}';
    }
}