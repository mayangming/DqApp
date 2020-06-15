package com.wd.daquan.redpacket.extension;

import com.alibaba.fastjson.JSONObject;
import com.wd.daquan.third.session.extension.CustomAttachment;
import com.wd.daquan.third.session.extension.CustomAttachmentType;

public class RdpRefundAttachment extends CustomAttachment {

    public String type;//0金融魔方红包
    public String tradeAmount;//交易金额
    public String tradeno;//订单号
    public String tradeStatus;//订单状态
    public String returnTime;//返款时间
    public String returnType;//返款类型
    public String returnCause;//返款原因

    private static final String KEY_TYPE = "type";
    private static final String KEY_TRADE_AMOUNT = "tradeAmount";
    private static final String KEY_TRADE_NO = "tradeno";
    private static final String KEY_TRADE_STATUS = "tradeStatus";
    private static final String KEY_RETURN_TIME = "returnTime";
    private static final String KEY_RETURN_TYPE = "returnType";
    private static final String KEY_RETURN_CAUSE = "returnCause";

    public RdpRefundAttachment() {
        super(CustomAttachmentType.RDP_REFUND_MSG);
    }

    @Override
    protected void parseData(JSONObject data) {
        type = data.getString(KEY_TYPE);
        tradeAmount = data.getString(KEY_TRADE_AMOUNT);
        tradeno = data.getString(KEY_TRADE_NO);
        tradeStatus = data.getString(KEY_TRADE_STATUS);
        returnTime = data.getString(KEY_RETURN_TIME);
        returnType = data.getString(KEY_RETURN_TYPE);
        returnCause = data.getString(KEY_RETURN_CAUSE);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_TYPE, type);
        data.put(KEY_TRADE_AMOUNT, tradeAmount);
        data.put(KEY_TRADE_NO, tradeno);
        data.put(KEY_TRADE_STATUS, tradeStatus);
        data.put(KEY_RETURN_TIME, returnTime);
        data.put(KEY_RETURN_TYPE, returnType);
        data.put(KEY_RETURN_CAUSE, returnCause);

        return data;
    }

    @Override
    public String toString() {
        return "RdpRefundAttachment{" +
                "type='" + type + '\'' +
                ", tradeAmount='" + tradeAmount + '\'' +
                ", tradeno='" + tradeno + '\'' +
                ", tradeStatus='" + tradeStatus + '\'' +
                ", returnTime='" + returnTime + '\'' +
                ", returnType='" + returnType + '\'' +
                ", returnCause='" + returnCause + '\'' +
                '}';
    }
}
