package com.wd.daquan.third.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.wd.daquan.common.constant.KeyValue;

/**
 * @author: dukangkang
 * @date: 2018/9/10 14:06.
 * @description: todo ...
 */
public class QcMultiportAttachment extends CustomAttachment {
    public String os;
    public String type;

    public QcMultiportAttachment() {
        super(CustomAttachmentType.QC_WEBLOGIN);
    }

    @Override
    protected void parseData(JSONObject data) {
        os = data.getString(KeyValue.WebMessage.OS);
        type = data.getString(KeyValue.WebMessage.TYPE);
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KeyValue.WebMessage.OS, os);
        jsonObj.put(KeyValue.WebMessage.TYPE, type);
        return jsonObj;
    }
}
