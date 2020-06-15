package com.da.library;

import android.content.Context;
import android.os.Handler;


/**
 * @author: dukangkang
 * @date: 2018/9/11 19:44.
 * @description: todo ...
 */
public class DqLibConfig {
    public static Context sContext;
    public static final Handler sHandler = new Handler();

    public static void init(Context context) {
        sContext = context.getApplicationContext();

    }

    private static DqLibConfig instance = null;

    private DqLibConfig(){
    }

    public static DqLibConfig getInstance() {
        synchronized (DqLibConfig.class) {
            if (instance == null) {
                instance = new DqLibConfig();
            }
        }
        return instance;
    }

    public void runOnUiThread(Runnable runnable) {
        sHandler.post(runnable);
    }

    public void runOnUiThread(Runnable runnable, long delayTime) {
        sHandler.postDelayed(runnable, delayTime);
    }
}
