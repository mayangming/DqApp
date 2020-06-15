/*
    ShengDao Android Client, NLog
    Copyright (c) 2014 ShengDao Tech Company Limited
 */

package com.wd.daquan.common.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;


public class QCLog {

    private static final String LOG_FORMAT = "%1$s\n%2$s";

    public static void d(String tag, Object... args) {
        log(Log.DEBUG, null, tag,  args);
    }

    public static void dRelease(String tag, boolean isRelease, Object... args) {
        log(Log.DEBUG, isRelease,null, tag,  args);
    }

    public static void i(String tag, Object... args) {
        log(Log.INFO,null, tag, args);
    }
    public static void iRelease(String tag, boolean isRelease, Object... args) {
        log(Log.INFO, isRelease,null, tag, args);
    }

    public static void w(String tag, Object... args) {
        log(Log.WARN, null, tag, args);
    }

    public static void wRelease(String tag, boolean isRelease, Object... args) {
        log(Log.WARN, isRelease,null, tag, args);
    }

    public static void e(Throwable ex, boolean isRelease) {
        log(Log.ERROR, isRelease, ex, null);
    }

    public static void e(String tag, Object... args) {
        log(Log.ERROR, null, tag, args);
    }
    public static void eRelease(String tag, boolean isRelease, Object... args) {
        log(Log.ERROR, isRelease,null, tag, args);
    }

    public static void e(Throwable ex, boolean isRelease, String tag, Object... args) {
        log(Log.ERROR, isRelease, ex, tag, args);
    }

    private static void log(int priority, boolean isRelease, Throwable ex, String tag, Object... args) {

        if (isRelease == false)return;

        String log = "";
        if (ex == null) {
            if (args != null && args.length > 0) {
                for (Object obj : args) {
                    log += String.valueOf(obj);
                }
            }
        } else {
            String logMessage = ex.getMessage();
            String logBody = Log.getStackTraceString(ex);
            log = String.format(LOG_FORMAT, logMessage, logBody);
        }
        Log.println(priority, tag, log);
    }

    private static void log(int priority, Throwable ex, String tag, Object... args) {

        if (isDebug() == false)return;

        String log = "";
        if (ex == null) {
            if (args != null && args.length > 0) {
                for (Object obj : args) {
                    log += String.valueOf(obj);
                }
            }
        } else {
            String logMessage = ex.getMessage();
            String logBody = Log.getStackTraceString(ex);
            log = String.format(LOG_FORMAT, logMessage, logBody);
        }
        Log.println(priority, tag, log);
    }

    private static Boolean isDebug = null;

    public static boolean isDebug(){
        return isDebug == null ? false : isDebug.booleanValue();
    }
    //同步debug状态
    public static void syncDebugStatus(Context mContext){
        if(isDebug == null) {
            isDebug = mContext.getApplicationInfo() != null
                    && (mContext.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        }
    }

}
