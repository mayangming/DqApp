package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 积分兑换明细
 */
public class IntegralExchangeEntity implements Serializable{
    private int id = 0;//条目ID
    private String name = "兑换充电宝";
    private int type = 0;//0 收入，1支出
    private long time = System.currentTimeMillis();//记录生成时间
    private int integralCount = 10;//积分数值

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getIntegralCount() {
        return integralCount;
    }

    public void setIntegralCount(int integralCount) {
        this.integralCount = integralCount;
    }
}