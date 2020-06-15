package com.wd.daquan.service;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;

/**
 * 华为推送服务
 */
public class HuaWeiHmsMessageService extends HmsMessageService{
    public HuaWeiHmsMessageService() {
        super();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("YM","华为信息:"+remoteMessage.getData());
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("YM","获取华为token:"+s);
    }

    @Override
    public void onTokenError(Exception e) {
        super.onTokenError(e);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i1) {
        return super.onStartCommand(intent, i, i1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}