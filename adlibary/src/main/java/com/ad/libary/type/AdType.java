package com.ad.libary.type;

/**
 * 广告类型
 */
public enum AdType {
    AD_TT(1,"穿山甲"),//穿山甲
    AD_GDT(2,"广点通"),//广点通
    AD_DQ(3,"斗圈")//斗圈
    ;
    public int type;
    public String adName;
    AdType(int type,String adName) {
        this.type = type;
        this.adName = adName;
    }


    public static AdType getAdTypeForValue(int value){
        AdType adType = AD_TT;
        for (AdType type: values()) {
            if (value == type.type){
                adType = type;
                break;
            }
        }
        return adType;
    }

}