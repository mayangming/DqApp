package com.da.library.widget;

import android.app.Activity;
import android.content.Context;

import com.da.library.R;


/**
 * @author: dukangkang
 * @date: 2018/8/21 16:11.
 * @description: todo ...
 */
public class AnimUtils {

    /**
     * 进入Activity
     * @param context
     */
    public static void enterAnimForActivity(Context context) {
        if (context instanceof Activity) {
//            ((Activity) context).overridePendingTransition(R.anim.translate_right_to_left_in, R.anim.translate_right_to_left_out);
            ((Activity) context).overridePendingTransition(R.anim.scale_in, R.anim.scale_none);
        }
    }

    /**
     * 退出Activity
     * @param context
     */
    public static void exitAnimForActivity(Context context) {
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.translate_left_to_right_in, R.anim.translate_left_to_right_out);
            ((Activity) context).overridePendingTransition(R.anim.scale_none, R.anim.scale_out);

        }
    }

}
