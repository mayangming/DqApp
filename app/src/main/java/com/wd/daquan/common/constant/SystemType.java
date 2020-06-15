package com.wd.daquan.common.constant;

/**
 * @author: dukangkang
 * @date: 2018/9/12 11:42.
 * @description:
 * 斗圈消息通知类型
 */
public interface SystemType {
    String MODIFY_PROTRAIT = "3"; // 修改了群头像
    String MODIFY_NOTICE = "4"; // 修改了群公告
    String TEAM_SCREENSHOT_OPEN = "5"; // 群组截屏通知 开启
    String TEAM_SCREENSHOT_CLOSE = "6"; // 群组截屏通知 关闭
    String P2P_SCREENSHOT_OPEN = "9"; // 单聊截屏通知 开启
    String P2P_SCREENSHOT_CLOSE = "10"; // 单聊截屏通知 关闭
    String P2P_SCREENSHOT_OPTION = "11"; //截屏通知 单聊
    String TEAM_SCREENSHOT_OPTION = "12"; // 截屏通知 群聊
    String RECEIVE_TRANSFER = "13"; // 接收转账，此版本不用
    String REMOVE_FRIEND = "14"; // 删除好友通知
    String TEAM_PROHIBITION = "15";// 群组封禁
    String TEAM_UNSEAL = "16";  // 群组解封
    String ACCOUNT_UNSEAL = "17"; // 账户封禁
    String SCAN_ADD_TEAM = "18"; // 扫码进群
    String ADMIN_UPDATE_MINE_NICKNAME = "19"; // 群组修改我在群聊中的昵称
    String ALL_MEMBER_ANEXCUSE = "21"; // 全员禁言
    String TEAM_AUTH_CLOSE = "23"; // 关闭群认证消息
    String TEAM_AUTH_OPEN = "24"; // 开启群认证消息
    String TEAM_PROTECT_OPEN = "25"; // 开启群成员保护模式消息
    String TEAM_PROTECT_CLOSE = "26"; // 关闭群成员保护模式消息
    String RED_RECEIVE_CLOSE = "27"; // 关闭禁止群员收发红包消息
    String RED_RECEIVE_OPEN = "28"; // 开启禁止群员收发红包消息
    String RED_LONG_TIME_UNRECEIVE_OPEN = "29"; // 开启长时间未领取红包
    String RED_LONG_TIME_UNRECEIVE_CLOSE = "30"; // 关闭长时间未领取红包












}
