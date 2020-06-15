package com.wd.daquan.third.session.extension;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.utils.GsonUtils;

import java.util.List;

/**
 * @author: dukangkang
 * @date: 2019/2/20 10:47.
 * @description: todo ...
 */
public class QcPokeAttachment extends CustomAttachment {

    // 用户ID
    public String senderId = "";
    // 用户名称
    public String senderName = "";
    // 群组ID
    public String targetId = "";
    // 群组名称
    public String targetName = "";
    // 用户头像或群组头像
    public String portraitUrl = "";
    // 消息内容
    public String content;
    // 如果是群组，群组成员ID列表
    public List<String> uidListString;
    // 预留其他字段扩展
    public String extra;

    public QcPokeAttachment() {
        super(CustomAttachmentType.QC_POKE);
    }

    @Override
    protected void parseData(JSONObject data) {
        senderId = data.getString(KeyValue.PokeMessage.SENDER_ID);
        senderName = data.getString(KeyValue.PokeMessage.SENDER_NAME);
        targetId = data.getString(KeyValue.PokeMessage.TARGET_ID);
        targetName = data.getString(KeyValue.PokeMessage.TARGET_NAME);
        portraitUrl = data.getString(KeyValue.PokeMessage.PORTRAIT_URL);
        content = data.getString(KeyValue.PokeMessage.CONTENT);
        String temp = data.getString(KeyValue.PokeMessage.UID_LIST);
        uidListString = GsonUtils.fromJsonList(temp, String.class);
        extra = data.getString(KeyValue.PokeMessage.EXTRA);
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KeyValue.PokeMessage.SENDER_ID, senderId);
            jsonObject.put(KeyValue.PokeMessage.SENDER_NAME, senderName);
            jsonObject.put(KeyValue.PokeMessage.TARGET_ID, targetId);
            jsonObject.put(KeyValue.PokeMessage.TARGET_NAME, targetName);
            jsonObject.put(KeyValue.PokeMessage.PORTRAIT_URL, portraitUrl);
            jsonObject.put(KeyValue.PokeMessage.CONTENT, content);
            String target = GsonUtils.toJson(uidListString);
            jsonObject.put(KeyValue.PokeMessage.UID_LIST, target);
            jsonObject.put(KeyValue.PokeMessage.EXTRA, extra);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public boolean isEmpty() {
        if (uidListString == null) {
            return true;
        }

        if (uidListString.size() == 0) {
            return true;
        }

        return false;
    }

    /**
     * 是否包含当前用户
     * @param id
     * @return
     */
    public boolean contains(String id) {
        if (uidListString == null) {
            return false;
        }

        for (String uid : uidListString) {
            if (TextUtils.isEmpty(uid)) {
                continue;
            }
            if (uid.equals(id)) {
                return true;
            }
        }
        return false;
    }
}
