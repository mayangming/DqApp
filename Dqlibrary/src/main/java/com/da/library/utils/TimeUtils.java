package com.da.library.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  格式化时间
 * Created by Kind on 2018/11/16.
 */

public class TimeUtils {

    /**
     * 格式化时间-抢红包时间
     * @param time
     * @return
     *  x秒
     *  x分钟
     *  x小时
     */
    public static String formatTime(long time) {
        String target = "";
        long minute = time / 60;
        long hour = minute / 60;
        if (minute < 1) {
            if (time == 0) {
                target = "1秒";
            } else {
                target = time + "秒";
            }
        } else if (hour < 1) {
            target = minute + "分钟";
        } else if (hour < 24) {
            target = hour + "小时";
        } else {
            target = "";
        }
        return  target;
    }

    /**
     * 格式化时间-收益明细
     * @param time
     * @return
     *  刚刚
     *  x分钟
     *  ...
     */
    public static String formatTimeForDetail(long time) {
        long timeDiff = System.currentTimeMillis() / 1000 - time;
        String target = formatTimeString(timeDiff);
        if (!TextUtils.isEmpty(target)) {
            return target;
        }

        target = formatDate(time * 1000);
        return target;
    }

    private static String formatTimeString(long time) {
        String target = "";
        long minute = time / 60;
        long hour = minute / 60;
        if (minute < 1) {
            target = "刚刚";
        } else if (hour < 1) {
            target = minute + "分钟";
        } else if (hour < 24) {
            target = hour + "小时";
        } else if (hour < 48){
            target = "昨天";
        } else if (hour < 72) {
            target = "前天";
        }
        return  target;
    }

    private static String formatDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time);
        String target = format.format(date);
        return target;
    }

    /**
     * 格式化时间-视频播放时长
     * @param duration
     * @return
     *     00:00:00
     */
    public static String formatPlayTime(String duration) {
        if (TextUtils.isEmpty(duration)) {
            return "0:00";
        }
        float result = Float.parseFloat(duration);
        int time = (int) Math.floor(result);
        int hh = time / 3600;
        int mm = time % 3600 / 60;
        int ss = time % 3600 % 60;
        String strTemp = null;
        if (time >= 3600) {
            strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            strTemp = String.format("%2d:%02d", mm, ss);
        }
        return strTemp;
    }

    /**
     * 格式化时间-阅读时长
     * @param second
     * @return
     *      xx小时xx分钟
     */
    public static String formatReadTime(long second) {
        String target = "";
        long minute = 0;
        long hour = second / 3600;
        long temp = second % 3600;
        if (hour >= 1) { // 时，分
            if (temp > 0) {
                if (temp > 60) {
                    minute = temp / 60;
                } else {
                    minute = 1;
                }
                target = hour + "小时" + minute + "分钟";
            } else {
                target = hour + "小时";
            }
        } else { // 分
            minute = second / 60;
            target = minute + "分钟";
        }
        return  target;
    }

    /**
     * 下拉刷新时间
     * @param time 秒
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String friendlyTime(long time) {
        //获取time距离当前的秒数
        int ct = (int) ((System.currentTimeMillis() - time) / 1000);

        if (ct == 0) {
            return "刚刚";
        }

        if (ct > 0 && ct < 60) {
            return ct + "秒前";
        }

        if (ct >= 60 && ct < 3600) {
            return Math.max(ct / 60, 1) + "分钟前";
        }
        if (ct >= 3600 && ct < 86400)
            return ct / 3600 + "小时前";
        if (ct >= 86400 && ct < 2592000) { //86400 * 30
            int day = ct / 86400;
            return day + "天前";
        }
//        if (ct >= 2592000 && ct < 31104000) { //86400 * 30
//            return ct / 2592000 + "月前";
//        }
//        return ct / 31104000 + "年前";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(ct * 1000L);
    }
}
