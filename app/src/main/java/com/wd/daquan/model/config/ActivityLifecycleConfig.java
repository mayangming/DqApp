package com.wd.daquan.model.config;

import android.app.Application;
import android.util.Log;

import com.dq.im.DqWebSocketClient;
import com.wd.daquan.common.utils.ForegroundCallbacks;

public class ActivityLifecycleConfig {


    public static void init(Application context) {

        ForegroundCallbacks.init(context);

        ForegroundCallbacks.get().addListener(new ForegroundCallbacks.Listener() {
            @Override
            public void onBecameForeground() {
                Log.w("cnvideo", "切换到前台");
                if (!DqWebSocketClient.getInstance().isConnectIng()){
                    DqWebSocketClient.getInstance().build();
                }
            }

            @Override
            public void onBecameBackground() {
                Log.w("cnvideo", "切换到后台");
            }
        });



//        context.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
//            @Override
//            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//
//            }
//
//            @Override
//            public void onActivityStarted(Activity activity) {
//                sTopActivity = activity;
//            }
//
//            @Override
//            public void onActivityResumed(Activity activity) {
//                sTopActivity = activity;
//            }
//
//            @Override
//            public void onActivityPaused(Activity activity) {
//
//            }
//
//            @Override
//            public void onActivityStopped(Activity activity) {
//
//            }
//
//            @Override
//            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//
//            }
//
//            @Override
//            public void onActivityDestroyed(Activity activity) {
//
//            }
//        });
    }
}
