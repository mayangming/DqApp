package com.wd.daquan.common.utils;

import android.util.Log;

import com.wd.daquan.DqApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author: dukangkang
 * @date: 2018/10/25 09:22.
 * @description: todo ...
 */
public class AssetsHelper {

    public static String getData(String fileName) {
        String target = "";
        try {
            InputStream is = DqApp.sContext.getResources().getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String temp = "";
            while ((temp = reader.readLine()) != null) {
                target += temp;
            }
            Log.w("docking", "target = " + target);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return target;
    }
}
