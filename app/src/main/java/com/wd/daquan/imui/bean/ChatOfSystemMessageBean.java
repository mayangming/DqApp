package com.wd.daquan.imui.bean;

import com.dq.im.model.IMContentDataModel;

/**
 * 聊天中的系统消息
 */
public class ChatOfSystemMessageBean extends IMContentDataModel {
    private String description;//消息描述
    private String couponId;//红包Id

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    @Override
    public String toString() {
        return "ChatOfSystemMessageBean{" +
                "description='" + description + '\'' +
                ", couponId='" + couponId + '\'' +
                '}';
    }
}