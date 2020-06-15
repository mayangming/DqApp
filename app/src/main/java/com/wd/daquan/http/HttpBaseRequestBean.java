package com.wd.daquan.http;

import android.os.Build;

import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.utils.ModelUtil;

/**
 * HTTP请求基础类
 */
public class HttpBaseRequestBean{
    private String userId = "";
    private String token = "";
    private AppInfo appInfo = new AppInfo();
    private Object data = new Object();
    public HttpBaseRequestBean() {
        setToken(ModuleMgr.getCenterMgr().getToken());
        setUserId(ModuleMgr.getCenterMgr().getUID());
        setAppVersion(ModelUtil.getVersion());
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setAppVersion(String version){
        appInfo.setApp_version(version);
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public class AppInfo{
        private String app_version = "";//app版本
        private String device_type = "android";//设备型号
        private String device_brand = Build.BRAND + Build.MODEL;//设备名称
        private String os_version = Build.VERSION.RELEASE;//系统版本

        public String getApp_version() {
            return app_version;
        }

        public void setApp_version(String app_version) {
            this.app_version = app_version;
        }

        public String getDevice_type() {
            return device_type;
        }

        public void setDevice_type(String device_type) {
            this.device_type = device_type;
        }

        public String getDevice_brand() {
            return device_brand;
        }

        public void setDevice_brand(String device_brand) {
            this.device_brand = device_brand;
        }

        public String getOs_version() {
            return os_version;
        }

        public void setOs_version(String os_version) {
            this.os_version = os_version;
        }
    }

}