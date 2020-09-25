package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 每日签到金额
 */
public class SignAwardEntity implements Serializable {
    /**
     * id
     */
    private long id;

    /**
     * 连续签到天数
     */
    private  int day;

    /**
     * 签到奖励
     */
    private int dbaward;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDbaward() {
        return dbaward;
    }

    public void setDbaward(int dbaward) {
        this.dbaward = dbaward;
    }
}