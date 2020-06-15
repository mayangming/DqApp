package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * vip分享兑换人数
 */
public class ExchangeRecordBean implements Serializable{

    /**
     * uid : 492486867409698816
     * shareTotalNum : 0
     * convertibilityNum : 0
     * surplusNum : 0
     */

    private String uid = "";
    private int shareTotalNum;//累计分享
    private int convertibilityNum;//已分享
    private int surplusNum;//剩余可兑换

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getShareTotalNum() {
        return shareTotalNum;
    }

    public void setShareTotalNum(int shareTotalNum) {
        this.shareTotalNum = shareTotalNum;
    }

    public int getConvertibilityNum() {
        return convertibilityNum;
    }

    public void setConvertibilityNum(int convertibilityNum) {
        this.convertibilityNum = convertibilityNum;
    }

    public int getSurplusNum() {
        return surplusNum;
    }

    public void setSurplusNum(int surplusNum) {
        this.surplusNum = surplusNum;
    }
}