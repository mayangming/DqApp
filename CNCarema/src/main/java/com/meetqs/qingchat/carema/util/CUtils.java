package com.meetqs.qingchat.carema.util;

/**
 * @author: dukangkang
 * @date: 2018/6/29 09:58.
 * @description: todo ...
 */
public class CUtils {
    /**
     * 时间
     *
     * @param time
     * @return
     */
    public static String getPlayTime(long time) {
        int hh = (int) (time / 3600);
        int mm = (int) (time % 3600 / 60);
        int ss = (int) (time % 3600 % 60);
        String strTemp = null;
        if (time >= 3600) {
            strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            strTemp = String.format("%02d:%02d", mm, ss);
        }

        return strTemp;
    }

    //限制点击次数
    private static long lastClickTime;
    //按钮点击频率控制
    public static boolean isFastDoubleClick(int duration) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < duration) {//500毫秒内按钮无效，这样可以控制快速点击，自己调整频率
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
