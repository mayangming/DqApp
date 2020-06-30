package com.wd.daquan.imui.type;

/**
 * 二级消息类型
 */
public enum MsgSecondType{
    MSG_SECOND_TYPE_UN_KNOWN("未知消息",-99),
    MSG_SECOND_TYPE_TRANSFER("转账消息",1),
    MSG_SECOND_TYPE_RED_PACKAGE("红包消息",2),
    MSG_SECOND_TYPE_NORMAL("普通消息",3),
    MSG_SECOND_TYPE_RED_RECEIVE("红包被领取",4),
    MSG_SECOND_TYPE_RED_COMPLETE("红包被领完",5);

    public String description;
    public int type;
    MsgSecondType(String description,int type){
        this.description = description;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return String.valueOf(type);
    }

    public void setType(int type) {
        this.type = type;
    }

    public static String getMsgSecondTypeDescription(String status){
        String description = "未定义红包类型";
        for (MsgSecondType redPackageStatus : values()){
            if (redPackageStatus.getType().equals(status)){
                description = redPackageStatus.description;
                break;
            }
        }
        return description;
    }

    public static MsgSecondType getMsgSecondTypeByValue(String type){
        MsgSecondType msgSecondType = MSG_SECOND_TYPE_UN_KNOWN;
        for (MsgSecondType temp : values()){
            if (type.equals(String.valueOf(temp.type))){
                msgSecondType = temp;
                break;
            }
        }
        return msgSecondType;
    }

}