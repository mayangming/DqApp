package com.wd.daquan.model.mgr;

import android.text.TextUtils;

import com.wd.daquan.model.bean.ConfigBean;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.model.sp.QcSharedPrefApp;

/**
 * @author: dukangkang
 * @date: 2018/11/20 10:36.
 * @description: todo ...
 */
public class ConfigManager {

    private static ConfigManager INSTANCE = null;

    private QcSharedPrefApp mSharedPrefApp;

    private static class Holder {
        private static ConfigManager holder = new ConfigManager();
    }

    public static ConfigManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = Holder.holder;
        }
        return INSTANCE;
    }

    public ConfigManager() {
        mSharedPrefApp = QCSharedPrefManager.getInstance().getSpForApp();
    }

    private ConfigBean getConfigBean(){
        return ModuleMgr.getCommonMgr().getConfigBean();
    }

    /**
     * 配置信息存储
     * @param serverConfig
     */
    public void saveServerConfig(String serverConfig) {
        mSharedPrefApp.saveString(QcSharedPrefApp.SERVER_CONFIG, serverConfig);
    }

    /**
     * 获取配置信息
     */
    public String getServerConfig() {
        return mSharedPrefApp.getString(QcSharedPrefApp.SERVER_CONFIG, "");
    }

    /**
     * 配置信息存储
     * @param serverConfig
     */
    public void saveNewsServerConfig(String serverConfig) {
        mSharedPrefApp.saveString(QcSharedPrefApp.NEW_SERVER_CONFIG, serverConfig);
    }

    /**
     * 获取配置信息
     */
    public String getNewsServerConfig() {
        return mSharedPrefApp.getString(QcSharedPrefApp.NEW_SERVER_CONFIG, "");
    }

    /**
     * 获取服务端配置，分享URL地址
     * @return
     */
    public String getShareUrl() {
        return getConfigBean().share_url;
    }

    /**
     * 是否启用戳一下功能
     * @return
     */
    public boolean enablePoke() {
        String poke = getConfigBean().poke;
        return "1".equals(poke);
    }

    /**
     * 获取小游戏入口ICON名称
     * @return
     */
    public String getGameIcon() {
        return getConfigBean().game_icon;
    }

    /**
     * 获取红包祝福语
     * @return
     */
    public String getGreetings() {
        return getConfigBean().greetings;
    }

    /**
     * 是否存在远程，支付宝收款码
     * @return
     */
    public boolean hasPaymentCode() {
        String paymentUrl = getPaymentPath();
        if(!TextUtils.isEmpty(paymentUrl)) {
            return true;
        }
        return false;
    }

    private static String paymentCodeDir;

    /**
     * 支付宝收款码-远程路径
     * @return
     */
    public String getPaymentPath() {
        return getConfigBean().ali_payment_path;
    }

    /**
     * 保存支付宝收款码
     * @return
     */
    public void savePlaymentPath(String path) {
        getConfigBean().ali_payment_path = path;
    }

    /**
     * 支付宝收款码-上传目录
     * @return
     */
    public String getPaymentDirectory() {
        if (TextUtils.isEmpty(paymentCodeDir)) {
            paymentCodeDir = getConfigBean().ali_payment_directory;
        }
        return paymentCodeDir;
    }
    /**
     * 获取红包雨开关 0：开 1： 关
     */
    public String getRedEnvelopedRainSwitch() {
//        return getKDPreferenceUserInfo().getString(EBSharedPrefUser.redEnvelopedRainSwitch, "");
        return mSharedPrefApp.getString(QcSharedPrefApp.redEnvelopedRainSwitch, "");
    }

    /**
     * 设置红包雨开关 0：开 1： 关
     */
    public void saveRedEnvelopedRainSwitch(String redRainSwitch) {
        mSharedPrefApp.saveString(QcSharedPrefApp.redEnvelopedRainSwitch, redRainSwitch);
    }
}
