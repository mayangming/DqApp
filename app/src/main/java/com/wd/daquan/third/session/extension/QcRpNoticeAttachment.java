package com.wd.daquan.third.session.extension;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.db.helper.MemberDbHelper;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.bean.GroupMemberBean;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.Friend;

/**
 * @author: dukangkang
 * @date: 2018/9/16 10:47.
 * @description: todo ...
 */
public class QcRpNoticeAttachment extends CustomAttachment {
    public String redpacketId;
    public String sendId;
    public String nickName;
    public String headpic;
    public String greetings;
    public String isSurplus;
    public String receiveName;
    public String receiveUid = "";
    public String type;
    public String extra;
    public String group_id;
    public String signature;

    public QcRpNoticeAttachment() {
        super(CustomAttachmentType.QC_REDPACKET_NOTICE);
    }

    @Override
    protected void parseData(JSONObject data) {
        redpacketId = data.getString(KeyValue.RpNoticeMessage.REDPACKET_ID);
        sendId = data.getString(KeyValue.RpNoticeMessage.SEND_ID);
        nickName = data.getString(KeyValue.RpNoticeMessage.NICKNAME);
        headpic = data.getString(KeyValue.RpNoticeMessage.HEADPIC);
        greetings = data.getString(KeyValue.RpNoticeMessage.GREETINGS);
        isSurplus = data.getString(KeyValue.RpNoticeMessage.ISSURPLUS);
        receiveName = data.getString(KeyValue.RpNoticeMessage.RECEIVE_NAME);
        receiveUid = data.getString(KeyValue.RpNoticeMessage.RECEIVE_UID);
        type = data.getString(KeyValue.RpNoticeMessage.TYPE);
        group_id = data.getString(KeyValue.RpNoticeMessage.GROUP_ID);
        signature = data.getString(KeyValue.RpNoticeMessage.SIGNATURE);
        extra = data.getString(KeyValue.RpNoticeMessage.EXTRA);
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("redpacketId", redpacketId);
            jsonObject.put("sendId", sendId);
            jsonObject.put("nickName", nickName);
            jsonObject.put("headpic", headpic);
            jsonObject.put("greetings", greetings);
            jsonObject.put("isSurplus", isSurplus);
            jsonObject.put("receiveName", receiveName);
            jsonObject.put("receiveUid", receiveUid);
            jsonObject.put("type", type);
            jsonObject.put("group_id", group_id);
            jsonObject.put("signature", signature);
            jsonObject.put("extra", extra);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String getContentSummary(QcRpNoticeAttachment qcRpNoticeAttachment) {
        String desc = "";
        if (sendId.equals(ModuleMgr.getCenterMgr().getUID())) {
            if (receiveUid.equals(ModuleMgr.getCenterMgr().getUID())) {
                String sendName = getSenderName(qcRpNoticeAttachment);
                desc = "你领取了" + sendName + "的红包";
            } else {
                String receiveName = getReceiveName(qcRpNoticeAttachment);
                desc = receiveName + "领取了您的红包";
            }
            return desc;
        } else {
            String senName = getSenderName(qcRpNoticeAttachment);
            desc = "你领取了" + senName + "的红包";
            return desc;
        }
    }
    //1.如果是好友，取出好友备注，2.非好友, 检查群昵称，如果用使用群昵称，没有则使用发送者原昵称
    private String getSenderName(QcRpNoticeAttachment content) {
        String nickName;
        Friend mFriend = FriendDbHelper.getInstance().getFriend(content.sendId);
        if (mFriend != null) {
            if (TextUtils.isEmpty(mFriend.getRemarks())) {
                nickName = sendGroup(content);
            } else {
                String remar = mFriend.getRemarks();
                nickName = substringName(remar);
            }
        } else {
            nickName = sendGroup(content);
        }
        return nickName;
    }

    @NonNull
    private String substringName(String remar) {
        String nickName = "";
        if(!TextUtils.isEmpty(remar)) {
            if (remar.length() > 6) {
                nickName = remar.substring(0, 6) + "..";
            } else {
                nickName = remar;
            }
        }
        return nickName;
    }

    private String sendGroup(QcRpNoticeAttachment content) {
        String nickName;
        if (TextUtils.isEmpty(content.group_id)) {
            String nic = content.nickName;
            nickName = substringName(nic);
        } else {
            GroupMemberBean userInfos =  MemberDbHelper.getInstance().getTeamMember(content.group_id, content.sendId);
            if (userInfos != null) {
                String nic = userInfos.getNickname();
                nickName = substringName(nic);
            } else {
                String nic = content.nickName;
                nickName = substringName(nic);
            }
        }
        return nickName;
    }
    //1.如果是好友，取出好友备注，2.非好友, 检查群昵称，如果用使用群昵称，没有则使用发送者原昵称
    private String getReceiveName(QcRpNoticeAttachment content) {
        String receiveName;
        Friend mFriends = FriendDbHelper.getInstance().getFriend(content.receiveUid);
        if (mFriends != null) {
            if (TextUtils.isEmpty(mFriends.getRemarks())) {
                receiveName = receiveGroup(content);
            } else {
                String str = mFriends.getRemarks();
                receiveName = substringName(str);
            }
        } else {
            receiveName = receiveGroup(content);
        }
        return receiveName;
    }
    private String receiveGroup(QcRpNoticeAttachment content) {
        String receiveName;
        if (TextUtils.isEmpty(content.group_id)) {
            receiveName = substringName(content.receiveName);
        } else {
            GroupMemberBean userInfos = MemberDbHelper.getInstance().getTeamMember(content.group_id, content.receiveUid);
            if (userInfos != null) {
                String nic = userInfos.getNickname();
                receiveName = substringName(nic);
            } else {
                receiveName = substringName(content.receiveName);
            }
        }
        return receiveName;
    }
}
