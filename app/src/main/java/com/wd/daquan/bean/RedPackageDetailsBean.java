package com.wd.daquan.bean;

/**
 * 红包详情模型
 */
//{
//        "couponId": "610576153664552960",
//        "fromUserId": "W",
//        "toUserId": "Q",
//        "toGroupId": null,
//        "status": "01",
//        "money": "15",
//        "hasMoney": "15",
//        "count": 0,
//        "hasCount": 0,
//        "type": null,
//        "description": "",
//        "creatTime": 1587522349495
//        }
public class RedPackageDetailsBean {

    /**
     * couponId : 610576153664552960
     * fromUserId : W
     * toUserId : Q
     * toGroupId : null
     * status : 01
     * money : 15
     * hasMoney : 15
     * count : 0
     * hasCount : 0
     * type : null
     * description :
     * creatTime : 1587522349495
     */

    private String couponId;
    private String fromUserId;
    private String toUserId;
    private String toGroupId;
    private String status;
    private String money;
    private String hasMoney;
    private int count;
    private int hasCount;
    private String type;
    private String description;
    private long createTime;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getToGroupId() {
        return toGroupId;
    }

    public void setToGroupId(String toGroupId) {
        this.toGroupId = toGroupId;
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

    public String getHasMoney() {
        return hasMoney;
    }

    public void setHasMoney(String hasMoney) {
        this.hasMoney = hasMoney;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getHasCount() {
        return hasCount;
    }

    public void setHasCount(int hasCount) {
        this.hasCount = hasCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "RedPackageDetailsBean{" +
                "couponId='" + couponId + '\'' +
                ", fromUserId='" + fromUserId + '\'' +
                ", toUserId='" + toUserId + '\'' +
                ", toGroupId='" + toGroupId + '\'' +
                ", status='" + status + '\'' +
                ", money='" + money + '\'' +
                ", hasMoney='" + hasMoney + '\'' +
                ", count=" + count +
                ", hasCount=" + hasCount +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}