package com.dq.im.bean.im;

import com.dq.im.model.IMContentDataModel;

/**
 * 红包模型
 */
public class MessageRedPackageBean extends IMContentDataModel {
    public static int RED_PACKAGE_WX = 1;//微信
    public static int RED_PACKAGE_CHANGE = 2;//零钱
    private String couponId = "";//红包ID
    private String description = "";
    private String status = "01";// 红包状态 01：未领取 02：已领取 03：已领完 04：已退回 05：已过期
    private String money = "0";//红包金额
    private String count = "1";//红包数量
    private String fromUserId = "1";//红包数量
    private String type;//红包类型,1,个人 2:群组
    private int redType;//红包支付类型，从零钱支付还是从微信支付 1：零钱 2：微信
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public int getRedType() {
        return redType;
    }

    public void setRedType(int redType) {
        this.redType = redType;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    @Override
    public String toString() {
        return "MessageRedPackageBean{" +
                "couponId='" + couponId + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", money='" + money + '\'' +
                ", count='" + count + '\'' +
                ", type='" + type + '\'' +
                ", redType=" + redType +
                '}';
    }
}