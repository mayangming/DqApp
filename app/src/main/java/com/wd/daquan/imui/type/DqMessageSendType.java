package com.wd.daquan.imui.type;


/**
 * 斗圈的发送消息方式
 */
public enum DqMessageSendType {
    SEND_LOADING(3),//发送中
    SEND_SUCCESS(1),//发送成功
    SEND_FAIL(2);//发送失败

    private int value;

    private DqMessageSendType(int var3) {
        this.value = var3;
    }

    public final int getValue() {
        return this.value;
    }

    public static DqMessageSendType typeOfValue(int var0) {
        DqMessageSendType[] var1;
        int var2 = (var1 = values()).length;
        for(int var3 = 0; var3 < var2; ++var3) {
            DqMessageSendType var4;
            if ((var4 = var1[var3]).getValue() == var0) {
                return var4;
            }
        }
        return SEND_SUCCESS;
    }
}