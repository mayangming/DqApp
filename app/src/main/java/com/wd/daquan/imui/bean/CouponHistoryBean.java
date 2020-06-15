package com.wd.daquan.imui.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 红包历史信息
 */
public class CouponHistoryBean implements Serializable {


    /**
     * tioCoupon : {"couponId":"636959836336029696","fromUserId":"630885476110172160","toUserId":null,"toGroupId":null,"status":"03","payStatus":"1","money":"0.1","hasMoney":"0.0","count":1,"hasCount":0,"type":null,"description":null,"createTime":1590667529562,"prepayId":null,"couponPayType":"2"}
     * couponHistory : [{"userId":"492486867409698816","couponId":"636959836336029696","money":"0.1","createTime":1590667851287}]
     * luckKingDto : {"userId":"492486867409698816","money":"0.1"}
     */

    private TioCouponBean tioCoupon;
    private LuckKingDtoBean luckKingDto;
    private List<CouponBean> couponHistory;

    public TioCouponBean getTioCoupon() {
        return tioCoupon;
    }

    public void setTioCoupon(TioCouponBean tioCoupon) {
        this.tioCoupon = tioCoupon;
    }

    public LuckKingDtoBean getLuckKingDto() {
        return luckKingDto;
    }

    public void setLuckKingDto(LuckKingDtoBean luckKingDto) {
        this.luckKingDto = luckKingDto;
    }

    public List<CouponBean> getCouponHistory() {
        return couponHistory;
    }

    public void setCouponHistory(List<CouponBean> couponHistory) {
        this.couponHistory = couponHistory;
    }

    public static class TioCouponBean {
        /**
         * couponId : 636959836336029696
         * fromUserId : 630885476110172160
         * toUserId : null
         * toGroupId : null
         * status : 03
         * payStatus : 1
         * money : 0.1
         * hasMoney : 0.0
         * count : 1
         * hasCount : 0
         * type : null
         * description : null
         * createTime : 1590667529562
         * prepayId : null
         * couponPayType : 2
         */

        private String couponId;
        private String fromUserId;
        private Object toUserId;
        private Object toGroupId;
        private String status;
        private String payStatus;
        private String money;
        private String hasMoney;
        private int count;
        private int hasCount;
        private String type;
        private String description;
        private long createTime;
        private Object prepayId;
        private String couponPayType;

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

        public Object getToUserId() {
            return toUserId;
        }

        public void setToUserId(Object toUserId) {
            this.toUserId = toUserId;
        }

        public Object getToGroupId() {
            return toGroupId;
        }

        public void setToGroupId(Object toGroupId) {
            this.toGroupId = toGroupId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(String payStatus) {
            this.payStatus = payStatus;
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

        public Object getPrepayId() {
            return prepayId;
        }

        public void setPrepayId(Object prepayId) {
            this.prepayId = prepayId;
        }

        public String getCouponPayType() {
            return couponPayType;
        }

        public void setCouponPayType(String couponPayType) {
            this.couponPayType = couponPayType;
        }
    }

    public static class LuckKingDtoBean {
        /**
         * userId : 492486867409698816
         * money : 0.1
         */

        private String userId;
        private String money;

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
    }
}