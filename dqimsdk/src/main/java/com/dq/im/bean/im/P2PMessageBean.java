package com.dq.im.bean.im;

import com.google.gson.JsonElement;

/**
 * 单聊数据基础模型
 * 该结构是仅仅是作为展示的数据模型。不是数据库的模型
 */
public class P2PMessageBean{
    private String type = "";
    private String msgType = "";
    private String fromUserId = "";
    private String toUserId = "";
    private String msgIdClient = "";//客户端的消息ID
    private String msgIdServer = "";//服务端的消息ID
    private String createTime = "";//消息创建时间，本地会生成一个时间，在消息发送成功后会更新为服务器接收的时间
    private JsonElement contentData;//该字段不解析

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public JsonElement getContentData() {
        return contentData;
    }

    public void setContentData(JsonElement contentData) {
        this.contentData = contentData;
    }

    public String getMsgIdClient() {
        return msgIdClient;
    }

    public void setMsgIdClient(String msgIdClient) {
        this.msgIdClient = msgIdClient;
    }

    public String getMsgIdServer() {
        return msgIdServer;
    }

    public void setMsgIdServer(String msgIdServer) {
        this.msgIdServer = msgIdServer;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "P2PMessageBean{" +
                "type='" + type + '\'' +
                ", msgType='" + msgType + '\'' +
                ", fromUserId='" + fromUserId + '\'' +
                ", toUserId='" + toUserId + '\'' +
                ", msgIdClient='" + msgIdClient + '\'' +
                ", msgIdServer='" + msgIdServer + '\'' +
                ", createTime='" + createTime + '\'' +
                ", contentData=" + contentData +
                '}';
    }
}