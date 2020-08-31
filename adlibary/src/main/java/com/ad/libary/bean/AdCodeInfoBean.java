package com.ad.libary.bean;

public class AdCodeInfoBean {
    /**
     * appId
     */
    private long appId = 0;
    /**
     * 广告代码位ID
     */
    private long codeId = 0;
    /**
     * 包名
     */
    private String packageName = "";
    /**
     * 平台 1:穿山甲 2:广点通
     */
    private int terrace = 0;
    /**
     * 应用名称
     */
    private String appName = "";

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public long getCodeId() {
        return codeId;
    }

    public void setCodeId(long codeId) {
        this.codeId = codeId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getTerrace() {
        return terrace;
    }

    public void setTerrace(int terrace) {
        this.terrace = terrace;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return "AdCodeInfoBean{" +
                "appId=" + appId +
                ", codeId=" + codeId +
                ", packageName='" + packageName + '\'' +
                ", terrace=" + terrace +
                ", appName='" + appName + '\'' +
                '}';
    }
}