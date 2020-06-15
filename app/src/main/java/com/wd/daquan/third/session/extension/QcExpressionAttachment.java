package com.wd.daquan.third.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.wd.daquan.common.constant.KeyValue;

/**
 * @author: dukangkang
 * @date: 2018/9/11 18:31.
 * @description: todo ...
 */
public class QcExpressionAttachment extends CustomAttachment {

    public String id;
    public String remote_path;
    public String filecode;
    public int pixW;
    public int pixH;
    public String extra;

    public QcExpressionAttachment() {
        super(CustomAttachmentType.QC_EXPRESSION);
    }

    @Override
    protected void parseData(JSONObject data) {
        id = data.getString(KeyValue.ExpressionMessage.ID);
        remote_path = data.getString(KeyValue.ExpressionMessage.REMOTE_PATH);
        filecode = data.getString(KeyValue.ExpressionMessage.FILECODE);
        pixH = data.getInteger(KeyValue.ExpressionMessage.PIXH);
        pixW = data.getInteger(KeyValue.ExpressionMessage.PIXW);
        extra = data.getString(KeyValue.ExpressionMessage.EXTRA);
    }

    @Override
    protected JSONObject packData() {
       JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KeyValue.ExpressionMessage.ID, id);
            jsonObject.put(KeyValue.ExpressionMessage.REMOTE_PATH, remote_path);
            jsonObject.put(KeyValue.ExpressionMessage.FILECODE, filecode);
            jsonObject.put(KeyValue.ExpressionMessage.PIXH, pixH);
            jsonObject.put(KeyValue.ExpressionMessage.PIXW, pixW);
            jsonObject.put(KeyValue.ExpressionMessage.EXTRA, extra);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
