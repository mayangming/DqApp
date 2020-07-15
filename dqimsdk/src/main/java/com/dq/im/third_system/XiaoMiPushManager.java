package com.dq.im.third_system;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.NonNull;

import com.dq.im.ImProvider;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

public class XiaoMiPushManager extends ThirdPushManager{
    private static XiaoMiPushManager xiaoMiPushManager;

    static {
        xiaoMiPushManager = new XiaoMiPushManager();
    }

    private XiaoMiPushManager() {
    }

    public static XiaoMiPushManager getXiaoMiPushManager() {
        return xiaoMiPushManager;
    }

    public void registerXiaoMiSystemReceiver(String miAppId, String miAppKey){
        //初始化push推送服务
        if(shouldInit()) {
            MiPushClient.registerPush(ImProvider.context, miAppId, miAppKey);
        }
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) ImProvider.context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = ImProvider.context.getApplicationInfo().processName;
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否支持小米推送
     */
    public boolean isSupport(){
        String manufacturer = Build.MANUFACTURER;
        //这个字符串可以自己定义,例如判断华为就填写huawei,魅族就填写meizu
        if ("xiaomi".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }
}
