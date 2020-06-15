package com.wd.daquan.third.helper;

import android.text.TextUtils;

import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.bean.GroupInfoBean;
import com.wd.daquan.model.bean.GroupMemberBean;
import com.wd.daquan.model.db.DbSubscribe;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.db.helper.MemberDbHelper;
import com.wd.daquan.model.db.helper.TeamDbHelper;
import com.wd.daquan.model.mgr.ModuleMgr;

/**
 * Created by hzxuwen on 2015/3/25.
 */
public class TeamHelper {

    public static String getTeamName(String teamId) {
        GroupInfoBean groupInfo = TeamDbHelper.getInstance().getTeam(teamId);
        if (groupInfo != null) {
            if (TextUtils.isEmpty(groupInfo.group_name)) {
                return teamId;
            }

            return groupInfo.group_name;
        } else {
            return teamId;
        }
    }

    public static void getTeamMsg(String teamId,DbSubscribe<GroupInfoBean> dbSubscribe) {
        TeamDbHelper.getInstance().getTeam(teamId,dbSubscribe);
    }

    /**
     * 获取显示名称。用户本人显示“我”
     *
     * @param tid
     * @param account
     * @return
     */
    public static String getTeamMemberDisplayName(String tid, String account) {
        if (account.equals(ModuleMgr.getCenterMgr().getUID())) {
            return "我";
        }

        return getDisplayNameWithoutMe(tid, account);
    }

    /**
     * 获取显示名称。用户本人显示“你”
     *
     * @return
     */
    public static String getTeamMemberDisplayNameYou(String tid, String account) {
        if (account.equals(ModuleMgr.getCenterMgr().getUID())) {
            return "你";
        }

        return getDisplayNameWithoutMe(tid, account);
    }

    /**
     * 获取显示名称。用户本人也显示昵称
     * 备注>群昵称>昵称
     */
    public static String getDisplayNameWithoutMe(String tid, String account) {
        Friend friend = FriendDbHelper.getInstance().getFriend(account);
        if (friend != null && !TextUtils.isEmpty(friend.remarks)) {
            return friend.remarks;
        }
        GroupMemberBean memberInfo = MemberDbHelper.getInstance().getTeamMember(tid, account);
        if (memberInfo != null) {
            if (!TextUtils.isEmpty(memberInfo.getRemarks())) {
                return memberInfo.getRemarks();
            } else if (!TextUtils.isEmpty(memberInfo.groupNickname)) {
                return memberInfo.groupNickname;
            }
        }

        return UserInfoHelper.getUserName(account);
    }

    public static boolean isContainer(String tid, String account, String content) {
        Friend friend =FriendDbHelper.getInstance().getFriend(account);
        if (friend != null && !TextUtils.isEmpty(friend.remarks)) {
            return content.contains(friend.remarks);
        }
        GroupMemberBean memberInfo =  MemberDbHelper.getInstance().getTeamMember(tid, account);
        if (memberInfo != null) {
            if (!TextUtils.isEmpty(memberInfo.getRemarks())) {
                return content.contains(memberInfo.getRemarks());
            } else if (!TextUtils.isEmpty(memberInfo.groupNickname)) {
                return content.contains(memberInfo.groupNickname);
            }
        }
        return false;
    }

    /**
     * 获取群头像
     */
    public static String getTeamHeadPic(String teamId) {
        GroupInfoBean groupInfo = TeamDbHelper.getInstance().getTeam(teamId);
        if (groupInfo == null) {
            return null;
        }

        if (TextUtils.isEmpty(groupInfo.group_pic)) {
            return null;
        }

        return groupInfo.group_pic;
    }

    /**
     * 获取群成员头像
     * @param tid
     * @param account
     * @return
     */
    public static String getTeamMemberHeadPic(String tid, String account) {
        GroupMemberBean memberInfo =  MemberDbHelper.getInstance().getTeamMember(tid, account);
        if (memberInfo != null) {
           return memberInfo.headpic;
        }

        return null;
    }

    public static String getMemberCount(String tid){
        GroupInfoBean groupInfo = TeamDbHelper.getInstance().getTeam(tid);
        if (groupInfo == null) {
            return "0";
        }


        return groupInfo.group_member_num;
    }

//    public static String getTeamNick(String tid, String account) {
//        Team team = NimUIKit.getTeamProvider().getTeamById(tid);
//        if (team != null && team.getType() == TeamTypeEnum.Advanced) {
//            TeamMember member = NimUIKit.getTeamProvider().getTeamMember(tid, account);
//            if (member != null && !TextUtils.isEmpty(member.getTeamNick())) {
//                return member.getTeamNick();
//            }
//        }
//        return null;
//    }
}
