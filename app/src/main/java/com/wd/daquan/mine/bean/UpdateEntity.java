package com.wd.daquan.mine.bean;

/**
 * @author: dukangkang
 * @date: 2018/5/19 11:30.
 * @description: todo ...
 */
public class UpdateEntity {
    public String upgradetype;//1普通2强制
    public String update_status;//1（需要升级），0（不需要升级）
    public String version_code;
    public String vername;
    public String content;
    public String url;
    public String version;
    public String key1 = ""; // 阀值
    public String key2 = ""; // 是否重置Host 0：不重置 1：重置

    @Override
    public String toString() {
        return "UpdateEntity{" +
                "upgradeType='" + upgradetype + '\'' +
                ", update_status='" + update_status + '\'' +
                ", versionCode='" + version_code + '\'' +
                ", versionName='" + vername + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", appVersion='" + version + '\'' +
                ", key1='" + key1 + '\'' +
                ", key2='" + key2 + '\'' +
                '}';
    }
}
