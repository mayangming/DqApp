package com.wd.daquan.model.utils;


import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 *  封装一些UI的功能
 * Created by Kind on 2018/11/16.
 */
public class UIUtil {

    /**
     * dp转px
     */
    public static int dip2px(float dpValue) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取菜单栏高度
     */
    public static int getStateBarHeight(){
        int resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return Resources.getSystem().getDimensionPixelSize(resourceId);
        }
        return dip2px(20);
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

}
