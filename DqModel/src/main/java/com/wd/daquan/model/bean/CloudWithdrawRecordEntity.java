package com.wd.daquan.model.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 提现记录
 */
public class CloudWithdrawRecordEntity{
    private List<WithrawRecordEntity> recordResponseDtos = new ArrayList<>();//交易明细
    private List<WithrawCountEntity> monthIncomeStatis = new ArrayList<>();//支出金额
    private List<WithrawCountEntity> monthExpenditureStatis = new ArrayList<>();//收入金额

    public List<WithrawRecordEntity> getRecordResponseDtos() {
        return recordResponseDtos;
    }

    public void setRecordResponseDtos(List<WithrawRecordEntity> recordResponseDtos) {
        this.recordResponseDtos = recordResponseDtos;
    }

    public List<WithrawCountEntity> getMonthIncomeStatis() {
        return monthIncomeStatis;
    }

    public void setMonthIncomeStatis(List<WithrawCountEntity> monthIncomeStatis) {
        this.monthIncomeStatis = monthIncomeStatis;
    }

    public List<WithrawCountEntity> getMonthExpenditureStatis() {
        return monthExpenditureStatis;
    }

    public void setMonthExpenditureStatis(List<WithrawCountEntity> monthExpenditureStatis) {
        this.monthExpenditureStatis = monthExpenditureStatis;
    }
}