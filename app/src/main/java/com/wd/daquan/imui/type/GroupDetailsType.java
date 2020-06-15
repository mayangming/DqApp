package com.wd.daquan.imui.type;

/**
 * 群组列表详情类型
 */
public enum GroupDetailsType {
    USER(0),
    ADD_USER(1),
    REDUCE_USER(2)
    ;

    private int typeValue;

    GroupDetailsType(int type){
        this.typeValue = type;
    }

    public int getValue(){
        return this.typeValue;
    }

    public static GroupDetailsType getFiendMultiType(int typeValue){
        GroupDetailsType groupDetailsType = USER;
        switch (typeValue){
            case 0:
                groupDetailsType = USER;
                break;
            case 1:
                groupDetailsType = ADD_USER;
                break;
            case 2:
                groupDetailsType = REDUCE_USER;
                break;
        }
        return groupDetailsType;
    }
}