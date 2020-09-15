package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 红包内容
 */
public class RedEnvelopBean implements Serializable{

    /**
     * flag : true
     * amount : 100
     */

    private boolean flag;//是否成功
    private int amount;//红包金额，单位分
    private int isBoom;// 是否暴击 0不是 1是

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean getIsBoom() {
        return isBoom == 1;
    }

    public void setIsBoom(int isBoom) {
        this.isBoom = isBoom;
    }
}