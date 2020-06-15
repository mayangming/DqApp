package com.wd.daquan.third.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.wd.daquan.DqApp;
import com.wd.daquan.model.mgr.ModuleMgr;

/**
 * Created by hzxuwen on 2015/4/13.
 */
public class UserPreferences {
    private final static String KEY_DOWNTIME_TOGGLE = "down_time_toggle";
    private final static String KEY_SB_NOTIFY_TOGGLE = "sb_notify_toggle";
    private final static String KEY_TEAM_ANNOUNCE_CLOSED = "team_announce_closed";
    private final static String KEY_STATUS_BAR_NOTIFICATION_CONFIG = "KEY_STATUS_BAR_NOTIFICATION_CONFIG";

    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_USER_DEVICE_ID = "device_id";
    private static final String KEY_QC_USER_TOKEN = "user_token";


    // 测试过滤通知
    private final static String KEY_MSG_IGNORE = "KEY_MSG_IGNORE";
    // 响铃配置
    private final static String KEY_RING_TOGGLE = "KEY_RING_TOGGLE";
    // 呼吸灯配置
    private final static String KEY_LED_TOGGLE = "KEY_LED_TOGGLE";
    // 通知栏标题配置
    private final static String KEY_NOTICE_CONTENT_TOGGLE = "KEY_NOTICE_CONTENT_TOGGLE";

    // 通知栏样式（展开、折叠）配置
    private final static String KEY_NOTIFICATION_FOLDED_TOGGLE = "KEY_NOTIFICATION_FOLDED";

    // 保存在线状态订阅时间
    private final static String KEY_SUBSCRIBE_TIME = "KEY_SUBSCRIBE_TIME";

    public static void setMsgIgnore(boolean enable) {
        saveBoolean(KEY_MSG_IGNORE, enable);
    }

    public static boolean getMsgIgnore() {
        return getBoolean(KEY_MSG_IGNORE, false);
    }

    public static void setNotificationToggle(boolean on) {
        saveBoolean(KEY_SB_NOTIFY_TOGGLE, on);
    }

    public static boolean getNotificationToggle() {
        return getBoolean(KEY_SB_NOTIFY_TOGGLE, false);
    }

    public static void setRingToggle(boolean on) {
        saveBoolean(KEY_RING_TOGGLE, on);
    }

    public static boolean getRingToggle() {
        return getBoolean(KEY_RING_TOGGLE, true);
    }

    public static void setLedToggle(boolean on) {
        saveBoolean(KEY_LED_TOGGLE, on);
    }

    public static boolean getLedToggle() {
        return getBoolean(KEY_LED_TOGGLE, true);
    }

    public static boolean getNoticeContentToggle() {
        return getBoolean(KEY_NOTICE_CONTENT_TOGGLE, false);
    }

    public static void setNoticeContentToggle(boolean on) {
        saveBoolean(KEY_NOTICE_CONTENT_TOGGLE, on);
    }

    public static void setDownTimeToggle(boolean on) {
        saveBoolean(KEY_DOWNTIME_TOGGLE, on);
    }

    public static boolean getDownTimeToggle() {
        return getBoolean(KEY_DOWNTIME_TOGGLE, false);
    }

    public static void setNotificationFoldedToggle(boolean folded) {
        saveBoolean(KEY_NOTIFICATION_FOLDED_TOGGLE, folded);
    }

    public static boolean getNotificationFoldedToggle() {
        return getBoolean(KEY_NOTIFICATION_FOLDED_TOGGLE, true);
    }

    public static void setTeamAnnounceClosed(String teamId, boolean closed) {
        saveBoolean(KEY_TEAM_ANNOUNCE_CLOSED + teamId, closed);
    }

    public static boolean getTeamAnnounceClosed(String teamId) {
        return getBoolean(KEY_TEAM_ANNOUNCE_CLOSED + teamId, false);
    }

    public static void setOnlineStateSubsTime(long time) {
        saveLong(KEY_SUBSCRIBE_TIME, time);
    }

    public static long getOnlineStateSubsTime() {
        return getLong(KEY_SUBSCRIBE_TIME, 0);
    }


    private static boolean getBoolean(String key, boolean value) {
        return getSharedPreferences().getBoolean(key, value);
    }

    private static void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private static void saveLong(String key, long value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putLong(key, value);
        editor.commit();
    }

    private static long getLong(String key, long value) {
        return getSharedPreferences().getLong(key, value);
    }

    static SharedPreferences getSharedPreferences() {
        return DqApp.sContext.getSharedPreferences("daquan." + ModuleMgr.getCenterMgr().getUID(), Context.MODE_PRIVATE);
    }

    private static void saveString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static String getString(String key) {
        return getSharedPreferences().getString(key, null);
    }

    public static void saveUserPassword(String account) {
        saveString(KEY_USER_PASSWORD, account);
    }

    public static String getUserPassword() {
        return getString(KEY_USER_PASSWORD);
    }

    public static String getDeviceId() {
        return getString(KEY_USER_DEVICE_ID);
    }

    public static void saveDeviceId(String device_id) {
        saveString(KEY_USER_DEVICE_ID, device_id);
    }

    public static void saveQCUserToken(String userToken) {
        saveString(KEY_QC_USER_TOKEN, userToken);
    }

    public static String getQCUserToken() {
        return getString(KEY_QC_USER_TOKEN);
    }
}
