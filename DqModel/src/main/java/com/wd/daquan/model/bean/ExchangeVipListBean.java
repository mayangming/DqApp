package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 获取vip兑换列表
 */
public class ExchangeVipListBean implements Serializable{

    /**
     * id : 1
     * exchangeName : 分享兑换7天VIP
     * beExchangeNum : 10
     * beExchangeDay : 7
     * status : 0
     * createTime : 2019-12-25T02:45:43.000+0000
     */

    private int id;//兑换商品的ID
    private String exchangeName;//兑换商品的名字
    private int beExchangeNum;//兑换商品的数量
    private int beExchangeDay;//兑换商品的天数
    private String status;//兑换商品的状态
    private String createTime;//兑换商品的时间

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