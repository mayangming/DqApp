package com.wd.daquan.redpacket.extension;

import com.alibaba.fastjson.JSONObject;
import com.dq.im.type.ImType;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.third.helper.TeamHelper;
import com.wd.daquan.third.helper.UserInfoHelper;
import com.wd.daquan.third.session.extension.CustomAttachment;
import com.wd.daquan.third.session.extension.CustomAttachmentType;

public class RedPacketOpenedAttachment extends CustomAttachment {
    private String sendId; //发包人id
    private String sendName; //发包人名称
    private String openId; //拆包人id
    private String openName; //拆包人名称
    private String redPacketId;     //红包ID
    private String isLastOne;    //是否被领完 1已领完 0为领完
    private String type;    //红包类型

    private static final String KEY_SEND_ID = "sendId";
    private static final String KEY_SEND_NAME = "sendName";
    private static final String KEY_OPEN_ID = "openId";
    private static final String KEY_OPEN_NAME = "openName";
    private static final String KEY_RP_ID = "redPacketId";
    private static final String KEY_DONE = "isLastOne";
    private static final String KEY_TYPE = "type";

    public RedPacketOpenedAttachment() {
        super(CustomAttachmentType.JRMF_RDP_OPEN_MSG);
    }

    public String getSendNickName(ImType sessionType, String sessionId) {
        String uid = ModuleMgr.getCenterMgr().getUID();
        if (uid.equals(sendId) && uid.equals(openId)) {
            return "自己";
        }

//        String name = getDisplayName(sessionType, sessionId, sendId);
//        if(name) {
//
//        }

        return sendName;
    }

    public String getOpenNickName(ImType sessionType, String sessionId) {
//        return getDisplayName(sessionTypeEnum, targetId, openId);
        return openName;
    }

    // 我发的红包或者是我打开的红包
    public boolean belongTo(String account) {
        if (openId == null || sendId == null || account == null) {
            return false;
        }
        return openId.equals(account) || sendId.equals(account);
    }

    private String getDisplayName(ImType sessionTypeEnum, String targetId, String account) {
        if (sessionTypeEnum == ImType.Team) {
            return TeamHelper.getTeamMemberDisplayNameYou(targetId, account);
        } else if (sessionTypeEnum == ImType.P2P) {
            return UserInfoHelper.getUserDisplayNameEx(account, "你");
        } else {
            return "";
        }
    }

    public String getDesc(ImType sessionType, String sessionId) {
        String sender = getSendNickName(sessionType, sessionId);
        String opened = getOpenNickName(sessionType, sessionId);
        return String.format("%s领取了%s的红包", opened, sender);
    }

    public String getSendId() {
        return sendId;
    }

    private void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getOpenId() {
        return openId;
    }

    private void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getRedPacketId() {
        return redPacketId;
    }

    private void setRedPacketId(String redPacketId) {
        this.redPacketId = redPacketId;
    }

    public boolean isLastOne() {
        return "1".equals(isLastOne);
    }

    private void setIsGetDone(String isLastOne) {
        this.isLastOne = isLastOne;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    protected void parseData(JSONObject data) {
        sendId = data.getString(KEY_SEND_ID);
        sendName = data.getString(KEY_SEND_NAME);
        openId = data.getString(KEY_OPEN_ID);
        openName = data.getString(KEY_OPEN_NAME);
        redPacketId = data.getString(KEY_RP_ID);
        isLastOne = data.getString(KEY_DONE);
        type = data.getString(KEY_TYPE);
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObj = new JSONObject();

        jsonObj.put(KEY_SEND_ID, sendId);
        jsonObj.put(KEY_SEND_NAME, sendName);
        jsonObj.put(KEY_OPEN_ID, openId);
        jsonObj.put(KEY_OPEN_NAME, openName);
        jsonObj.put(KEY_RP_ID, redPacketId);
        jsonObj.put(KEY_DONE, isLastOne);
        jsonObj.put(KEY_TYPE, type);

        return jsonObj;
    }

    public static RedPacketOpenedAttachment obtain(String sendPacketId, String openPacketId, String packetId, String isGetDone) {
        RedPacketOpenedAttachment model = new RedPacketOpenedAttachment();
        model.setRedPacketId(packetId);
        model.setSendId(sendPacketId);
        model.setOpenId(openPacketId);
        model.setIsGetDone(isGetDone);
        return model;
    }

    public static RedPacketOpenedAttachment obtain(String sendPacketId, String openPacketId,
                   String packetId, String isGetDone, String type) {
        RedPacketOpenedAttachment model = new RedPacketOpenedAttachment();
        model.setRedPacketId(packetId);
        model.setSendId(sendPacketId);
        model.setOpenId(openPacketId);
        model.setIsGetDone(isGetDone);
        model.setType(type);
        return model;
    }
}
