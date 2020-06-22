package com.dq.im.type;

/**
 * 聊天类型，分为
 * 单聊类型
 * 群聊类型
 * 系统消息类型
 */
public enum ImType{
    P2P("1"),
    Team("2"),
    System("6"),
    RECEIVE_MESSAGE_CALLBACK("7");

    private String value;

    private ImType(String var3) {
        this.value = var3;
    }

    public final String getValue() {
        return this.value;
    }

    public static ImType typeOfValue(String var0) {
        ImType[] var1;
        int var2 = (var1 = values()).length;
        for(int var3 = 0; var3 < var2; ++var3) {
            ImType var4;
            if ((var4 = var1[var3]).getValue().equals(var0)) {
                return var4;
            }
        }
        return P2P;
    }
}