package com.wd.daquan.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by DELL on 2018/9/11.
 */

public class NetWorkUtils {

    public static boolean isNetworkAvailable(Context context) {
        boolean res = false;
        int reTry = 3;

        for (int i = 0; i < reTry; i++) {
            try {
                ConnectivityManager nm = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = nm != null ? nm.getActiveNetworkInfo() : null;
                if (null != networkInfo) {
                    res = networkInfo.isConnected();
                }
                break;
            } catch (Exception e) {

            }
        }
        return res;
    }

    public static int getNetworkTypeName(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm != null ? cm.getActiveNetworkInfo() : null;
        if (ni != null) {
            return ni.getType();
        }
        return -1;
    }
}
