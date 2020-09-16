package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 会员抢红包不中获得斗币
 */
public class VipVideoDBEntity implements Serializable {

    /**
     * id : 716507466430414848
     * tradTime : 1600150346031
     * tradeStatus : 0
     * tradeName : 视频奖励
     * tradeMoney : 50
     * userId : 580373971455705088
     */

    private long id;
    private long tradTime;
    private int tradeStatus;
    private String tradeName;
    private int tradeMoney;
    private String userId;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}