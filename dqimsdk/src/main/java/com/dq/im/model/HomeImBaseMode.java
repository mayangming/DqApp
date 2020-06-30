package com.dq.im.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;


/**
 * 首页聊天消息模型，主要记录首页的最后一条消息，以及其未读消息数
 */
@Entity(tableName = "home_message",primaryKeys = {"msgIdClient","msgIdServer"})
public class HomeImBaseMode extends ImMessageBaseModel{

    @ColumnInfo(name = "unReadNumber")
    @NonNull
    private transient int unReadNumber = 0;//未读消息数

    @ColumnInfo(name = "groupId")
    private transient String groupId = "";//群组ID
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

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

    public int getUnReadNumber() {
        return unReadNumber;
    }

    public void setUnReadNumber(int unReadNumber) {
        this.unReadNumber = unReadNumber;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @NonNull
    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(@NonNull long createTime) {
        this.createTime = createTime;
    }


    @Override
    public String toString() {
        return "HomeImBaseMode{" +
                "unReadNumber=" + unReadNumber +
                ", groupId='" + groupId + '\'' +
                ", msgIdClient='" + msgIdClient + '\'' +
                ", msgIdServer='" + msgIdServer + '\'' +
                ", type='" + type + '\'' +
                ", msgType='" + msgType + '\'' +
                ", createTime='" + createTime + '\'' +
                ", sourceContent='" + sourceContent + '\'' +
                ", messageSendStatus=" + messageSendStatus +
                ", toUserId='" + toUserId + '\'' +
                ", fromUserId='" + fromUserId + '\'' +
                ", contentData=" + contentData +
                '}';
    }
}