package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 微信支付的参数
 */
public class WxPayBody implements Serializable{
    private String couponId = "";//红包ID,这个ID
    private String appid = "";//微信APPID
    private String nonce_str = "";//微信随机数
    private String mch_id = "";//商户号
    private String prepay_id = "";//订单号
    private String sign = "";//微信签名
    private long timestamp = 0;//时间戳
    private String trade_type = "";//交易类型

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    @Override
    public String toString() {
        return "WxPayBody{" +
                "appid='" + appid + '\'' +
                ", nonce_str='" + nonce_str + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", prepay_id='" + prepay_id + '\'' +
                ", sign='" + sign + '\'' +
                ", timestamp=" + timestamp +
                ", trade_type='" + trade_type + '\'' +
                '}';
    }
}