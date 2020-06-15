package com.wd.daquan.third.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.wd.daquan.common.constant.KeyValue;

/**
 * type=0好友添加成功
 */
public class UserSysTemAttachment extends CustomAttachment {
    public String type;
    public String to_uid;
    public String to_nickname;
    public String content;

    public UserSysTemAttachment() {
        super(CustomAttachmentType.USER_SYSTEM_MSG);
    }

    @Override
    protected void parseData(JSONObject data) {
        type = data.getString(KeyValue.UserSystemMessage.TYPE);
        to_uid = data.getString(KeyValue.UserSystemMessage.TO_UID);
        to_nickname = data.getString(KeyValue.UserSystemMessage.TO_NICKNAME);
        content = data.getString(KeyValue.UserSystemMessage.CONTENT);
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KeyValue.UserSystemMessage.TYPE, type);
        jsonObject.put(KeyValue.UserSystemMessage.TO_UID, to_uid);
        jsonObject.put(KeyValue.UserSystemMessage.TO_NICKNAME, to_nickname);
        jsonObject.put(KeyValue.UserSystemMessage.CONTENT, content);

        return jsonObject;
    }

//    public String getContentSummary(boolean isCurUser) {
//        String nickName = "";
//        Friend mFriend = FriendDbHelper.getInstance().getFriend(targetId);
//        if (mFriend != null) {
//            if (TextUtils.isEmpty(mFriend.getRemarks())) {
//                nickName = mFriend.getName();
//            } else {
//                nickName = mFriend.getRemarks();
//            }
//        } else {
//            nickName = targetNickname;
//        }
//        return nickName + " " + content;
//    }

    /**
     * 添加好友
     */
    public Boolean isAddFriendOk(){
        return "0".equals(type);
    }
}
