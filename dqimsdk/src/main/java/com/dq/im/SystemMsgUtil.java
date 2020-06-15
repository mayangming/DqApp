package com.dq.im;

import android.annotation.SuppressLint;
import android.provider.Settings;

/**
 * 获取系统信息的工具类
 */
public class SystemMsgUtil{
    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取当前手机系统版本号
     * @return  系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }
    /**
     * 获取当前手机系统版本号
     * @return  系统版本号
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidId(){
        String androidId = "";
        if (null != ImProvider.context){
            androidId = Settings.Secure.getString(ImProvider.context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return androidId;
    }

//    /**
//     * 获取手机卡的唯一标识
//     * @return
//     */
//    public static String getPhoneCardICCD(){
//        return GetPhoneISMIUtil.check();
//    }

}