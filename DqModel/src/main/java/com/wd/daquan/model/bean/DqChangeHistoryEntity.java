package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 积分兑换记录
 */
public class DqChangeHistoryEntity implements Serializable {

    /**
     * id : 713647747139371008
     * userId : 630885476110172160
     * commoditiesId : 712924267364220928
     * commoditiesNum : 1
     * commoditiesPrice : 100
     * changeTime : 1599809440889
     * commoditiesNmae : 测试任务
     * userName : 嘿嘿嘿123
     */

    private long id;
    private String userId;
    private long commoditiesId;
    private int commoditiesNum;
    private int commoditiesPrice;
    private long changeTime;
    private String commoditiesNmae;
    private String userName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getCommoditiesId() {
        return commoditiesId;
    }

    public void setCommoditiesId(long commoditiesId) {
        this.commoditiesId = commoditiesId;
    }

    public int getCommoditiesNum() {
        return commoditiesNum;
    }

    public void setCommoditiesNum(int commoditiesNum) {
        this.commoditiesNum = commoditiesNum;
    }

    public int getCommoditiesPrice() {
        return commoditiesPrice;
    }

    public void setCommoditiesPrice(int commoditiesPrice) {
        this.commoditiesPrice = commoditiesPrice;
    }

    public long getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(long changeTime) {
        this.changeTime = changeTime;
    }

    public String getCommoditiesNmae() {
        return commoditiesNmae;
    }

    public void setCommoditiesNmae(String commoditiesNmae) {
        this.commoditiesNmae = commoditiesNmae;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}