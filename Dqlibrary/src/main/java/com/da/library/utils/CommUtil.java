package com.da.library.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Kind on 2019/4/16.
 */
public class CommUtil {

    /**
     * 是否Http url
     * @param url
     * @return
     */
    public static boolean isWebHttpUrl(String url) {
        if (!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://")) {
            return false;
        }
        return true;
    }


    /**
     * 对double类型的数值保留指定位数的小数: 就进舍入
     *
     * @param value ： 需格式化的数字
     * @param digit : 小数点后保留的位数
     */
    public static Double formatNum(double value, int digit) {
        BigDecimal bg = new BigDecimal(value);
        return bg.setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    public static String dformat(String money) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(TypeConvertUtil.toDouble(money));
    }

    /**
     * 强制打开键盘
     * @param context
     */
    public static void forceOpen(Context context) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).
                toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 强制打开键盘
     * @param editText
     */
    public static void showSoftKeyboard(EditText editText){
        if (editText == null)return;
        InputMethodManager imm = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null){
            imm.toggleSoftInput(1000, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 强制关闭键盘
     * @param activity
     */
    public static void forceClose(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputmanger != null){
                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

}
