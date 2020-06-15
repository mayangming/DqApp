package com.wd.daquan.imui.type;

/**
 * 好友列表多类型枚举
 */
public enum FriendMultiType {
    GROUP(0),
    FRIEND(1)
    ;

    private int typeValue;

    FriendMultiType(int type){
        this.typeValue = type;
    }

    public int getValue(){
        return this.typeValue;
    }

    public static FriendMultiType getFiendMultiType(int typeValue){
        FriendMultiType friendMultiType = GROUP;
        switch (typeValue){
            case 0:
                friendMultiType = GROUP;
                break;
            case 1:
                friendMultiType = FRIEND;
                break;
        }
        return friendMultiType;
    }

}