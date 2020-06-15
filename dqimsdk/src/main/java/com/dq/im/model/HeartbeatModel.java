package com.dq.im.model;

/**
 * 心跳包数据内容
 */
public class HeartbeatModel{
    private String device_model = "";//设备型号
    private String device_version = "";//设备系统版本
    private String device_id = "";//设备Id
    private String device_phone_iccd = "";//手机卡唯一ID，这里只获取主卡的
    private String user_id = "";//用户ID
    private int type = 3;//消息类型，类型为3，心跳包

    public String getDevice_model() {
        return device_model;
    }

    public void setDevice_model(String device_model) {
        this.device_model = device_model;
    }

    public String getDevice_version() {
        return device_version;
    }

    public void setDevice_version(String device_version) {
        this.device_version = device_version;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_phone_iccd() {
        return device_phone_iccd;
    }

    public void setDevice_phone_iccd(String device_phone_iccd) {
        this.device_phone_iccd = device_phone_iccd;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}