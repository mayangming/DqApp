package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 签到记录
 */
public class SignUserEntity implements Serializable{
    /**
     * 签到记录Id
     */
    private long id;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 签到时间
     */
    private long signTime;
    /**
     * 签到次数
     */
    private int signNum;
    /**
     * 斗币余额
     */
    private int dbMoney;

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

    public long getSignTime() {
        return signTime;
    }

    public void setSignTime(long signTime) {
        this.signTime = signTime;
    }

    public int getSignNum() {
        return signNum;
    }

    public void setSignNum(int signNum) {
        this.signNum = signNum;
    }

    public int getDbMoney() {
        return dbMoney;
    }

    public void setDbMoney(int dbMoney) {
        this.dbMoney = dbMoney;
    }
}