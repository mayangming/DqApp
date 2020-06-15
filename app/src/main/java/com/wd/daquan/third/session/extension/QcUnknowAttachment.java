package com.wd.daquan.third.session.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * @author: dukangkang
 * @date: 2019/1/19 17:26.
 * @description: todo ...
 */
public class QcUnknowAttachment extends CustomAttachment {
    private String content;

    public QcUnknowAttachment() {
        super(CustomAttachmentType.DEFAULT);
    }

    @Override
    protected void parseData(JSONObject data) {
        content = data.toJSONString();
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = null;
        try {
            data = JSONObject.parseObject(content);
        } catch (Exception e) {

        }
        return data;
    }
}
