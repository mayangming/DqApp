package com.wd.daquan.third.session.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * @author: dukangkang
 * @date: 2018/9/10 14:08.
 * @description: todo ...
 */
public class CardAttachment extends CustomAttachment {
    public String userId;
    public String headPic;
    public String nickName;
    public String extra;

    public static final String KEY_USERID = "userId";
    public static final String KEY_NICKNAME = "nickName";
    public static final String KEY_HEADPIC = "headPic";
    public static final String KEY_EXTRA = "extra";

    public CardAttachment() {
        super(CustomAttachmentType.CARD_MSG);
    }

    @Override
    protected void parseData(JSONObject data) {
        userId = data.getString(KEY_USERID);
        nickName = data.getString(KEY_NICKNAME);
        headPic = data.getString(KEY_HEADPIC);
        extra = data.getString(KEY_EXTRA);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_USERID, userId);
        data.put(KEY_NICKNAME, nickName);
        data.put(KEY_HEADPIC, headPic);
        data.put(KEY_EXTRA, extra);
        return data;
    }

//    @Override
//    public String toString() {
//        return "CardAttachment{" +
//                "userId='" + userId + '\'' +
//                ", headPic='" + headPic + '\'' +
//                ", nickName='" + nickName + '\'' +
//                ", extra='" + extra + '\'' +
//                '}';
//    }
}
