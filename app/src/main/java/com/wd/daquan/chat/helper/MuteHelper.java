package com.wd.daquan.chat.helper;

/**
 * 静音 免打扰
 * Created by Kind on 2018/9/21.
 */

public class MuteHelper {


    private MuteHelper() {
    }

    private static class MuteHolder {
        static MuteHelper INSTANCE = new MuteHelper();
    }

    public static MuteHelper getInstance() {
        return MuteHolder.INSTANCE;
    }

    public boolean isMute(String account, boolean isTeam) {
        if (isTeam) {
            return isGroupNotify(account);
        } else {
            return isNeedMessageNotify(account);
        }
    }

    /******************群组**********************/
    /**
     * 群
     * 获取当前群是否免打扰
     *
     * @param account
     * @return 接收消息为true， 不接收消息为false
     */
    public boolean isGroupNotify(String account) {
        return true;
    }

    /**
     * 群
     * 设置免打扰
     *
     * @param account
     * @param isNotify
     */
    public void setGroupNotify(String account, boolean isNotify) {
    }

    /******************个人**********************/
    /**
     * 是否提醒该用户发来的消息，false 为静音（不提醒）
     *
     * @param account
     * @return
     */
    public boolean isNeedMessageNotify(String account) {
        return true;
    }

    public void setNeedMessageNotify(String account, boolean isNotify) {
    }
}
