package com.dq.im.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.annotation.NonNull;

/**
 * 群组IM消息模型
 */

@Entity(tableName = "team_message",primaryKeys = {"msgIdClient","msgIdServer"})
public class TeamMessageBaseModel extends ImMessageBaseModel{

    @ColumnInfo(name = "groupId")
    @NonNull
    private String groupId = "1";

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

    @NonNull
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(@NonNull String groupId) {
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
        return "TeamMessageBaseModel{" +
                "groupId='" + groupId + '\'' +
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