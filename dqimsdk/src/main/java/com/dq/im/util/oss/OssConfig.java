package com.dq.im.util.oss;

import com.dq.im.constants.URLUtil;

/**
 * OSS配置
 */
public class OssConfig {
    public static String endpoint = "https://oss-cn-beijing.aliyuncs.com";
    public static String stsServer = URLUtil.getServer()+"oss/appTokenServer";
    public static String bucketName = "dq-oss";
}