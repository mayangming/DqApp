package com.wd.daquan.model.bean;

/**
 * VIP列表
 */
public class VipCommodityEntity{
    private Integer id;

    private String vipCommodityId = "";//商品ID

    private String totalAmount = "";//实际金额，单位分

    private String showAmount = "";//展示金额，单位元

    private String subject = "";//标题

    private int vipDays;

    private String remark = "";//备注

    private boolean isSelected = false;//是否被选中

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVipCommodityId() {
        return vipCommodityId;
    }

    public void setVipCommodityId(String vipCommodityId) {
        this.vipCommodityId = vipCommodityId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getShowAmount() {
        return showAmount;
    }

    public void setShowAmount(String showAmount) {
        this.showAmount = showAmount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getVipDays() {
        return vipDays;
    }

    public void setVipDays(int vipDays) {
        this.vipDays = vipDays;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}