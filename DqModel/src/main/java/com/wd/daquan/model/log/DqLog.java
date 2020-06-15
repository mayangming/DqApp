package com.wd.daquan.model.log;

import android.util.Log;

import com.wd.daquan.model.BuildConfig;

/**
 * 全局统一的日志打印工具类：comes from <pre>https://github.com/elvishew/XLog</pre>
 * Created by Kind on 2018/10/10.
 */

public class DqLog {

    private static final String DOUQUAN = "douquan";


    public static void i(String msg){
        i(DOUQUAN, msg);
    }

    /**
     * 需要打印的信息日志
     */
    public static void i(String tag, String msg){
        if(BuildConfig.DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void w(String msg){
        w(DOUQUAN, msg);
    }

    /**
     * 警告日志
     */
    public static void w(String tag, String msg){
        if(BuildConfig.DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void e(String msg){
        e(DOUQUAN, msg);
    }

    /**
     * 错误日志
     */
    public static void e(String tag, String msg){
        if(BuildConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void d(String msg){
        e(DOUQUAN, msg);
    }

    /**
     * debug日志
     */
    public static void d(String tag, String msg){
        if(BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

}
