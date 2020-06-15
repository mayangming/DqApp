package com.wd.daquan.model.manager;

import com.wd.daquan.model.sp.QCSharedPreTeamInfo;
import com.wd.daquan.model.sp.QCSharedPrefManager;

/**
 * @author: dukangkang
 * @date: 2019/1/20 13:53.
 * @description: todo ...
 */
public class TeamManager {

    private static TeamManager INSTANCE = null;

    private QCSharedPreTeamInfo mSharedPreTeamInfo;


    private static class Holder {
        private static TeamManager instance = new TeamManager();
    }

    public static TeamManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = Holder.instance;
        }
        return INSTANCE;
    }

    private TeamManager() {
        QCSharedPrefManager mSharedPrefManager = QCSharedPrefManager.getInstance();
        mSharedPreTeamInfo = mSharedPrefManager.getKDPreferenceTeamInfo();
    }

    /**
     * 存储时间未领取红包
     */
    public void saveLongTimeRp(String key, String value) {
        mSharedPreTeamInfo.saveString(key + QCSharedPreTeamInfo.LONG_TIME_RED_PACKAGE, value);
    }

    /**
     * 是否开启长时间未领取红包
     * @return
     * true: 开始 false:关闭
     */
    public boolean enableLongTimeRp(String key) {
        String result = mSharedPreTeamInfo.getString(key + QCSharedPreTeamInfo.LONG_TIME_RED_PACKAGE, "0");
        return "1".equals(result);
    }
}
