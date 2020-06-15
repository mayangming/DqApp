package com.wd.daquan.third;

import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.model.sp.QcSharedPrefApp;

/**
 * @author: dukangkang
 * @date: 2019/2/20 14:34.
 * @description: todo ...
 */
public class ThirdUtils {

    /**
     * 规定时间内是否可以点击
     *
     * @param unique 自定义唯一标示
     */
    public static boolean isEnableClick(String unique, String tips, int threshold) {
        QcSharedPrefApp sharedPrefApp = QCSharedPrefManager.getInstance().getSpForApp();

        long time = System.currentTimeMillis();
        long lastTime = sharedPrefApp.getLong(QcSharedPrefApp.POKE_ONE_POKE + unique, 0);
        int second = (int) ((time - lastTime) / 1000);
        if (second < threshold) {
            String targetTips = String.format(tips, "" + (threshold - second));
            DqToast.showShort(targetTips);
            return false;
        }
        return true;
    }

    /**
     * 保存戳一下时间戳
     *
     * @param unique
     */
    public static void savePokeTime(String unique) {
        long time = System.currentTimeMillis();
        QcSharedPrefApp sharedPrefApp = QCSharedPrefManager.getInstance().getSpForApp();
        sharedPrefApp.saveLong(QcSharedPrefApp.POKE_ONE_POKE + unique, time);
    }
}
