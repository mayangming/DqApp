/*
    ShengDao Android Client, BroadcastManager
    Copyright (c) 2015 ShengDao Tech Company Limited
*/

package com.wd.daquan.common.utils;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.wd.daquan.DqApp;
import com.wd.daquan.common.constant.KeyValue;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 2018/7/24 fangzhi 重构广播消息处理类
 **/
public class QCBroadcastManager {

    private Map<String, BroadcastReceiver> receiverMap = new HashMap<>();

    /**
     * 添加
     *
     * @param
     */
    public void addAction(String action, BroadcastReceiver receiver) {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(action);
            DqApp.sContext.registerReceiver(receiver, filter);
            receiverMap.put(action, receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送广播
     *
     * @param action 唯一码
     */
    public void sendBroadcast(String action) {
        sendBroadcast(action, "");
    }

    /**
     * 发送广播
     *
     * @param action 唯一码
     * @param obj    参数
     */
    public void sendBroadcast(String action, Object obj) {
        try {
            Intent intent = new Intent();
            intent.setAction(action);
            intent.putExtra("result", JsonParseUtils.obj2Json(obj));
            DqApp.sContext.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送参数为 String 的数据广播
     *
     * @param action
     * @param s
     */
    public void sendBroadcast(String action, String s) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(KeyValue.STRING, s);
        DqApp.sContext.sendBroadcast(intent);
    }

//    public void sendBroadcast(String action, Message content, String str) {
//        Intent intent = new Intent();
//        intent.setAction(action);
//        intent.putExtra("result", content);
//        DqApp.sContext.sendBroadcast(intent);
//    }

    public void sendBroadcast(String action, String groupId, List<String> userIds) {
        Intent intent = new Intent();
        intent.setAction(action);
        Bundle bundle = new Bundle();
        bundle.putString("groupId", groupId);
        bundle.putSerializable("userIds", (Serializable)userIds);
        intent.putExtras(bundle);
        DqApp.sContext.sendBroadcast(intent);
    }


    /**
     * 销毁指定广播
     *
     * @param action
     */
    public void destroy(String action) {
        if (receiverMap != null) {
            BroadcastReceiver receiver = receiverMap.remove(action);
            if (receiver != null) {
                DqApp.sContext.unregisterReceiver(receiver);
            }
        }
    }

    public void destroyAll(){
        for (Map.Entry<String, BroadcastReceiver> entry : receiverMap.entrySet()) {
             BroadcastReceiver receiver = entry.getValue();
            DqApp.sContext.unregisterReceiver(receiver);
        }
        receiverMap.clear();
        receiverMap = null;
    }

}
