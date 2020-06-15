package com.wd.daquan.third.session.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * 红包雨活动通知
 */
public class RedRainSystemAttachment extends CustomAttachment{

    /**
     * RedEnvelopeActiveNoticeEndTime : 2020-01-10 14:30:00
     * RedEnvelopeActiveNoticeStartTime : 2020-01-10 14:40:00
     * objectName : cloudRedEnvelopeActiveNoticeMsg
     * {"RedEnvelopeActiveNoticeEndTime":"2020-01-10 14:30:00","RedEnvelopeActiveNoticeStartTime":"2020-01-10 14:40:00","objectName":"cloudRedEnvelopeActiveNoticeMsg"}
     */

    private String RedEnvelopeActiveNoticeEndTime;
    private String RedEnvelopeActiveNoticeStartTime;
    private String objectName;
    private JSONObject data;


    public RedRainSystemAttachment() {
        super(CustomAttachmentType.QC_SYSTEM_RED_RAIN);
    }

    public String getRedEnvelopeActiveNoticeEndTime() {
        return RedEnvelopeActiveNoticeEndTime;
    }

    public void setRedEnvelopeActiveNoticeEndTime(String RedEnvelopeActiveNoticeEndTime) {
        this.RedEnvelopeActiveNoticeEndTime = RedEnvelopeActiveNoticeEndTime;
    }

    public String getRedEnvelopeActiveNoticeStartTime() {
        return RedEnvelopeActiveNoticeStartTime;
    }

    public void setRedEnvelopeActiveNoticeStartTime(String RedEnvelopeActiveNoticeStartTime) {
        this.RedEnvelopeActiveNoticeStartTime = RedEnvelopeActiveNoticeStartTime;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    @Override
    protected void parseData(JSONObject data) {
        RedEnvelopeActiveNoticeEndTime = data.getString("RedEnvelopeActiveNoticeEndTime");
        RedEnvelopeActiveNoticeStartTime = data.getString("RedEnvelopeActiveNoticeStartTime");
        objectName = data.getString("objectName");
        this.data = data;
    }

    @Override
    protected JSONObject packData() {
        return data;
    }

}