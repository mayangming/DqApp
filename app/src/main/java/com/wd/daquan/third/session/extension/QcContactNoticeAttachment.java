package com.wd.daquan.third.session.extension;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.Friend;

/**
 * @author: dukangkang
 * @date: 2018/9/16 10:42.
 * @description: todo ...
 */
public class QcContactNoticeAttachment extends CustomAttachment {
    public String targetId;
    public String targetNickname;
    public String message;

    public QcContactNoticeAttachment() {
        super(CustomAttachmentType.CONTACT_MSG);
    }

    @Override
    protected void parseData(JSONObject data) {
        targetId = data.getString(KeyValue.ContactMessage.TARGET_ID);
        targetNickname = data.getString(KeyValue.ContactMessage.TARGET_NICKNAME);
        message = data.getString(KeyValue.ContactMessage.MESSAGE);
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KeyValue.ContactMessage.TARGET_ID, targetId);
        jsonObject.put(KeyValue.ContactMessage.TARGET_NICKNAME, targetNickname);
        jsonObject.put(KeyValue.ContactMessage.MESSAGE, message);
        return jsonObject;
    }

    public String getContentSummary(boolean isCurUser) {
        String nickName = "";
        Friend mFriend = FriendDbHelper.getInstance().getFriend(targetId);
        if (mFriend != null) {
            if (TextUtils.isEmpty(mFriend.getRemarks())) {
                nickName = mFriend.getName();
            } else {
                nickName = mFriend.getRemarks();
            }
        } else {
            nickName = targetNickname;
        }
        return nickName + " " + message;
    }
}
