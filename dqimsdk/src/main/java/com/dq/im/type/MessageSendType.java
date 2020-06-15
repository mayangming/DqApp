package com.dq.im.type;

/**
 * 消息发送状态
 */
public enum MessageSendType {
    SEND_LOADING(1),//发送中
    SEND_SUCCESS(2),//发送成功
    SEND_FAIL(3);//发送失败

    private int value;

    private MessageSendType(int var3) {
        this.value = var3;
    }

    public final int getValue() {
        return this.value;
    }

    public static MessageSendType typeOfValue(int var0) {
        MessageSendType[] var1;
        int var2 = (var1 = values()).length;
        for(int var3 = 0; var3 < var2; ++var3) {
            MessageSendType var4;
            if ((var4 = var1[var3]).getValue() == var0) {
                return var4;
            }
        }
        return SEND_SUCCESS;
    }
}