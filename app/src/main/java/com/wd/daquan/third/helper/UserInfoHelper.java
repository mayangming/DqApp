package com.wd.daquan.third.helper;

import android.text.TextUtils;

import com.dq.im.type.ImType;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.db.DbSubscribe;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.mgr.ModuleMgr;

/**
 * @author: dukangkang
 * @date: 2018/9/20 19:42.
 * @description: todo ...
 */
public class UserInfoHelper {
    // 获取用户显示在标题栏和最近联系人中的名字
    public static String getUserTitleName(String id, ImType sessionType) {
        if (sessionType == ImType.P2P) {
//            if (ModuleMgr.getCenterMgr().getUID().equals(id)) {
//                return "我的电脑";
//            } else {
                return getUserDisplayName(id);
//            }
        } else if (sessionType == ImType.Team) {
            return TeamHelper.getTeamName(id);
        }
        return id;
    }

    /**
     * @param account 用户帐号
     * @return
     */
    public static String getUserDisplayName(String account) {
        Friend friend = FriendDbHelper.getInstance().getFriend(account);
        if (friend == null) {
            return account;
        }
        return friend.getName();
    }

    // 获取用户原本的昵称
    public static String getUserName(String account) {
        Friend friend = FriendDbHelper.getInstance().getFriend(account);
        if (friend == null) {
            return account;
        }

        if (!TextUtils.isEmpty(friend.friend_remarks)) {
            return friend.friend_remarks;
        }

        if (!TextUtils.isEmpty(friend.nickname)) {
            return friend.nickname;
        }

        return account;
    }

    // 获取用户原本的昵称
    public static String getUserNickName(String account) {
        Friend friend = FriendDbHelper.getInstance().getFriend(account);
        if (friend == null) {
            return account;
        }

        if (!TextUtils.isEmpty(friend.nickname)) {
            return friend.nickname;
        }

        return account;
    }

    /**
     * 获取用户头像
     * @param account
     * @return
     */
    public static String getHeadPic(String account) {
        Friend friend = FriendDbHelper.getInstance().getFriend(account);
        if (friend == null) {
            return account;
        }

        if (!TextUtils.isEmpty(friend.headpic)) {
            return friend.headpic;
        }

        return account;
    }

    /**
     * 当数据库数据改变时候会触发这里回调
     * @param account
     * @param dbSubscribe
     */
    public static void getUserInfo(String account, DbSubscribe<Friend> dbSubscribe){
        FriendDbHelper.getInstance().getFriend(account, dbSubscribe);
    }

    /**
     * 判断用户是否是VIP
     * @param account
     * @return
     */
    public static boolean getUserVipStatus(String account) {
        Friend friend = FriendDbHelper.getInstance().getFriend(account);
        if (friend == null) {
            return false;
        }

        return friend.isVip();
    }

    /**
     * @param account         账号
     * @param selfNameDisplay 如果是自己，则显示内容
     * @return
     */
    public static String getUserDisplayNameEx(String account, String selfNameDisplay) {
        if (account.equals(ModuleMgr.getCenterMgr().getUID())) {
            return selfNameDisplay;
        }

        return getUserDisplayName(account);
    }

    /**
     * @param account         账号
     * @return 是否是好友
     */
    public static boolean isFriend(String account) {
        Friend friend = FriendDbHelper.getInstance().getFriend(account);

        return "0".equals(friend.whether_friend);
    }

}
