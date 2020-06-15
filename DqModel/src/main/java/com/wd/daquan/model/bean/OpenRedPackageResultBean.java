package com.wd.daquan.model.bean;

/**
 * 打开红包的结果信息
 */
public class OpenRedPackageResultBean {
    private String couponId = "";
    private String userId = "";
    private String money = "";
    private String createTime = "";
    private String status = "";//红包状态

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OpenRedPackageResultBean{" +
                "couponId='" + couponId + '\'' +
                ", userId='" + userId + '\'' +
                ", money='" + money + '\'' +
                ", createTime='" + createTime + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}