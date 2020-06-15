package com.wd.daquan.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WithdrawRecordBean implements Serializable {

    private List<WithdrawRecordSubBean> recordSubBeans = new ArrayList<>();
    private Map<String,WithrawCountEntity> withrawCountEntityMap = new HashMap<>();//存储每个月的收入支出情况
    private long incomeAmount = 0;//收入金额,单位为分
    private long expenditureAmount = 0;//支出金额，单位为分
    private int year = 0;//年份
    private int month = 0;//月份
    private String recordDate = "";//每个月订单的集合,该值相同时认为是同一个月份的数据
    public List<WithdrawRecordSubBean> getRecordSubBeans() {
        return recordSubBeans;
    }

    public void setRecordSubBeans(List<WithdrawRecordSubBean> recordSubBeans) {
        this.recordSubBeans = recordSubBeans;
    }

    public long getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(long incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public long getExpenditureAmount() {
        return expenditureAmount;
    }

    public void setExpenditureAmount(long expenditureAmount) {
        this.expenditureAmount = expenditureAmount;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public Map<String, WithrawCountEntity> getWithrawCountEntityMap() {
        return withrawCountEntityMap;
    }

    public void setWithrawCountEntityMap(Map<String, WithrawCountEntity> withrawCountEntityMap) {
        this.withrawCountEntityMap = withrawCountEntityMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WithdrawRecordBean that = (WithdrawRecordBean) o;
        return recordDate.equals(that.recordDate);
    }

    @Override
    public int hashCode() {
        return recordDate.hashCode();
    }

    public WithdrawRecordSubBean getWithdrawRecordSubBean(){
        return new WithdrawRecordSubBean();
    }

    /**
     * 提现记录子集
     */
    public class WithdrawRecordSubBean implements Serializable{
        int id = 0;//id
        String uid = "";//用户ID
        String recordId = "";//斗圈ID
        String subject = "红包";
        String recordTime = "";//时间 格式为2020-01-03 12:22:00
        String recordTimeStr = "";//时间 格式为2020-01-02T06:00:59.000+0000
        String status = "";//状态 0初始新建状态  1success   2 微信返回出现未明确的错误码 用原商户订单号重试  3 提现申请失败
        String wxReturnCode = "";//微信支付返回code，具体含义不明
        String paymentNo = "";////微信转账单号
        String remark = "";//转账异常时候的原因
        long totalAmount = 20;//单位：分
        String recordType = "";//交易类型，0：存入 1：提现

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public long getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(long totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getRecordType() {
            return recordType;
        }

        public void setRecordType(String recordType) {
            this.recordType = recordType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public String getRecordTime() {
            return recordTime;
        }

        public void setRecordTime(String recordTime) {
            this.recordTime = recordTime;
        }

        public String getRecordTimeStr() {
            return recordTimeStr;
        }

        public void setRecordTimeStr(String recordTimeStr) {
            this.recordTimeStr = recordTimeStr;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getWxReturnCode() {
            return wxReturnCode;
        }

        public void setWxReturnCode(String wxReturnCode) {
            this.wxReturnCode = wxReturnCode;
        }

        public String getPaymentNo() {
            return paymentNo;
        }

        public void setPaymentNo(String paymentNo) {
            this.paymentNo = paymentNo;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}