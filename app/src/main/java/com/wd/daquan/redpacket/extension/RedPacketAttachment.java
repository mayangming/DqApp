package com.wd.daquan.redpacket.extension;

import com.alibaba.fastjson.JSONObject;
import com.wd.daquan.third.session.extension.CustomAttachment;
import com.wd.daquan.third.session.extension.CustomAttachmentType;

public class RedPacketAttachment extends CustomAttachment {

    private String content;//  消息文本内容
    private String redPacketId;//  红包id
    private String title;// 红包名称
    private String amount;//金额

    private static final String KEY_CONTENT = "content";
    private static final String KEY_ID = "redPacketId";
    private static final String KEY_TITLE = "title";
    private static final String KEY_AMOUNT = "amount";

    public RedPacketAttachment() {
        super(CustomAttachmentType.JRMF_RDP_MSG);
    }

    @Override
    protected void parseData(JSONObject data) {
        content = data.getString(KEY_CONTENT);
        redPacketId = data.getString(KEY_ID);
        title = data.getString(KEY_TITLE);
        amount = data.getString(KEY_AMOUNT);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_CONTENT, content);
        data.put(KEY_ID, redPacketId);
        data.put(KEY_TITLE, title);
        data.put(KEY_AMOUNT, amount);
        return data;
    }

    public String getRpContent() {
        return content;
    }

    public String getRpId() {
        return redPacketId;
    }

    public String getRpTitle() {
        return title;
    }


    public void setRpContent(String content) {
        this.content = content;
    }

    public void setRpId(String briberyID) {
        this.redPacketId = briberyID;
    }

    public void setRpTitle(String briberyName) {
        this.title = briberyName;
    }

    @Override
    public String toString() {
        return "RedPacketAttachment{" +
                "content='" + content + '\'' +
                ", redPacketId='" + redPacketId + '\'' +
                ", title='" + title + '\'' +
                ", objectName='" + objectName + '\'' +
                '}';
    }
}
