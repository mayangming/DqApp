package com.da.library.utils;

import android.os.Handler;

/**
 * 延时执行
 * Created by Kind on 2019/3/30.
 */
public class TimerUtil {

    public interface CallBack {
        void call();
    }

    /**
     * 延时执行
     *
     * @param callBack 延时执行回调
     * @param tm       延时时长
     */
    public static void beginTime(final CallBack callBack, int tm) {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                callBack.call();
            }
        };
        handler.postDelayed(runnable, tm);
    }
}
