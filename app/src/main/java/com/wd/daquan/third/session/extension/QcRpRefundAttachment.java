package com.wd.daquan.third.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.wd.daquan.common.constant.KeyValue;

/**
 * @author: dukangkang
 * @date: 2019/1/19 13:47.
 * @description: todo ...
 */
public class QcRpRefundAttachment extends CustomAttachment {
    public String title;//标题
    public String start_time;//到账时间
    public String amount;//退换金额
    public String reason;//退款原因
    public String remak;//备注
    public String end_time;//退还时间
    public String type;
    public String bankname;//银行
    public String cardno;//卡号
    public String create_nickname;
    public String receipt_id;

    public QcRpRefundAttachment() {
        super(CustomAttachmentType.QC_REDPACKET_REFUND_NOTICE);
    }

    @Override
    protected void parseData(JSONObject data) {
        title = data.getString(KeyValue.RpRefundMessage.TITLE);
        start_time = data.getString(KeyValue.RpRefundMessage.START_TIME);
        amount = data.getString(KeyValue.RpRefundMessage.AMOUNT);
        reason = data.getString(KeyValue.RpRefundMessage.REASON);
        remak = data.getString(KeyValue.RpRefundMessage.REMAK);
        end_time = data.getString(KeyValue.RpRefundMessage.END_TIME);
        type = data.getString(KeyValue.RpRefundMessage.TYPE);
        bankname = data.getString(KeyValue.RpRefundMessage.BANKNAME);
        cardno = data.getString(KeyValue.RpRefundMessage.CARDNO);
        create_nickname = data.getString(KeyValue.RpRefundMessage.CREATE_NICKNAME);
        receipt_id = data.getString(KeyValue.RpRefundMessage.RECEIPT_ID);
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KeyValue.RpRefundMessage.TITLE, title);
            jsonObject.put(KeyValue.RpRefundMessage.START_TIME, start_time);
            jsonObject.put(KeyValue.RpRefundMessage.AMOUNT, amount);
            jsonObject.put(KeyValue.RpRefundMessage.REASON, reason);
            jsonObject.put(KeyValue.RpRefundMessage.REMAK, remak);
            jsonObject.put(KeyValue.RpRefundMessage.END_TIME, end_time);
            jsonObject.put(KeyValue.RpRefundMessage.TYPE, type);
            jsonObject.put(KeyValue.RpRefundMessage.BANKNAME, bankname);
            jsonObject.put(KeyValue.RpRefundMessage.CARDNO, cardno);
            jsonObject.put(KeyValue.RpRefundMessage.CREATE_NICKNAME, create_nickname);
            jsonObject.put(KeyValue.RpRefundMessage.RECEIPT_ID, receipt_id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String getContentSummary(QcRpRefundAttachment refundAttachment) {
        if (refundAttachment == null) {
            return "";
        }
        return refundAttachment.title;
    }
}
