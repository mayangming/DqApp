package com.dq.sdk.ad.http.bean

import android.os.Build

/**
 * HTTP请求基础类
 */
class HttpBaseRequestBean {
    var appId = "1"
    var sha = "82:69:20:A3:F6:4C:80:DA:5A:3F:BC:C5:67:85:81:F8:28:66:E8:9F"
    var appInfo = AppInfo()
    var data = Any()

    fun setAppVersion(version: String?) {
        appInfo.app_version = version
    }

    inner class AppInfo {
        var app_version: String? = "" //app版本
        var device_type = "android" //设备型号
        var device_brand = Build.BRAND + Build.MODEL //设备名称
        var os_version = Build.VERSION.RELEASE //系统版本

    }

    init {
         appId = "1"
         sha = "82:69:20:A3:F6:4C:80:DA:5A:3F:BC:C5:67:85:81:F8:28:66:E8:9F"
    }
}