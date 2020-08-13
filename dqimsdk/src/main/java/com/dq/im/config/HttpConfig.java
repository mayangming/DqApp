package com.dq.im.config;

import java.util.ResourceBundle;

/**
 * HTTP配置
 */
public class HttpConfig {
    private  String HTTP_SERVER = "";
    private  String HTTP_SERVER_SDK = "";
    private static HttpConfig httpConfig;
    static {
        httpConfig = new HttpConfig();
        String webSocketServer = ResourceBundle.getBundle("appConfig").getString("webSocketServer");
        httpConfig.setHTTP_SERVER(webSocketServer);
        String appServer = ResourceBundle.getBundle("appConfig").getString("appServer");
        httpConfig.setHTTP_SERVER_SDK(appServer);
    }

    private HttpConfig(){

    }

    public static HttpConfig getInstance(){
        return httpConfig;
    }


    public String getHTTP_SERVER() {
        return HTTP_SERVER;
    }

    public void setHTTP_SERVER(String HTTP_SERVER) {
        this.HTTP_SERVER = HTTP_SERVER;
    }

    public String getHTTP_SERVER_SDK() {
        return HTTP_SERVER_SDK;
    }

    public void setHTTP_SERVER_SDK(String HTTP_SERVER_SDK) {
        this.HTTP_SERVER_SDK = HTTP_SERVER_SDK;
    }
}