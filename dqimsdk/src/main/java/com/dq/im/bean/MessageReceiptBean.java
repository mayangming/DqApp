package com.dq.im.bean;

/**
 * 消息回执数据模型
 * 当收到消息后将该消息id传递给服务器，表示客户端已经收到了消息
 */
public class MessageReceiptBean {
    private int type = 4;//消息状态
    private String msgIdServer = "";//消息ID

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsgIdServer() {
        return msgIdServer;
    }

    public void setMsgIdServer(String msgIdServer) {
        this.msgIdServer = msgIdServer;
    }
}