package com.ad.libary.config;


import com.ad.libary.BuildConfig;

public class UrlConfig {
    private static int SERVER_TYPE = 1; // 0 :本地 1: 测试 2:生产

    /**
     * 本地服务器地址
     */
    private static String LOCAL_SERVER = "http://192.168.72.202:8096/";
    /**
     * 本地服务器地址
     */
//        private val LOCAL_SERVER = "http://192.168.72.191:8096/"

    /**
     * 测试服务器地址
     */
    private static String TEST_SERVER = "http://39.106.158.176:8096/";
    /**
     * 生产服务器地址
     */
    private static String ONLINE_SERVER = "http://39.106.158.176:8096/";

    /**
     * 获取广告平台
     */
    public static String GET_AD_TERRACE = "advertisement/getadTerrace";

    /**
     * 获取用户接口
     */
    public static String getURL(String url){

        return getServer() + url;
    }
    private static String getServer(){
        return BuildConfig.SERVER;
    }

//    private static String getServer(){
//        String result = LOCAL_SERVER;
//        switch (SERVER_TYPE){
//            case 0:
//                result = LOCAL_SERVER;
//                break;
//            case 1:
//                result = TEST_SERVER;
//                break;
//            case 2:
//                result = ONLINE_SERVER;
//                break;
//        }
//        return result;
//    }
}
