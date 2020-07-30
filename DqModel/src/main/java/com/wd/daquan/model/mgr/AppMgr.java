package com.wd.daquan.model.mgr;

import androidx.fragment.app.FragmentActivity;
import com.wd.daquan.model.rxbus.ModuleBase;

/**
 * 应用信息
 * Created by Kind on 2019/3/8.
 */
public interface AppMgr extends ModuleBase {

    /**
     * 获取包名
     *
     * @return 安装包名
     */
    String getPackageName();

    /**
     * 获取应用的版本号
     *
     * @return 版本号，如果没有获取到返回0
     */
    int getVerCode();

    /**
     * 获取应用的版本名
     *
     * @return 版本名，如果没有获取到返回""
     */
    String getVerName();

//    /**
//     * 获取IMEI
//     * @return
//     */
//    String getIMEI();


    /**
     *  获取应用程序名称
     *
     * @return 程序名称 ，如果没有获取到返回""
     */
    String getAppName();


    /**
     * 获取屏幕宽度
     */
    int getScreenWidth(FragmentActivity activity);

    /**
     * 获取屏幕高度
     */
    int getScreenHeight(FragmentActivity activity);


}
