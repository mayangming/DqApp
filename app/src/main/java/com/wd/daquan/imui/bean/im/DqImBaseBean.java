package com.wd.daquan.imui.bean.im;

import java.io.Serializable;
import java.util.UUID;

/**
 * 斗圈IM基础通信格式
 */
public class DqImBaseBean implements Serializable {
    private String signal = "PUB_ACK";
    private String subSignal = "MP";
    private long messageId = System.currentTimeMillis();
    private String userId = "";
    private String token = "";
    private String msgIdServer = "";
    private FirstContentBean content = new FirstContentBean();

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }

    public String getSubSignal() {
        return subSignal;
    }

    public void setSubSignal(String subSignal) {
        this.subSignal = subSignal;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public FirstContentBean getContent() {
        return content;
    }

    public void setContent(FirstContentBean content) {
        this.content = content;
    }

    public String getMsgIdServer() {
        return msgIdServer;
    }

    public void setMsgIdServer(String msgIdServer) {
        this.msgIdServer = msgIdServer;
    }
}