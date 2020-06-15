package com.dq.im.model;

/**
 * 通知消息模型
 */
public class ImNotificationModel extends IMContentDataModel{
    private String linkUrl = "";// 链接内容
    private String title = "";// 主标题
    private String description = "";//描述内容

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

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
}