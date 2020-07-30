package com.wd.daquan.login.helper;

import android.app.Activity;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.da.library.tools.ActivitysManager;
import com.wd.daquan.DqApp;
import com.wd.daquan.MainActivity;
import com.wd.daquan.R;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.mine.mode.MessageNotifyHelper;
import com.wd.daquan.model.bean.LoginBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;

/**
 * @Author: 方志
 * @Time: 2018/9/11 11:19
 * @Description:
 */
public class LoginHelper {

    /**
     * 未进入MainActivity调用
     */
    public static void login(@NonNull Activity context){

        //网络异常直接跳转main
        if (!DqUtils.isNetworkConnected(DqApp.sContext)) {
            DqToast.showShort(DqApp.sContext.getString(R.string.network_err_please_check_and_try_again));
            gotoMain(context);
            return;
        }
        String account = ModuleMgr.getCenterMgr().getUID();
        String token = ModuleMgr.getCenterMgr().getImToken();
    }
    /**
     * 未进入MainActivity调用
     */
    public static void login(@NonNull Activity context, Boolean isGotoMain){

        //网络异常直接跳转main
        if (!DqUtils.isNetworkConnected(DqApp.sContext)) {
            DqToast.showShort(DqApp.sContext.getString(R.string.network_err_please_check_and_try_again));
            gotoMain(context);
            return;
        }
        String account = ModuleMgr.getCenterMgr().getUID();
        String token = ModuleMgr.getCenterMgr().getImToken();
    }

    /**
     * 未进入MainActivity调用
     */
    public static void login(@NonNull Activity context, @NonNull String account, @NonNull String token){

        //网络异常直接跳转main
        if (!DqUtils.isNetworkConnected(DqApp.sContext)) {
            DqToast.showShort(DqApp.sContext.getString(R.string.network_err_please_check_and_try_again));
            gotoMain(context);
            return;
        }

        gotoMain(context);
    }


    public static void gotoMain(@NonNull Activity context) {
        // 进入主界面
        NavUtils.gotoMainActivity(context);
        ActivitysManager.getInstance().finishAllFilter(MainActivity.class);
        MsgMgr.getInstance().sendMsg(MsgType.MT_App_Login, true);
    }

//    private static void saveLoginInfo(final String account, final String token) {
//        Preferences.saveUserAccount(account);
//        Preferences.saveUserToken(token);
//    }

    /**
     * sp存储个人信息
     */
    public static void saveCurrentUserInfo(LoginBean data, String password) {
        if(null == data) return;

        EBSharedPrefUser userInfoSp = QCSharedPrefManager.getInstance().getKDPreferenceUserInfo();
        userInfoSp.saveString(EBSharedPrefUser.phone, data.phone);
        userInfoSp.saveString(EBSharedPrefUser.token, data.token);
        userInfoSp.saveString(EBSharedPrefUser.nickname, data.nickName);
        userInfoSp.saveString(EBSharedPrefUser.headpic, data.headpic);
        userInfoSp.saveString(EBSharedPrefUser.sex, data.sex);
        userInfoSp.saveString(EBSharedPrefUser.ctime, data.regTime);
        userInfoSp.saveString(EBSharedPrefUser.uid, data.uid);
        userInfoSp.saveString(EBSharedPrefUser.im_token, data.imToken);
        userInfoSp.saveBoolean(EBSharedPrefUser.isLand, true);
        userInfoSp.saveString(EBSharedPrefUser.isVip, data.isVip);
        userInfoSp.saveString(EBSharedPrefUser.vipStartTime, data.vipStartTime);
        userInfoSp.saveString(EBSharedPrefUser.vipEndTime, data.vipEndTime);
//        if (!TextUtils.isEmpty(password)) {
        userInfoSp.saveString(EBSharedPrefUser.password, password);
//        }
        userInfoSp.saveString(EBSharedPrefUser.allow_search, data.allowSearch);
        userInfoSp.saveString(EBSharedPrefUser.NEWS_DETAIL, data.newsDetail);
        userInfoSp.saveString(EBSharedPrefUser.whether_validate, data.addedFriendWhetherV);
        // 设置斗圈号
        userInfoSp.saveString(EBSharedPrefUser.QINGTALK_NUM, data.douquanNum);
        userInfoSp.saveString(EBSharedPrefUser.ALLOW_QINGCHAT_SEARCH, data.allowDouquanSearch);
//        //设置金融魔方token
        userInfoSp.saveString(EBSharedPrefUser.JRMF_TOKEN, data.jrmfToken);
        //登录
        MessageNotifyHelper.getInstance().setNewMessageNotify(data.msgNotify);

        // 保存戳一下消息状态
        ModuleMgr.getCenterMgr().savePoke(data.poke);
        ModuleMgr.getCenterMgr().saveTeamInvite(data.groupValidate);
    }

    /**
     * 是否需要登录
     */
    public static boolean isNeedLogin(){
        String token = ModuleMgr.getCenterMgr().getToken();
        return TextUtils.isEmpty(token);
    }

}
