package com.wd.daquan.third.session.extension;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.wd.daquan.common.constant.KeyValue;

/**
 * @author: dukangkang
 * @date: 2018/9/16 10:33.
 * @description:
 * 添加好友
 */
public class QcContactAttachment extends CustomAttachment {
    public String operation;
    public String sourceUserId;
    public String targetUserId;
    public String message;
    public String extra;

    public QcContactAttachment() {
        super(CustomAttachmentType.CONTACT_APPLY_MSG);
    }

    @Override
    protected void parseData(JSONObject data) {
        operation = data.getString(KeyValue.ContactMessage.OPERATION);
        sourceUserId = data.getString(KeyValue.ContactMessage.SOURCE_USERID);
        targetUserId = data.getString(KeyValue.ContactMessage.TARGET_USERID);
        message = data.getString(KeyValue.ContactMessage.MESSAGE);
        extra = data.getString(KeyValue.ContactMessage.EXTRA);
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KeyValue.ContactMessage.OPERATION, this.operation);
        jsonObj.put(KeyValue.ContactMessage.SOURCE_USERID, this.sourceUserId);
        jsonObj.put(KeyValue.ContactMessage.TARGET_USERID, this.targetUserId);
        if (!TextUtils.isEmpty(this.message)) {
            jsonObj.put(KeyValue.ContactMessage.MESSAGE, this.message);
        }

        if (!TextUtils.isEmpty(extra)) {
            jsonObj.put(KeyValue.ContactMessage.EXTRA, extra);
        }
        return jsonObj;
    }

    public String getContentSummary(boolean isCurUser) {
        if ("Request".equals(operation)) {
            return "test + " + message;
        }
        return "";
    }
}
