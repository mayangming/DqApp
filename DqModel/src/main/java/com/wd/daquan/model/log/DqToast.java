package com.wd.daquan.model.log;

import android.content.Context;
import androidx.annotation.StringRes;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 全局Toast提示类
 * Created by Kind on 2018/9/30.
 */

public class DqToast {

    private static Context context;

    /**
     * 需要在application中进行初始化，以便程序中后续进行调用
     *
     * @param context applicationContext
     */
    public static void init(Context context) {
        DqToast.context = context;
    }

    public static void showShort(String tip) {
        Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(@StringRes int tip) {
        Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(String tip) {
        Toast.makeText(context, tip, Toast.LENGTH_LONG).show();
    }

    public static void showLong(@StringRes int tip) {
        Toast.makeText(context, tip, Toast.LENGTH_LONG).show();
    }

    /**
     * 居中显示的toast
     *
     * @param tip 提示文字
     */
    public static void showCenterShort(String tip) {
        Toast toast = Toast.makeText(context, tip, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 居中显示的toast
     *
     * @param tip 提示文字
     */
    public static void showCenterLong(String tip) {
        Toast toast = Toast.makeText(context, tip, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
