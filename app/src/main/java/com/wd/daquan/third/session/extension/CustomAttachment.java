package com.wd.daquan.third.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.dq.im.model.IMContentDataModel;

/**
 * Created by zhoujianghua on 2015/4/9.
 */
public abstract class CustomAttachment extends IMContentDataModel {

    // 区分自定义消息类型字段
    protected String objectName;

    public CustomAttachment(String type) {
        this.objectName = type;
    }

    public void fromJson(JSONObject data) {
        if (data != null) {
            parseData(data);
        }
    }

    @Override
    public String toJson(boolean send) {
        return CustomAttachParser.packData(objectName, packData());
    }

    public String getType() {
        return objectName;
    }

    protected abstract void parseData(JSONObject data);

    protected abstract JSONObject packData();


}
