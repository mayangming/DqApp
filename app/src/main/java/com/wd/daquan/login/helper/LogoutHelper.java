package com.wd.daquan.login.helper;

import android.content.Context;

import com.da.library.tools.ActivitysManager;
import com.dq.im.DqWebSocketClient;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.ui.drop.DropManager;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.login.activity.LoginRegisterActivity;
import com.wd.daquan.mine.mode.MessageNotifyHelper;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;

/**
 * 注销帮助类
 * Created by huangjun on 2015/10/8.
 */
public class LogoutHelper {

    /**
     * 手动，或token过期退出
     */
    public static void logout(Context context) {
        // 清理缓存&注销监听&清除状态
        NimUIKit.logout();
        clearUserInfo();
        DropManager.getInstance().destroy();
        DqToast.showShort(DqApp.sContext.getString(R.string.setting_logout_success));
        MsgMgr.getInstance().sendMsg(MsgType.MT_App_Login, false);
        NavUtils.gotoLoginCodeActivity(context,null);
        ActivitysManager.getInstance().finishAllFilter(LoginRegisterActivity.class);
        DqWebSocketClient.getInstance().cancel();
    }
    /**
     * 注销账号
     */
    public static void logoff(Context context) {
        NimUIKit.logout();
        clearUserInfo();
        DropManager.getInstance().destroy();
        DqToast.showShort(DqApp.sContext.getString(R.string.setting_logoff_success));
        MsgMgr.getInstance().sendMsg(MsgType.MT_App_Login, false);

        NavUtils.gotoLoginPasswordActivity(context);
        ActivitysManager.getInstance().finishAllFilter(LoginRegisterActivity.class);
    }

    /**
     * 同时登录被登出
     */
    public static void logout(Context context, String content) {
        NimUIKit.logout();
        clearUserInfo();
        DropManager.getInstance().destroy();
        DqToast.showShort(content);
        MsgMgr.getInstance().sendMsg(MsgType.MT_App_Login, false);
        NavUtils.gotoLoginPasswordActivity(context);
        ActivitysManager.getInstance().finishAllFilter(LoginRegisterActivity.class);
    }

    /**
     * 清除用户信息
     */
    public static void clearUserInfo() {
        EBSharedPrefUser userInfoSp = QCSharedPrefManager.getInstance().getKDPreferenceUserInfo();
        String phone = userInfoSp.getString(EBSharedPrefUser.phone, "");
        String headpic = userInfoSp.getString(EBSharedPrefUser.headpic, "");
        String password = userInfoSp.getString(EBSharedPrefUser.password, "");
        //消息通知相关 暂时注释 9.12....
        String notify = MessageNotifyHelper.getInstance().getNewMessageNotify();
        String newde = userInfoSp.getString(EBSharedPrefUser.NEWS_DETAIL, "");
        String city = userInfoSp.getString(EBSharedPrefUser.city, "");
        String choicecity = userInfoSp.getString(EBSharedPrefUser.PROVINCE, "");
        String filepath = userInfoSp.getString(EBSharedPrefUser.filePath, "");

        userInfoSp.clear();
        ModuleMgr.getCenterMgr().clear();

        userInfoSp.saveString(EBSharedPrefUser.phone, phone);
        userInfoSp.saveString(EBSharedPrefUser.headpic, headpic);
        userInfoSp.saveString(EBSharedPrefUser.password, password);
        userInfoSp.saveString(EBSharedPrefUser.city, city);
        userInfoSp.saveString(EBSharedPrefUser.PROVINCE, choicecity);
        userInfoSp.saveString(EBSharedPrefUser.msg_notify, notify);
        userInfoSp.saveString(EBSharedPrefUser.NEWS_DETAIL, newde);
        userInfoSp.saveString(EBSharedPrefUser.filePath, filepath);
        ModuleMgr.getCenterMgr().setInstall(true);
    }
}
