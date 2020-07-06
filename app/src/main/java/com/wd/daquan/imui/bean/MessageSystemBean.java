package com.wd.daquan.imui.bean;

import com.dq.im.model.IMContentDataModel;

/**
 * 删除好友的系统通知消息
 */
public class MessageSystemBean extends IMContentDataModel {
    private String title;//消息标题
    private String description ;//消息描述
    private String operator = "";// 操作人id
    private String groupId ;// 群id
    private String fromUserId;//消息来源
    private String toUserId;//消息目的地

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    @Override
    public String toString() {
        return "MessageSystemBean{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", operator='" + operator + '\'' +
                ", groupId='" + groupId + '\'' +
                ", fromUserId='" + fromUserId + '\'' +
                ", toUserId='" + toUserId + '\'' +
                '}';
    }
}