package com.wd.daquan.model.sp;

/**
 * <用户信息缓存>
 */
public class EBSharedPrefUser extends BaseSharedPreference {

    public static final String phone = "phone";
    public static final String token = "token";
    public static final String isVip = "isVip";//是否是会员, 0: 不是会员 1：是会员
    public static final String vipStartTime = "vipStartTime";//会员开始时间
    public static final String vipEndTime = "vipEndTime";//会员结束时间
    public static final String shareTotalNum = "shareTotalNum";//分享总人数
    public static final String redEnvelopedRainRemind = "redEnvelopedRainRemind";//分享总人数
    public static final String weChatIconUrl = "weChatIconUrl";//微信用户头像
    public static final String weChatName = "weChatName";//微信用户昵称
    public static final String nickname = "nickName";
    public static final String headpic = "headpic";
    public static final String sex = "sex";
    public static final String ctime = "regTime";
    public static final String uid = "uid";
    public static final String im_token = "imToken";
    public static final String isLand = "isLand";
    public static final String password="password";
    public static final String filePath="filePath";
    public static final String allow_search="allowSearch";
    public static final String whether_validate="whether_validate";
    public static final String noise="noise";
    public static final String newInfo="newInfo";
    public static final String msg_notify="msgNotify";
    public static final String READED="readed";
    public static final String TEXTSIZE="textsize";
    public static final String vibration="vibration";
    public static final String HOSTNAME="hostname";
    public static final String type="type";
    public static final String NEWS_DETAIL="newsDetail";
    public static final String map="map";
    public static final String remark_nickname="remark_nickname";//备注
    public static final String remark_userId ="remark_userId";//关于备注的id
    public static final String city="city";
    public static final String PROVINCE ="province";
    public static final String savephone="savephone";
    public static final String savehead="savehead";
    public static final String TONGYI="tongyi";
    public static final String UPDATE="update";
    public static final String UPDATE_STATUS = "update_status";//是否需要升级
    public static final String IS_AGREE_PROTOCOL = "isAgreeProtocol";//是否同意服务协议
    public static final String RED_RAIN_CONTENT = "redRainContent";//红包雨内容
    public static final String isShowMakeMoneyTip = "isShowMakeMoneyTip";//是否显示赚钱的新手须知

    public static final String lastTaskSendDraft = "lastTaskSendDraft";//上次发布内容的草稿

    public static final String httpProxy = "httpProxy";//是否显示赚钱的新手须知

    // 公众号
    public static final String QINGTALK_NUM = "dq_num";
    // 允许斗圈号搜索
    public static final String ALLOW_QINGCHAT_SEARCH = "allowDouquanSearch";
    //金融魔方token
    public static final String JRMF_TOKEN = "jrmf_token";
    //金融魔方免责说明
    public static final String JRMF_DISCLAIMER = "jrmf_disclaimer";
    public static String DEVICE_ID = "device_id";

    // 戳一下消息标示
    public static final String POKE_MESSAGE = "poke_message";
    // 是否允许被添加到群组
    public static final String TEAM_INVITE = "team_invite";
    // 通知消息列表, 添加好友／是否允许被
    public static final String NOTIFY_LIST = "notify_list";

    public EBSharedPrefUser(String fileName) {
        super(fileName);
    }
}
