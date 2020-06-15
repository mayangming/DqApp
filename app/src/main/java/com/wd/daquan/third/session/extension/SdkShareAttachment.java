package com.wd.daquan.third.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.wd.daquan.common.constant.KeyValue;

/**
 * Created by zhoujianghua on 2015/4/9.
 */
public class SdkShareAttachment extends CustomAttachment {

    public String title;
    public String content;
    public String url;
    public String urlIntent;
    public String appId;
    public String appName;
    public String backInfo;
    public String package_name;
    public String appSecret;
    public int type;
    public String extra;

    public String appLogo;

    public SdkShareAttachment() {
        super(CustomAttachmentType.SDK_SHARE_MSG);
    }

    @Override
    protected void parseData(JSONObject data) {
        title = data.getString(KeyValue.SDKShare.TITLE);
        content = data.getString(KeyValue.SDKShare.CONTENT);
        url = data.getString(KeyValue.SDKShare.URL);
        urlIntent = data.getString(KeyValue.SDKShare.URL_INTENT);
        appId = data.getString(KeyValue.SDKShare.APP_ID);
        appName = data.getString(KeyValue.SDKShare.APP_NAME);
        appLogo = data.getString(KeyValue.SDKShare.APP_LOGO);
        backInfo = data.getString(KeyValue.SDKShare.BACKINFO);
        package_name = data.getString(KeyValue.SDKShare.PACKAGE_NAME);
        appSecret = data.getString(KeyValue.SDKShare.APP_SECRET);
        type = data.getInteger(KeyValue.SDKShare.TYPE);
        extra = data.getString(KeyValue.SDKShare.EXTRA);
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KeyValue.SDKShare.TITLE, title);
        jsonObj.put(KeyValue.SDKShare.CONTENT, content);
        jsonObj.put(KeyValue.SDKShare.URL, url);
        jsonObj.put(KeyValue.SDKShare.URL_INTENT, urlIntent);
        jsonObj.put(KeyValue.SDKShare.APP_ID, appId);
        jsonObj.put(KeyValue.SDKShare.APP_NAME, appName);
        jsonObj.put(KeyValue.SDKShare.APP_LOGO, appLogo);
        jsonObj.put(KeyValue.SDKShare.BACKINFO, backInfo);
        jsonObj.put(KeyValue.SDKShare.PACKAGE_NAME, package_name);
        jsonObj.put(KeyValue.SDKShare.APP_SECRET, appSecret);
        jsonObj.put(KeyValue.SDKShare.TYPE, type);
        jsonObj.put(KeyValue.SDKShare.EXTRA, extra);
        return jsonObj;
    }

    public static SdkShareAttachment obtain(int type, String title, String content, String thumb,
                                            String urlIntent, String appid, String appsecret, String appName, String appLogo, String backinfo,
                                            String packageName, String extra) {
        return new SdkShareAttachment(type, title, content, thumb, urlIntent, appid, appsecret,
                appName, appLogo, backinfo, packageName, extra);

    }

    public SdkShareAttachment(int type, String title, String content, String thumb, String urlIntent,
                              String appid, String appsecret, String appName, String appLogo, String backinfo, String packageName, String extra) {
        super(CustomAttachmentType.SDK_SHARE_MSG);
        this.type = type;
        this.title = title;
        this.content = content;
        this.url = thumb;
        this.urlIntent = urlIntent;
        this.appId = appid;
        this.appSecret = appsecret;
        this.appName = appName;
        this.appLogo = appLogo;
        this.backInfo = backinfo;
        this.package_name = packageName;
        this.extra = extra;
    }

}
