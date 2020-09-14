package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 兑换记录
 */
public class DqGoodChangeEntity implements Serializable {
    private String userId = "";//用户ID
    private String userName = "";//用户名字
    private long commoditiesId = 0;//商品Id
    private int commoditiesNum = 0;//商品数量
    private int commoditiesPrice = 0;//商品价格
    private long changeTime  = 0;//兑换时间
    private String commoditiesNmae = "";//商品名称

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}