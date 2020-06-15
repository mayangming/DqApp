package com.wd.daquan.third.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.wd.daquan.common.constant.KeyValue;

/**
 * @author: dukangkang
 * @date: 2018/9/10 14:06.
 * @description: todo ...
 */
public class QcAssistantAttachment extends CustomAttachment {
    public String title;
    public String create_time;
    public String message;


    public QcAssistantAttachment() {
        super(CustomAttachmentType.QC_ASSISTANT_SYSTEM);
    }

    @Override
    protected void parseData(JSONObject data) {
        title = data.getString(KeyValue.AssistantSystem.TITLE);
        create_time = data.getString(KeyValue.AssistantSystem.CREATE_TIME);
        message = data.getString(KeyValue.AssistantSystem.MESSAGE);
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KeyValue.AssistantSystem.TITLE, title);
        jsonObj.put(KeyValue.AssistantSystem.CREATE_TIME, create_time);
        jsonObj.put(KeyValue.AssistantSystem.MESSAGE, message);
        return jsonObj;
    }

    public static QcAssistantAttachment obtain(String type, String title, String content, String thumb, String thumbData,
           String urlIntent, String appid, String appsecret, String appName, String appLogo, String backinfo, String packageName, String extra) {
        return new QcAssistantAttachment(type, title, content, thumb, thumbData, urlIntent, appid, appsecret, appName, appLogo, backinfo, packageName, extra);

    }

    public QcAssistantAttachment(String type, String title, String content, String thumb, String thumbData, String urlIntent,
                          String appid, String appsecret,
                          String appName, String appLogo, String backinfo, String packageName, String extra) {
        super(CustomAttachmentType.QC_ASSISTANT_SYSTEM);
    }
}
