package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * @author: dukangkang
 * @date: 2018/6/15 11:50.
 * @description:
 *  群组获取单人信息／直接获取单人信息结构体
 */
public class UserBean implements Serializable {

    public String uid;
    public String sex; //1 男2 女
    public String nickname;
    public String headpic;
    // 斗圈号
    public String chuiniu_num = "";
    // 是否开启群成员保护
    public String is_protect_groupuser = "0";
    // 好友备注
    public String friend_remarks;
    // 是否是好友 0 非 1 是
    public String whether_friend;
    // 是否是黑名单 0 非 1 是
    public String whether_black;
    // 来源
    public String source = "";
    // 自己是不是管理员或群主
    public String own_master; // 0 群员 1 管理员 2 群主
    // 目标用户，在群中角色
    public String target_master; // 0 群员 1 管理员 2 群主
    // 好友是否是管理员或者群主
    public String master;   // 0 群员 1 管理员 2 群主
    //单人: 备注, 群组:群备注
    public String remarks;
    // 手机号
    public String phone;
    // 支付宝账号
    public String alipay_account;
    // 微信账号
    public String wechat_account;

    // 阅后即焚
    public String burn; //0 非1 是，此功能去除
    // 通知截屏
    public String screenshot_notify; //0 非 1 是
    // 真实姓名
    public String truename;




//目前没有看到返回，应该是登录成功后提供
//    public String token;
//    public int setPwd;
//    public String newsDetail = "";
//    public String addedFriendWhetherV;
//    public String regTime;
//    public String allowSearch; // 允许通过手机号搜索
//    public String type;
//    public int whether;
//    public String msgNotify;
//
//    // 允许斗圈账号搜索
//    public String allowDouquanSearch = "";
//
//
//    public String groupMemberPortraitUri;

}
