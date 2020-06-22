package com.wd.daquan.imui.bean.im;

import com.dq.im.model.IMContentDataModel;

import java.io.Serializable;

/**
 * 第一层Content
 */
public class FirstContentBean implements Serializable {
    private int messageUid = 0;//PC端需要的内容，客户端不需要修改
    private long messageId = System.currentTimeMillis();//PC端需要的内容，客户端不需要修改
    private int direction = 0;//PC端需要的内容，客户端不需要修改


    private String from = "";//当前用户ID
    private int status = 0;//固定传0
    private long timestamp = 0;//当前时间戳
    private String msgIdServer = "";//服务端ID
    private String msgIdClient = "";//客户端ID，通过客户端ID进行更新服务端ID
    private String conversationType = "0";//0 单聊 1：群聊 6：系统消息 7：消息回传内容
    private String target = "";//接收人ID
    private int line = 0;//固定传0
    //01：系统消息 02：文本消息 03：带链接文本消息 04：音频 05：图片 06：文件 07视频 08：红包 09：位置 10：名片 11：语音通话 12：视频通话 13：字符表情 14：匿名消息
    private int msgType = 0;//消息类型
    // 01:转账 02：红包 03：普通消息
    private String msgSecondType = "";//二级消息类型 0 单聊 1：群聊 6：系统消息
    private String groupId = "";//群组ID
    private String sourceContent = "";//消息内容，未解析的消息内容
    private IMContentDataModel content = new IMContentDataModel();

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMsgIdServer() {
        return msgIdServer;
    }

    public void setMsgIdServer(String msgIdServer) {
        this.msgIdServer = msgIdServer;
    }

    public String getConversationType() {
        return conversationType;
    }

    public void setConversationType(String conversationType) {
        this.conversationType = conversationType;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public IMContentDataModel getContent() {
        return content;
    }

    public void setContent(IMContentDataModel content) {
        this.content = content;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getMsgSecondType() {
        return msgSecondType;
    }

    public void setMsgSecondType(String msgSecondType) {
        this.msgSecondType = msgSecondType;
    }

    public String getSourceContent() {
        return sourceContent;
    }

    public void setSourceContent(String sourceContent) {
        this.sourceContent = sourceContent;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMsgIdClient() {
        return msgIdClient;
    }

    public void setMsgIdClient(String msgIdClient) {
        this.msgIdClient = msgIdClient;
    }

    @Override
    public String toString() {
        return "FirstContentBean{" +
                "from='" + from + '\'' +
                ", status=" + status +
                ", timestamp=" + timestamp +
                ", msgIdServer='" + msgIdServer + '\'' +
                ", msgIdClient='" + msgIdClient + '\'' +
                ", conversationType='" + conversationType + '\'' +
                ", target='" + target + '\'' +
                ", line=" + line +
                ", msgType='" + msgType + '\'' +
                ", msgSecondType='" + msgSecondType + '\'' +
                ", groupId='" + groupId + '\'' +
                ", sourceContent='" + sourceContent + '\'' +
                ", content=" + content +
                '}';
    }
}