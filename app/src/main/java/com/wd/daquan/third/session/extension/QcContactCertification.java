package com.wd.daquan.third.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.wd.daquan.common.constant.KeyValue;

/**
 * 群认证
 * Created by Kind on 2019/1/22.
 */
public class QcContactCertification extends CustomAttachment {

    public String group_id;
    public String source_uid;
    public String source_nickname;
    public String message;


    public QcContactCertification() {
        super(CustomAttachmentType.CONTACT_MSG);
    }

    @Override
    protected void parseData(JSONObject data) {
        group_id = data.getString(KeyValue.CertificationMessage.GROUP_ID);
        source_uid = data.getString(KeyValue.CertificationMessage.SOURCE_UID);
        source_nickname = data.getString(KeyValue.CertificationMessage.SOURCE_NICKNAME);
        message = data.getString(KeyValue.CertificationMessage.MESSAGE);
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KeyValue.CertificationMessage.GROUP_ID, group_id);
        jsonObject.put(KeyValue.CertificationMessage.SOURCE_UID, source_uid);
        jsonObject.put(KeyValue.CertificationMessage.SOURCE_NICKNAME, source_nickname);
        jsonObject.put(KeyValue.CertificationMessage.MESSAGE, message);

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
}
