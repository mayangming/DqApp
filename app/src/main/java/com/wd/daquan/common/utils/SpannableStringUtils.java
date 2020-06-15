package com.wd.daquan.common.utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

/**
 * SpannableString处理
 */
public class SpannableStringUtils {

    public static void addColorText(SpannableStringBuilder builder, String msg, int color, int fontSize) {
        int len = msg.length();
        SpannableString spannable = addTextFountAndColor(msg, len, color, fontSize);
        builder.append(spannable);
    }

    public static void addColorText(SpannableStringBuilder builder, String msg) {
        int len = msg.length();
        SpannableString spannable = addTextFountAndColor(msg, len);
        builder.append(spannable);
    }

    private static SpannableString addTextFountAndColor(String msg, int len) {
        SpannableString spannable = new SpannableString(msg);
        return spannable;
    }

    public static SpannableString addTextFountAndColor(String msg, int len, int color, int fontSize) {
        SpannableString spannable = new SpannableString(msg);
        AbsoluteSizeSpan fontSpan = new AbsoluteSizeSpan(fontSize, true);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        spannable.setSpan(fontSpan, 0, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(colorSpan, 0, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static SpannableString addTextColor(String msg, int startLength, int endLength, int color) {
        SpannableString spannable = new SpannableString(msg);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        spannable.setSpan(colorSpan, startLength, endLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static SpannableStringBuilder addTextColor(String msg, int startLength1, int endLength1, int startLength2, int endLength2, int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder(msg);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(color);
        builder.setSpan(colorSpan, startLength1, endLength1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(colorSpan1, startLength2, endLength2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return builder;
    }

    public static SpannableStringBuilder addTextColor(String msg, int startLength1, int endLength1, int startLength2, int endLength2,
                                                      int color1, int color2) {
        SpannableStringBuilder builder = new SpannableStringBuilder(msg);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color1);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(color2);
        builder.setSpan(colorSpan, startLength1, endLength1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(colorSpan1, startLength2, endLength2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return builder;
    }
}
