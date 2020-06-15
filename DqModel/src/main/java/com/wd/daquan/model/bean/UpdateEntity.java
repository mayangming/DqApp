package com.wd.daquan.model.bean;

public class UpdateEntity {
    public String upgradeType;//升级类型，1普通2强制
    public String update_status;//1（需要升级），0（不需要升级）
    public String versionCode;
    public String versionName;
    public String content;
    public String url;
    public String appVersion;
    public String redEnvelopedRainSwitch;//红包雨开关，0 ：开 1：关，
    public String key1 = ""; // 阀值
    public String key2 = ""; // 是否重置Host 0：不重置 1：重置

    @Override
    public String toString() {
        return "UpdateEntity{" +
                "upgradeType='" + upgradeType + '\'' +
                ", update_status='" + update_status + '\'' +
                ", versionCode='" + versionCode + '\'' +
                ", versionName='" + versionName + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", redEnvelopedRainSwitch='" + redEnvelopedRainSwitch + '\'' +
                ", key1='" + key1 + '\'' +
                ", key2='" + key2 + '\'' +
                '}';
    }
}
