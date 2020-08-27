package com.wd.daquan.model.rxbus;

/**
 * 主线订阅消息类型
 * Created by Kind on 2018/9/20.
 * MT_System
 * MT MsgType
 * System 模块
 * MT_CONTACT
 */

public class MsgType {

    /*************系统********************/
    /**
     * 系统相关的在这里写
     */
    public static final String MT_System = "MT_System";


    /**
     * 应用相关的消息类型
     * 登录、退出登录消息。true登录；false退出。
     */
    public static final String MT_App_Login = "MT_App_Login";


    /*************群********************/
    /**
     * 群设置名称
     */
    public static final String MT_GROUP_SETTING_NAME = "mt_group_setting_name";

    /**
     * 群设置公告
     */
    public static final String MT_GROUP_SETTING_ANNOUNCEMENT = "mt_group_setting_announcement";

    /**
     * 群设置我在群的信息
     */
    public static final String MT_GROUP_SETTING_MY_GROUP_INFO = "mt_group_setting_my_group_info";

    /**
     * 群设置置顶变更通知
     */
    public static final String MT_GROUP_SETTING_TOP_CHANGE = "mt_group_setting_top_change";

    /**
     * 群设置免打扰
     */
    public static final String MT_GROUP_SETTING_NOTIFY_MUTE = "MT_GROUP_SETTING_NOTIFY_MUTE";

    /**
     * 群设置成员增减变更
     */
    public static final String MT_GROUP_SETTING_MEMBER_CHANGE = "mt_group_setting_member_change";

    /**
     * 群设置管理员
     */
    public static final String MT_GROUP_SETTING_ADMIN_CHANGE = "mt_group_setting_admin_change";

    /**
     * 群设置新群主
     */
    public static final String MT_GROUP_SETTING_NEW_LORD_CHANGE = "mt_group_setting_new_lord_change";

    /**
     * 退出该群
     */
    public static final String MT_GROUP_SETTING_QUIT = "mt_group_setting_quit";

    /**
     * 群通知
     */
    public static final String MT_GROUP_NOTICE = "MT_GROUP_NOTICE";

    /**
     * 搜索群助手列表更新
     */
    public static final String MT_GROUP_UPDATE_AIDES = "mt_group_update_aides";

    /**
     * 删除群助手
     */
    public static final String MT_GROUP_DELETE_AIDES = "mt_group_delete_aides";

    /**
     * 显示群助手layout
     */
    public static final String MT_CONVERSATION_GROUP_ASSIST = "mt_conversation_group_assist";

    /*************消息********************/

    /**
     * 首次加载
     */
    public static final String MT_MESSAGE_FIRST_LOAD_INFO = "mt_message_first_load_info";


    /*************消息********************/

    /**
     * 个人资料变更
     */
    public static final String MT_CENTER_PERSONALINFO_CHANGE = "mt_center_personalinfo_change";


    /*******************************  联系人 ***********************************
    /**
     * 联系人，未读消息数
     */
    public static final String MT_CONTACT_UNREAD = "mt_contact_unread";

    /**
     * 联系人，添加好友通知
     */
    public static final String MT_CONTACT_NOTIFY = "mt_contact_notify";
//    /**
//     * 更新联系人列表，加入好友黑名单，删除联系人
//     */
//    public static final String MT_CONTACT_LIST_UPDATE = "mt_contact_list_update";


    /**
     * 发送@所有人-消息
     */
    public static final String MT_AIT_ALL_NOTICE = "mt_group_notice_ait_all";
    /**
     * 草稿信息
     */
    public static final String MT_DRAFT_STATUS = "mt_draft_status";



    /******************** 好友 ******************
    /**
     * 修改好友备注
     */
    public static final String MT_FRIEND_REMARKS_CHANGE = "mt_friend_remarks_change";
    /**
     * 好友移除黑名单
     */
    public static final String MT_FRIEND_REMOVE_BLACK_LIST = "mt_friend_remove_black_list";
    /**
     * 好友加入黑名单
     */
    public static final String MT_FRIEND_ADD_BLACK_LIST = "mt_friend_add_black_list";
    /**
     * 添加好友
     */
    public static final String MT_FRIEND_ADD_FRIEND = "mt_friend_add_friend";
    /**
     * 删除好友
     */
    public static final String MT_FRIEND_REMOVE_FRIEND = "mt_friend_remove_friend";
    /**
     * 最近联系人finish
     */
    public static final String MT_RECENT_CONTACTS_FINISH = "mt_recent_contacts_finish";

    /******************** Web端登录 ******************/
     public static final String MT_WEB_NOTICE = "mt_web_notice";

    /** -------------------- 钱包 MT_WALLET ---------------------- **/
    /**
     * 充值成功
     */
    public static final String MT_WALLET_RECHARGE = "mt_wallet_recharge";
    /**
     * 绑定银行卡
     */
    public static final String MT_WALLET_BIND_CARD = "mt_wallet_recharge";

    /**
     * 更新群组信息
     */
    public static final String MT_TEAM_UPDATE_TEAM_INGO = "mt_team_update_team_ingo";

    /**
     * 二维码识别
     */
    public static final String MT_QR_CODE = "mt_qr_code";


    /** -------------------  全局消息提示  ----------------------- **/

    /**
     * 红包雨开始半小时提醒
     */
    public static final String APPLICATION_RED_RAIN_HALF = "application_red_rain_half";
    /**
     * 红包雨开始提醒
     */
    public static final String APPLICATION_RED_RAIN_START = "application_red_rain_start";

    /**
     * vip兑换成功后数据变动
     */
    public static final String VIP_EXCHANGE_CHANGE = "vipExchange";

    /**
     * 红包发送中用到的微信支付
     */
    public static final String RED_PACKAGE_PAY = "redPackagePay";


    public static final String P2P_MESSAGE_CONTENT = "P2PImMessage";//个人聊天消息

    public static final String TEAM_MESSAGE_CONTENT = "TeamImMessage";//群组聊天消息

    /** --------------------- 定制消息 ------------------------------- **/

    public static final String CHAT_RED_PACKAGE = "chatRedPackageMsg";//聊天红包消息

    public static final String CHAT_PICTURE = "chatPictureMsg";//聊天图片消息

    public static final String CHAT_VIDEO = "chatVideoMsg";//聊天视频消息

    public static final String CHAT_VOICE = "chatVoiceMsg";//聊天音频消息

    public static final String CHAT_TEXT = "chatTextMsg";//聊天文本消息

    public static final String CLEAR_MSG = "clearMsg";//清空消息

    public static final String SEND_MSG = "sendMsg";//发送消息

    public static final String GROUP_REMOVE = "groupRemove";//群解散

    public static final String UPDATE_READ_PACKAGE_RAIN_LABEL = "redPackageRainLabel";//红包雨标识

    public static final String MESSAGE_RECEIVE_CALL_BACK = "messageReceiveCallBack";//消息发送后，服务器把消息回传给客户端的内容
    public static final String MESSAGE_RECEIVE_CALL_BACK_TIMEOUT = "messageReceiveCallBackTimeOut";//消息发送后，超时
    /**
     * 更新首页消息内容，比如头像、名字
     */
    public static final String HOME_UPDATE_MSG = "updateHomeMessage";

    public static final String TASK_OBJ = "taskObj";//任务刷新标志

    public static final String CLEAR_UNREAD_AREA = "clearUnReadDynamic";//清空朋友圈内容

    /**
     * 创建任务后的事件监听
     */
    public static final String TASK_PAY_RESULT = "createTaskPayResult";
    /**
     * 退款后进行页面刷新
     */
    public static final String TASK_REFUND = "taskRefund";
}
