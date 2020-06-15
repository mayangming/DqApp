package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 提现记录页，数据返回
 * 该数据需要处理成 WithdrawRecordBean才能进行展示使用
 */
public class WithrawRecordEntity implements Serializable{

    /**
     * id : 24
     * recordId : 530234316110692352
     * uid : 492655210338975744
     * subject : 斗圈活动红包
     * totalAmount : 100
     * recordType : 0
     * recordTime : 2020-01-02T06:00:59.000+0000
     * status : 1
     * wxReturnCode : SUCCESS
     * paymentNo : null
     * remark : null
     */

    private int id;
    private String recordId;
    private String uid;
    private String subject;
    private int totalAmount;
    private String recordType;// 0存入  1提现
    private String recordTime;//时间 格式为2020-01-03 12:22:00
    private String recordTimeStr;//时间 格式为2020-01-02T06:00:59.000+0000
    private String status;//状态 0初始新建状态  1success   2 微信返回出现未明确的错误码 用原商户订单号重试  3 提现申请失败
    private String wxReturnCode;
    private String paymentNo;//微信转账单号
    private String remark;//转账异常时候的原因

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
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

    public String getRecordTimeStr() {
        return recordTimeStr;
    }

    public void setRecordTimeStr(String recordTimeStr) {
        this.recordTimeStr = recordTimeStr;
    }
}