package com.wd.daquan.imui.type;

/**
 * 二级消息类型
 */
public enum MsgSecondType{
    MSG_SECOND_TYPE_UN_KNOWN("未知消息","-99"),
    MSG_SECOND_TYPE_TRANSFER("转账消息","01"),
    MSG_SECOND_TYPE_RED_PACKAGE("红包消息","02"),
    MSG_SECOND_TYPE_NORMAL("普通消息","03"),
    MSG_SECOND_TYPE_RED_RECEIVE("红包被领取","04");

    public String description;
    public String type;
    MsgSecondType(String description,String type){
        this.description = description;
        this.type = type;
    }

    public static String getMsgSecondTypeDescription(String status){
        String description = "未定义红包类型";
        for (MsgSecondType redPackageStatus : values()){
            if (redPackageStatus.type.equals(status)){
                description = redPackageStatus.description;
                break;
            }
        }
        return description;
    }

    public static MsgSecondType getMsgSecondTypeByValue(String type){
        MsgSecondType msgSecondType = MSG_SECOND_TYPE_UN_KNOWN;
        for (MsgSecondType temp : values()){
            if (type.equals(temp.type)){
                msgSecondType = temp;
                break;
            }
        }
        return msgSecondType;
    }

}