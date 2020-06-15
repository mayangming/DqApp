package com.wd.daquan.model.bean;

public class VipExchangeItemBean {
    private String title = "7天VIP会员";
    private String count = "-10人数";

    public VipExchangeItemBean() {
    }

    public VipExchangeItemBean(String title, String count) {
        this.title = title;
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}