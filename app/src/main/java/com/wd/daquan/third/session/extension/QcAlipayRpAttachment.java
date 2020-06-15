package com.wd.daquan.third.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.wd.daquan.common.constant.KeyValue;

/**
 * @author: dukangkang
 * @date: 2018/9/12 15:19.
 * @description:
 *  支付宝红包
 */
public class QcAlipayRpAttachment extends CustomAttachment {
    public String type;
    public String redpacketId;
    public String redpacket_extra;
    public String blessing;
    public String receiveId;
    public String receiveName;
    public String receivePic;
    public String sendId;
    public String sendName;
    public String sendPic;
    public String signature;
    public String isClientSend;
    public String resendSign; // 重发红包，1为重发
    public String extra;

    public QcAlipayRpAttachment() {
        super(CustomAttachmentType.QC_ALIPAY_REDPACKET);
    }

    @Override
    protected void parseData(JSONObject data) {
        type = data.getString(KeyValue.RedPacketMessage.TYPE);
        blessing = data.getString(KeyValue.RedPacketMessage.BLESSING);
        redpacketId = data.getString(KeyValue.RedPacketMessage.REDPACKET_ID);
        redpacket_extra = data.getString(KeyValue.RedPacketMessage.REDPACKET_EXTRA);
        receiveId = data.getString(KeyValue.RedPacketMessage.RECEIVE_ID);
        receiveName = data.getString(KeyValue.RedPacketMessage.RECEIVE_NAME);
        receivePic = data.getString(KeyValue.RedPacketMessage.RECEIVE_PIC);
        sendId = data.getString(KeyValue.RedPacketMessage.SEND_ID);
        sendName = data.getString(KeyValue.RedPacketMessage.SEND_NAME);
        sendPic = data.getString(KeyValue.RedPacketMessage.SEND_PIC);
        signature = data.getString(KeyValue.RedPacketMessage.SIGNATURE);
        isClientSend = data.getString(KeyValue.RedPacketMessage.IS_CLIENT_SEND);
        resendSign = data.getString(KeyValue.RedPacketMessage.RESEND_SIGN);
        extra = data.getString(KeyValue.RedPacketMessage.EXTRA);

    }

    @Override
    protected JSONObject packData() {
       JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KeyValue.RedPacketMessage.TYPE, type);
            jsonObject.put(KeyValue.RedPacketMessage.BLESSING, blessing);
            jsonObject.put(KeyValue.RedPacketMessage.REDPACKET_ID, redpacketId);
            jsonObject.put(KeyValue.RedPacketMessage.REDPACKET_EXTRA, redpacket_extra);
            jsonObject.put(KeyValue.RedPacketMessage.RECEIVE_ID, receiveId);
            jsonObject.put(KeyValue.RedPacketMessage.RECEIVE_NAME, receiveName);
            jsonObject.put(KeyValue.RedPacketMessage.RECEIVE_PIC, receivePic);
            jsonObject.put(KeyValue.RedPacketMessage.SEND_ID, sendId);
            jsonObject.put(KeyValue.RedPacketMessage.SEND_NAME, sendName);
            jsonObject.put(KeyValue.RedPacketMessage.SEND_PIC, sendPic);
            jsonObject.put(KeyValue.RedPacketMessage.SIGNATURE, signature);
            jsonObject.put(KeyValue.RedPacketMessage.IS_CLIENT_SEND, isClientSend);
            jsonObject.put(KeyValue.RedPacketMessage.RESEND_SIGN, resendSign);
            jsonObject.put(KeyValue.RedPacketMessage.EXTRA, extra);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String getSendName() {
        return sendName == null ? "" : sendName;
    }

    public String getSendPic() {
        return sendPic == null ? "" : sendPic;
    }

    public String getExtra() {
        return extra == null ? "" : extra;
    }

    public String getBlessing() {
        return blessing == null ? "" : blessing;
    }

    public String getSendId() {
        return  sendId == null ? "" : sendId;
    }
}
