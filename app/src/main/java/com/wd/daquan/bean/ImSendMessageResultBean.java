package com.wd.daquan.bean;

/**
 * 发送IM消息后返回的数据结构
 */
public class ImSendMessageResultBean {

    /**
     * msgType : 02
     * msgId : 610570248009023488
     * type : 1
     * couponId : null //红包Id，类型为红包时候，这个ID才会有
     * msgSecondType : 1
     */

    private String msgType;
    private String msgId;
    private String type;
    private String couponId;
    private String msgSecondType;

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getMsgSecondType() {
        return msgSecondType;
    }

    public void setMsgSecondType(String msgSecondType) {
        this.msgSecondType = msgSecondType;
    }

    @Override
    public String toString() {
        return "ImSendMessageResultBean{" +
                "msgType='" + msgType + '\'' +
                ", msgId='" + msgId + '\'' +
                ", type='" + type + '\'' +
                ", couponId='" + couponId + '\'' +
                ", msgSecondType='" + msgSecondType + '\'' +
                '}';
    }
}