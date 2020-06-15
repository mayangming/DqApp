package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 提现统计
 */
public class WithrawCountEntity implements Serializable{
    private String month = "";//格式为2020-10
    private long sumTotalAmount = 0;//金额，这个是服务器返回的
    private long income = 0;//收入，这个用于展示
    private long expenditure = 0;//支出，这个用于展示

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public long getIncome() {
        return income;
    }

    public void setIncome(long income) {
        this.income = income;
    }

    public long getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(long expenditure) {
        this.expenditure = expenditure;
    }

    public long getSumTotalAmount() {
        return sumTotalAmount;
    }

    public void setSumTotalAmount(long sumTotalAmount) {
        this.sumTotalAmount = sumTotalAmount;
    }
}