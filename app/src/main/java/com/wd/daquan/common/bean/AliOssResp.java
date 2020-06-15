package com.wd.daquan.common.bean;


import java.io.Serializable;


public class AliOssResp implements Serializable {
    private static final long serialVersionUID = -1L;

    public String region;
    public String bucket;
    public String OSS_WEB_SITE;
    public String AccessKeySecret;
    public String AccessKeyId;
    public String Expiration;
    public String SecurityToken;
    public String media_directory;
    public int expire;

    @Override
    public String toString() {
        return "AliOssResp{" +
                "region='" + region + '\'' +
                ", bucket='" + bucket + '\'' +
                ", OSS_WEB_SITE='" + OSS_WEB_SITE + '\'' +
                ", AccessKeySecret='" + AccessKeySecret + '\'' +
                ", AccessKeyId='" + AccessKeyId + '\'' +
                ", Expiration='" + Expiration + '\'' +
                ", SecurityToken='" + SecurityToken + '\'' +
                ", media_directory='" + media_directory + '\'' +
                ", expire=" + expire +
                '}';
    }
}
