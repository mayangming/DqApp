package com.dq.im.third_system;

import android.util.Log;

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;

/**
 * 华为推送自定义广播
 */
public class HwSystemMessageReceiver extends HmsMessageService {
    /**
     * 接收透传消息
     * @param remoteMessage
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("YM","华为消息内容数据:"+remoteMessage.toString());
    }

    /**
     * 服务端更新Token
     * @param s
     */
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("YM","华为消息更新的Token:"+s);
        HwPushManager.getHwPushManager().sendMessage(ThirdSystemType.HUA_WEI,s);
    }

    /**
     * 服务端更新Token失败回调
     * @param e
     */
    @Override
    public void onTokenError(Exception e) {
        super.onTokenError(e);
        e.printStackTrace();
        Log.e("YM","华为消息更新的Token失败:");
    }
}