package com.wd.daquan.model.mgr;

import android.content.Context;

import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.rxbus.ModuleBase;
import com.wd.daquan.model.utils.ProcessUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 模块初始化工具
 * Created by Kind on 2018/10/10.
 */

public class ModuleMgr {

    private static List<ModuleBase> selfModules = new ArrayList<>();

    /**
     * 调用所有逻辑模块
     */
    public static void releaseAll() {
        for (ModuleBase item : selfModules) {
            try {
                item.release();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        selfModules.clear();
        selfModules = null;
    }

    /**
     * 按等级初始化逻辑模块
     */
    public static void initModule(final Context context) {
        //初始化日志打印，每个进程都初始化一次
        if (ProcessUtils.isMainProcess(context)) {
            preInit(context);
        }
    }

    /**
     * 预加载，在进程初始化的时候加载一次
     */
    private static void preInit(Context context) {
        DqToast.init(context);

        getCenterMgr();
        getDbMgr();
        getAppManager();
    }

    /**
     * 将一个新模块添加的管理器中，并调用其init方法
     *
     * @param module 一个新的模块
     */
    private static void addModule(final ModuleBase module) {
        module.init();
        selfModules.add(module);
    }

    private static AppMgr appMgr;
    public static AppMgr getAppMgr() {
        if (appMgr == null) {
            appMgr = new AppMgrImpl();
            addModule(appMgr);
        }
        return appMgr;
    }

    /**
     * 个人模块
     */
    private static CenterMgr centerMgr = null;

    public static CenterMgr getCenterMgr() {
        if (centerMgr == null) {
            centerMgr = new CenterMgr();
            addModule(centerMgr);
        }
        return centerMgr;
    }

    /**
     * 聊天模块
     */
    private static DbMgr dbMgr = null;

    /**
     * 取得应用信息管理的实例的唯一方法，统一从此处获得，全局唯一
     *
     * @return 返回应用管理器的实例
     */
    public static DbMgr getDbMgr() {
        if (dbMgr == null) {
            dbMgr = new DbMgr();
            addModule(dbMgr);
        }
        return dbMgr;
    }

    /**
     * Application 变量存储模块
     */
    private static AppManager sAppManager = null;

    public static AppManager getAppManager() {
        if (sAppManager == null) {
            sAppManager  = new AppManager();
            addModule(sAppManager);
        }
        return sAppManager;
    }

    /**
     * 公共配置模块
     */
    private static CommonMgr sCommonMgr = null;

    public static CommonMgr getCommonMgr() {
        if (sCommonMgr == null) {
            sCommonMgr = new CommonMgr();
            addModule(sCommonMgr);
        }
        return sCommonMgr;
    }
}
