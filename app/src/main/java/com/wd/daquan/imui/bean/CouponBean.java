package com.wd.daquan.imui.bean;

import java.io.Serializable;

public class CouponBean implements Serializable {
    /**
     * userId : 492486867409698816
     * couponId : 636959836336029696
     * money : 0.1
     * createTime : 1590667851287
     */

    private String userId;
    private String couponId;
    private String money;
    private long createTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}