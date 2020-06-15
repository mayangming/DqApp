package com.wd.daquan.third.helper;

import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.model.sp.QcSharedPrefApp;
import com.wd.daquan.model.log.DqToast;

/**
 * @Author: 方志
 * @Time: 2019/5/6 14:11
 * @Description:
 */
public class AliPaymentCodeHelp {


    /**
     * 判断是否允许Click
     *
     * @param unique
     * @param key
     * @param tips
     * @param threshold
     * @return
     */
    public static boolean isEnableClick(String unique, String key, String tips, int threshold) {
        QcSharedPrefApp spForApp = QCSharedPrefManager.getInstance().getSpForApp();

        long time = System.currentTimeMillis();
        long lastTime = spForApp.getLong(key + unique, 0);
        int second = (int) ((time - lastTime) / 1000);
        if (second < threshold) {
            String targetTips = String.format(tips, "" + (threshold - second));
            DqToast.showShort(targetTips);
            return false;
        }
        return true;
    }

    /**
     * 保存点击时间戳
     *
     * @param key
     * @param unique
     */
    public static void saveClickTime(String key, String unique) {
        long time = System.currentTimeMillis();
        QcSharedPrefApp spForApp = QCSharedPrefManager.getInstance().getSpForApp();
        spForApp.saveLong(key + unique, time);
    }
}
