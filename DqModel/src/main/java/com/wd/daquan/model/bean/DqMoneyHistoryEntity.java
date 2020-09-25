package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 斗币明细列表
 */
public class DqMoneyHistoryEntity implements Serializable{

    /**
     * id : 713514864324116480
     * tradTime : 1599793600193
     * tradeStatus : 1
     * tradeName : 兑换测试任务
     * tradeMoney : 100
     */

    private long id;
    private long tradTime;
    private int tradeStatus;
    private String tradeName;
    private int tradeMoney;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTradTime() {
        return tradTime;
    }

    public void setTradTime(long tradTime) {
        this.tradTime = tradTime;
    }

    public int getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(int tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public int getTradeMoney() {
        return tradeMoney;
    }

    public void setTradeMoney(int tradeMoney) {
        this.tradeMoney = tradeMoney;
    }
}