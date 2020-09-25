package com.wd.daquan.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 斗币交易详情
 */
public class DqMoneyDetailEntity implements Serializable {
    private ArrayList<DqMoneyHistoryEntity> list = new ArrayList<>();
    private int totalMoney;

    public ArrayList<DqMoneyHistoryEntity> getList() {
        return list;
    }

    public void setList(ArrayList<DqMoneyHistoryEntity> list) {
        this.list = list;
    }

    public int getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(int totalMoney) {
        this.totalMoney = totalMoney;
    }
}