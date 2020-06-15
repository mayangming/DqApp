package com.netease.nim.uikit.common.util.notification;

import android.util.Log;

import com.wd.daquan.common.utils.ForegroundCallbacks;

/**
 * 前后台拦截器
 */
public class FrontNotificationFilter implements NotificationFilter{

    private boolean isForeground = true;
    public FrontNotificationFilter() {
        foregroundListener();
    }

    private void foregroundListener(){
        ForegroundCallbacks.get().addListener(new ForegroundCallbacks.Listener() {
            @Override
            public void onBecameForeground() {
                Log.e("YM","前台应用");
                isForeground = true;
            }

            @Override
            public void onBecameBackground() {
                Log.e("YM","后台应用");
                isForeground = false;
            }
        });
    }

    @Override
    public boolean filter() {
        return isForeground;
    }
}
