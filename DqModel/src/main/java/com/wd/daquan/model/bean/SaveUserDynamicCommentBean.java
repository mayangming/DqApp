package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 评论朋友圈的内容
 */
public class SaveUserDynamicCommentBean implements Serializable {

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
}