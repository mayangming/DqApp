package com.wd.daquan.model.bean;

/**
 * 点赞朋友圈的结果
 */
public class SaveUserDynamicLikeBean {

    /**
     * likeId : 676016665929973760
     * dynamicId : 675977653500510208
     * userId : 492484774594609152
     * createTime : 1595323466470
     */

    private long likeId;
    private long dynamicId;
    private String userId;
    private long createTime;

    public long getLikeId() {
        return likeId;
    }

    public void setLikeId(long likeId) {
        this.likeId = likeId;
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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}