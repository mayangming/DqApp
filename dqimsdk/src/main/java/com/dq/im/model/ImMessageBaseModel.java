package com.dq.im.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.dq.im.type.MessageSendType;

import java.io.Serializable;

/**
 * IM消息基础结构
 */
public class ImMessageBaseModel implements Serializable {
    @ColumnInfo(name = "msgIdClient")
    @NonNull
    protected String msgIdClient = "";//客户端消息ID

    @ColumnInfo(name = "msgIdServer")
    @NonNull
    protected String msgIdServer = "";//服务端消息ID

    @ColumnInfo(name = "type")
    @NonNull
    protected String type = "";//消息类型：1 单聊、2 群聊、3 心跳包 6、系统消息

    //01：系统消息 02：文本消息 03：带链接文本消息 04：音频 05：图片 06：文件 07视频 08：红包 09：位置 10：名片 11：语音通话 12：视频通话 13：字符表情 14：匿名消息
    @ColumnInfo(name = "msgType")
    @NonNull
    protected String msgType = "";

    //二级消息类型，该类型内容取决于msgType消息类型
    //01:转账 02：红包 03：普通消息
    @ColumnInfo(name = "msgSecondType")
    @NonNull
    protected String msgSecondType = "";

    @ColumnInfo(name = "createTime")
    @NonNull
    protected long createTime;//创建时间

    @ColumnInfo(name = "sourceContent")//原数据，不解析，解析后的值可以用 imContentModel
    @NonNull
    protected String sourceContent = "";

    @ColumnInfo(name = "messageSendStatus")//消息发送状态
    @NonNull
    protected int messageSendStatus = MessageSendType.SEND_SUCCESS.getValue();

    @ColumnInfo(name = "toUserId")
    @NonNull
    protected String toUserId = "";//消息发送给谁

    @ColumnInfo(name = "fromUserId")
    @NonNull
    protected String fromUserId = "";//消息从哪里发来的

    @Ignore
    protected IMContentDataModel contentData = new IMContentDataModel();//数据解析

//    @ColumnInfo(name = "signal")
//    protected String signal = "PUB_ACK";//PC端使用的字段，本地不需要处理
//
//    @ColumnInfo(name = "subSignal")
//    @NonNull
//    protected String subSignal = "MP";//PC端使用的字段，本地不需要处理
//
//    @ColumnInfo(name = "conversationType")
//    @NonNull
//    protected String conversationType = "";// PC端使用的字段，本地不需要处理
    @NonNull
    public String getMsgIdClient() {
        return msgIdClient;
    }

    public void setMsgIdClient(@NonNull String msgIdClient) {
        this.msgIdClient = msgIdClient;
    }

    @NonNull
    public String getMsgIdServer() {
        return msgIdServer;
    }

    public void setMsgIdServer(@NonNull String msgIdServer) {
        this.msgIdServer = msgIdServer;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    @NonNull
    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(@NonNull String msgType) {
        this.msgType = msgType;
    }

    @NonNull
    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(@NonNull long createTime) {
        this.createTime = createTime;
    }

    @NonNull
    public String getSourceContent() {
        return sourceContent;
    }

    public void setSourceContent(@NonNull String sourceContent) {
        this.sourceContent = sourceContent;
    }

    public int getMessageSendStatus() {
        return messageSendStatus;
    }

    public void setMessageSendStatus(int messageSendStatus) {
        this.messageSendStatus = messageSendStatus;
    }

    @NonNull
    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(@NonNull String toUserId) {
        this.toUserId = toUserId;
    }

    @NonNull
    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(@NonNull String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public IMContentDataModel getContentData() {
        return contentData;
    }

    public void setContentData(IMContentDataModel contentData) {
        this.contentData = contentData;
    }

    @NonNull
    public String getMsgSecondType() {
        return msgSecondType;
    }

    public void setMsgSecondType(@NonNull String msgSecondType) {
        this.msgSecondType = msgSecondType;
    }

//    public String getSignal() {
//        return signal;
//    }
//
//    public void setSignal(String signal) {
//        this.signal = signal;
//    }
//
//    public String getSubSignal() {
//        return subSignal;
//    }
//
//    public void setSubSignal(String subSignal) {
//        this.subSignal = subSignal;
//    }
//
//    public String getConversationType() {
//        return conversationType;
//    }
//
//    public void setConversationType(String conversationType) {
//        this.conversationType = conversationType;
//    }

    @Override
    public String toString() {
        return "ImMessageBaseModel{" +
                "msgIdClient='" + msgIdClient + '\'' +
                ", msgIdServer='" + msgIdServer + '\'' +
                ", type='" + type + '\'' +
                ", msgType='" + msgType + '\'' +
                ", msgSecondType='" + msgSecondType + '\'' +
                ", createTime='" + createTime + '\'' +
                ", sourceContent='" + sourceContent + '\'' +
                ", messageSendStatus=" + messageSendStatus +
                ", toUserId='" + toUserId + '\'' +
                ", fromUserId='" + fromUserId + '\'' +
                ", contentData=" + contentData +
                '}';
    }
}