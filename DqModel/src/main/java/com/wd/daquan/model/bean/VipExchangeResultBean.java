package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 兑换结果页
 */
public class VipExchangeResultBean implements Serializable{
    private int id = 0;
    private String exchangeName = "";//兑换商品的名字
    private int beExchangeNum = 0;//兑换的数量
    private int beExchangeDay = 0;//兑换的天数
    private String status = "";//兑换的状态
    private String createTime = "";//兑换的时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public int getBeExchangeNum() {
        return beExchangeNum;
    }

    public void setBeExchangeNum(int beExchangeNum) {
        this.beExchangeNum = beExchangeNum;
    }

    public int getBeExchangeDay() {
        return beExchangeDay;
    }

    public void setBeExchangeDay(int beExchangeDay) {
        this.beExchangeDay = beExchangeDay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}