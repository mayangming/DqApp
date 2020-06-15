package com.wd.daquan.model;

import android.content.Context;

public class ModelConfig {

    private static Context sContext;

    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }

    public static String getString(int id){
        return getContext().getResources().getString(id);
    }
}
