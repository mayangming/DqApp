package com.wd.daquan.common.constant;

/**
 * @author: dukangkang
 * @date: 2018/4/20 11:00.
 * @description: 意图间传递数据时，键-值
 */
public class KeyValue {

    public static final String ID = "id";
    // 群组-ID
    public static final String GROUP_ID = "group_id";
    // 群组-昵称
    public static final String GROUP_REMARK = "group_remark";
    // 群组-跳转到搜索组员界面
    public static final String GROUP_MEMBER_LIST = "group_member_list";
    // 群组-返回消息对象
    public static final String GROUP_RESPONSE = "group_response";

    public static final String KEY_ACCOUNT = "account";

    public static final String KEY_SESSION_TYPE = "session_type";

    public static final String OSS_DIRECTION = "web_media/";//oss基础路径

    // 组装支付宝红包实体
    public static final String KEY_ALIPAY_MESSAGE_ENTITY = "alipay_message_entity";
    public static final String ENTER_TYPE = "enter_type";
    public static final int NET_ERR = -1111;

    public interface Carema {
        String KEY_IMAGE_PATH = "image_path";
        String KEY_VIDEO_PATH = "video_path";
        String KEY_VIDEO_DURATION = "video_duration";
        String KEY_VIDEO_FILE_SIZE = "video_filesize";
        String KEY_VIDEO_ROTATE = "video_rotate";
    }


    public interface Team {
        // 加入群聊
        String ADD = "0";
        // 解散
        String DISBANDED = "1";
        // 群封禁
        String BLOCKED = "2";
    }

    public interface Group {
        String GROUP_UID = "group_uid";
        String GROUP_PIC = "group_pic";
        String GROUP_NAME = "group_name";
        String SHOW_GROUP_MEMBER = "showGroupMember";
        String NOTICE = "notice";
        String IS_ADDADDRESS_BOOK = "is_addAddress_book";
    }


//    /**
//     * 语音通话
//     */
//    public interface VoiceMessage {
//        public static final String UID = "uid";
//        public static final String GROUP_ID = "groupId";
//        public static final String NICKNAME = "nickName";
//        public static final String MESSAGE = "content";
//        public static final String EXTRA = "extra";
//    }

    /**
     * 小视频
     */
    public interface VideoMessage {
        public static final String UID = "uid";
        // 文件大小
        public static final String FILE_SIZE = "file_size";
        public static final String GROUP_ID = "groupId";
        // 附加信息
        public static final String EXTRA = "extra";
        // 存储空间
        public static final String BUCKET = "bucket";
        // 文件网络地址
        public static final String FILE_WEBURL = "fileWebUrl";
        // 文件网络绝对地址
        public static final String FILE_WEB_HTTPURL = "fileWebHttpUrl";
        // 网络资源唯一标示
        public static final String ETAG = "etag";
        // 第一帧图片
        public static final String FIRST_FRAME_IMAGE = "firstFrameImage";
        // 第一帧图片,本地存储路径
        public static final String FIRST_FRAME_LOCAL_PATH = "firstFrameLocalPath";
        // 视频总时长
        public static final String DURATION = "duration";
        // 本地视频路径，android暂时不用
        public static final String LOCALPATH = "localPath";
        // 本地视频录制方向
        public static final String ORIENTATION = "orientation";
        // 录制视频的角度
        public static final String ROTATE = "rotate";
        // 本地第一帧图片路径
        public static final String LOCAL_FIRST_FRAME_IMAGE = "local_first_frame_image";
    }

    public interface FileType {
        public static final String IMAGE = "image";
        public static final String GIF = "gif";
        public static final String VIDEO = "video";
        public static final String DOWNLODA = "download";
        public static final String AUDIO = "audio";
    }

    // GIF表情
    public interface ExpressionMessage {
        public static final String ID = "id";
        public static final String REMOTE_PATH = "remote_path";
        public static final String FILECODE = "filecode";
        public static final String PIXH = "pixH";
        public static final String PIXW = "pixW";
        public static final String EXTRA = "extra";
    }

    /**
     * 系统消息
     */
    public interface SystemMessage{
        String UID = "uid";
        String GROUP_ID = "groupId";
        String NICKNAME = "nickName";
        String MESSAGE = "content";
        String EXTRA = "extra";
        String SOURCE_UID = "source_uid";
        String REMARKS = "remarks"; // 群组备注
        String CNEXTRA = "cnExtra"; // 是否阅后即焚
        String SOURCE_NICKNAME = "source_nickname";
        String GROUP_NAME = "groupName";
        String GROUP_PIC = "groupPic";
    }

    /**
     * 红包消息
     */
    public interface RedPacketMessage {
        String BLESSING = "blessing";
        String RECEIVE_ID = "receiveId";
        String RECEIVE_NAME = "receiveName";
        String RECEIVE_PIC = "receivePic";
        String SEND_ID = "sendId";
        String SEND_NAME = "sendName";
        String SEND_PIC = "sendPic";
        String REDPACKET_ID = "redpacketId";
        String TYPE = "type";
        String SIGNATURE = "signature";
        String IS_CLIENT_SEND = "isClientSend";
        String REDPACKET_EXTRA = "redpacket_extra";
        String RESEND_SIGN = "resendSign";
        String EXTRA = "extra";
    }

    /**
     * 红包消息提醒
     */
    public interface RpNoticeMessage {
        String REDPACKET_ID = "redpacketId";
        String SEND_ID = "sendId";
        String NICKNAME = "nickName";
        String HEADPIC = "headpic";
        String GREETINGS = "greetings";
        String ISSURPLUS = "isSurplus";
        String RECEIVE_NAME = "receiveName";
        String RECEIVE_UID = "receiveUid";
        String TYPE = "type";
        String EXTRA = "extra";
        String GROUP_ID = "group_id";
        String SIGNATURE = "signature";
    }

    /**
     * 红包退回消息
     */
    public interface RpRefundMessage {
        String TITLE = "title";//标题
        String START_TIME = "start_time";//到账时间
        String AMOUNT = "amount";//退换金额
        String REASON = "reason";//退款原因
        String REMAK = "remak";//备注
        String END_TIME = "end_time";//退还时间
        String TYPE = "type";
        String BANKNAME = "bankname";//银行
        String CARDNO = "cardno";//卡号
        String CREATE_NICKNAME = "create_nickname";
        String RECEIPT_ID = "receipt_id";
    }

    /**
     * 联系人相关，添加好友，好友添加成功
     */
    public interface ContactMessage {
        String OPERATION = "type";
        String SOURCE_USERID = "sourceUserId";
        String TARGET_USERID = "targetUserId";
        String MESSAGE = "content";
        String EXTRA = "extra";
        // 添加好友消息成功
        String TARGET_ID = "targetId";
        String TARGET_NICKNAME = "targetNickname";
    }

    /**
     * 认证
     */
    public interface CertificationMessage {
        String GROUP_ID = "group_id";
        String SOURCE_UID = "source_uid";
        String SOURCE_NICKNAME = "source_nickname";
        String MESSAGE = "content";
    }

    /**
     * 个人系统消息
     */
    public interface UserSystemMessage {
        String TYPE = "type"; //系统消息类型，0添加好友成功，后续1,2,往后扩展
        String TO_UID = "to_uid";//接受者id
        String TO_NICKNAME = "to_nickname";//接受者名称
        String CONTENT = "content";//消息需要描述的内容
    }

    public interface TeamMessage {
        String OPERATORNAME = "operatorName";
        String RECEIVERNAME = "receiverName";
        String TYPE = "type";
        String DATA = "intentUrl";
        String CONTENT = "content";
        String EXTRA = "extra";
    }

    public interface TeamAuditMessage {
        String GROUP_ID = "group_id";
        String SOURCE_UID = "source_uid";
        String SOURCE_NICKNAME = "source_nickname";
        String MESSAGE = "content";
    }

    public interface ImageMessage {
        String OBJECT_KEY = "object_key";
        String LOCAL_PATH = "local_path";
    }

    public interface WebMessage {
        String OS = "os";
        String TYPE = "type";
    }

    /**
     * 戳一下消息参数
     */
    public interface PokeMessage {
        // 用户ID
        String SENDER_ID = "senderId";
        // 用户名称
        String SENDER_NAME = "senderName";
        // 群组ID
        String TARGET_ID = "targetId";
        // 群组名称
        String TARGET_NAME = "targetName";
        // 用户头像或群组头像
        String PORTRAIT_URL = "portraitUrl";
        // 消息内容
        String CONTENT = "content";
        // 如果是群组，群组成员ID列表
        String UID_LIST = "uidListString";
        // 预留其他字段扩展
        String EXTRA = "extra";
    }

    public interface Poke {
        String POKE_ENTITY = "poke_entity";
        String POKE_MESSAGE_CONTENT = "poke_message_content";
        // 是否是群组
        String POKE_IS_GROUP = "poke_is_group";
    }


    public interface AliOss {
        String OBJECT_KEY = "objectKey";
    }

    public interface RedPacket {
        String REDPACKET_ID = "redpacket_id";
        String MESSAGE = "content";
        String SIGNATURE = "signture";


        String TYPE = "type";
        String GROUP_ID = "group_id";
        String STYLE = "style";
        String RECEIVE_UIDS = "receive_uids";
        String GREETINGS = "greetings";
        String AMOUNT = "amount";
        String NUM = "num";
        String REDPACKET_EXTRA = "redpacket_extra";
        String PWD_PAY = "pwd_pay";
        String FRMS_IMEI = "frms_imei";
        String SOURCE = "source";
        String PKG_NAME = "pkg_name";
        String APP_NAME = "app_name";

    }

    public interface RedpacktRob {
        String IS_OPEN = "is_open";
        String TYPE = "type";
        String PAY = "pay";
//        String MESSAGE = "content";
        String TARGET_ID = "target_id";
        String ATTACHMENT = "attachment";
        //
        String UNRECEIVE = "unreceive";

        int RESPONSE = 456;
    }

    /**
     * 小助手系统消息
     */
    public interface AssistantSystem {
        String TITLE = "title";
        String CREATE_TIME = "create_time";
        String MESSAGE = "content";
    }

    public interface TeamInvite {
        String SOURCE_USERID = "sourceUserId";
        String GROUP_ID = "group_id";
    }

    /**
     * 点击会话列表内，图片时使用
     */
    public interface Picture {
        String KEY_IS_CURRENT = "key_is_current";
        String KEY_ENTITY = "key_entity";
        String KEY_MESSAGE_ID = "key_message_id";
        String KEY_POSITION = "key_position";
        String KEY_CURRENT_MESSAGEID = "key_current_messageid";
        String KEY_SHARE_ELEMENT_MARK = "cn_image_";
        String KEY_SHARE_ELEMENT_VIDEO_MARK = "cn_video_";
    }

    public interface SelectFriend {
        // 支付宝群组红包选择联系人
        String KEY_SELECTED_USERIDS = "selected_userids";
        String KEY_IS_ALL_MEMBER = "is_all_member";
        String KEY_SELECTED_LIST = "selected_list";
        String KEY_WITHOUT_SELF = "without_self";
    }

    public interface Code {
        int NET_ERR = -111;
//        ERROR_EXISTSUSER(10001, "已存在用户"),
//        ERROR_10002(10002, "手机验证码错误"),
//        ERROR_10004(10004, "手机验证码过期"),
//        ERROR_10005(10005, "密码错误"),
//        ERROR_10007(10007, "查无此用户"),

        //用户token认证失败
        int TOKEN_ERR = 10008;
//        ERROR_10006(10006, "用户冻结"),


//        ERROR_10010(10010, "用户不允许通过手机号搜索");

//        ERROR_10003(10003, "ERR", "token_key验证失败");

        //被添加用户开启验证返回
        int ADD_FRIEND_ERR = 10009;

        /**
         * --------------------------------群组-----------------------------
         */
        int DISMISS_GROUP_ERR = 10100;
    }

    public final class Role {
        // 成员
        public static final String MEMBER = "0";
        // 群主
        public static final String MASTER = "2";
        // 管理员
        public static final String ADMIN = "1";
    }

    // @成员
    public interface Ait {
        String ENTITY = "friend_entity";
    }

    public interface Exclusive {
        // 专属名称
        String EXCLUSIVE_NAME = "exclusive_name";
        // 专属ID
        String EXCLUSIVE_ID = "exclusive_id";
    }

    /**
     * 群管理相关字段
     */
    public interface GroupManager {
        String GROUP_ID = "group_id";
    }

    /**
     * 转帐
     */
    public interface Transfer {
        String ATTACHMENT = "attachment";
        String TRANSFERSTATUS = "transferStatus";
        String FRIEND = "friend";
        String UID = "uid";
        String AVATER = "avater";
        String NAME = "name";

        String AMOUNT = "amount";
        String GREETINGS = "greetings";
        String TO_UID = "to_uid";
        String PWD_PAY  = "pwd_pay";
        String FRMS_IMEI   = "frms_imei";
        String SOURCE = "source";
        String PKG_NAME = "pkg_name";
        String APP_NAME = "app_name";


        String VERIFY_CODE = "verify_code";
        String VERIFY_TOKEN = "verify_token";

        String OWN_ORDER_ID = "own_order_id";
        String SUFFIX = "suffix";


    }



    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;
    public static final int SEVEN = 7;
    public static final int EIGHT = 8;
    public static final int NINE = 9;
    public static final int TEN = 10;
    public static final int SIXTEEN = 16;
    public static final String ZERO_STRING = "0";
    public static final String ONE_STRING = "1";
    public static final String TWO_STRING = "2";
    public static final String THREE_STRING = "3";
    public static final String FOUR_STRING = "4";
    public static final String FIVE_STRING = "5";
    public static final String SIX_STRING = "6";
    public static final String SEVEN_STRING = "7";
    public static final String EIGHT_STRING = "8";
    public static final String NINE__STRING = "9";
    public static final String TEN_STRING = "10";
    public static final String STRING = "String";
    public static final String NEW_MESSAGE_VOICE_ON = "btnoise";//新消息声音开
    public static final String NEW_MESSAGE_VOICE_OFF = "nbt";//新消息声音关
    public static final String NEW_MESSAGE_SHAKE_ON = "vibtrue";//新消息震动开
    public static final String NEW_MESSAGE_SHAKE_OFF = "vibfalse";//新消息震动开
    public static final String PASSWORD="password";
    public static final String EXIT = "exit";
    public static final String ABOUT_FREEZE_TYPE = "freeze_type";
    public static final String PHONE = "phone";
    public static final int PHOTO_REQUEST_GALLERY = 101;//聊天背景跳相册选择
    public static final int PHOTO_REQUEST_CUT = 102;//聊天背景剪切
    public static final int PHOTO_REQUEST_LOCAL = 103;//聊天背景选择给定的六张图片
    public static final String CHAT_BG_TYPE = "chat_bg_type";//聊天背景选择的类型
    public static final String PERSONAL_SETTING_TYPE = "personal_setting";//个人信息设置的类型
    public static final String CHANGEINFO = "change_user_info";//修改个人信息
    public static final String APPVERSION = "appversion";
    public static final String PKG_NAME = "pkg_name";
    public static final String APP_NAME = "app_name";
    public static final String ANDROID = "ANDROID";


    public static class IntentCode {
        // 退出状态码
        public static final int REQUEST_CODE_EXIT = 500;
        public static final int RESPONSE_CODE_EXIT = 501;

        public static final int RESPONSE_CODE_SELECT_FRIEND = 101;

    }
    /**
     * 头像处理
     */
    public static class HeadPortrait {
        // 选择拍照
        public static final int TYPE_CAMERA = 1;
        // 选择相册
        public static final int TYPE_PICTURE = 2;
        // 图片裁剪
        public static final int TYPE_CROP = 3;
        //删除图片
        public static final int TYPE_DEL_IMG = 4;
    }
    public static final String SERVICE_VERSION = "service_version";
    public static final String LENGTH = "length";
    public static final String PAGE = "page";
    public static final String YEAR = "year";
    public static final String TYPE = "type";
    public static final String REDTYPE = "redtype";//红包详情
    public static final int TARGET_VALUE = 2000000;//二维码中用到
    /**
     * 二维码前缀
     * https://www.meetsn.com/qcqrcode?qr=
     */
//    public static final String RECEIPT_QR_CODE= DqUrl.H5_QRCODE + "/"+ KeyValue.QR.KEY_UNIQUE + "?"+KeyValue.QR.DQ;
    public static final String RECEIPT_QR_CODE = KeyValue.QR.KEY_UNIQUE + "?"+KeyValue.QR.DQ;
    public static class QR {
        // 跳转到二维码识别加好友还是跳转
        public static final String KEY_UNIQUE = "dqqrcode";
        // 二维码键值
        public static final String DQ = "dq=";
        public static final String MONEY = "&money=";
        // 二维码参数
        public static final String UID = "uid";
        public static final String TYPE = "type";
        public static final String GROUP_ID = "group_id";
        public static final String SOURCE_UID = "source_id";//邀请人id
        public static final String Add_GROUP_BEAN = "add_group_bean";
        //web登录cnqrcode
        public static final String KEY_CN_QRCODE = "dqqrcode";
        public static final String KEY_QC_WEB_QRCODE = "qc_web_qrcode";
        public static final String KEY_WEB_QRUUID = "qruuid";
        public static final String KEY_WEB_TYPE = "typs";

        public static final String CLIENT_TYPE = "client_type";
        public static final String APP = "app";
    }

    public static final String CONTACTS = "contacts";
    public static final String GROUP_STATUS = "";
    public static final String USER_HEAD = "userhead";
    public static final String DEFAULT = "default";
    public static final String UPDATE = "update";
    public static final String EXTRA = "extra";
    public static final String QR_ENTITY = "qr_entity";
    public static final String SCREENSHOT_NOTIFY = "screenshot_notify";
    public static final String OK = "ok";
    public static final String CONFIG = "config";
    public static final String IS_DEBUG = "isDebug";
    public static final String TARGET_ID = "TargetId";
    public static final String QUIT_GROUP = "quit_group";
    public static final String GROUPID = "groupID";
    public static final String GROUPNAME = "groupName";
    public static final String GROUP_NOTICE_MODIFY_TIME = "group_notice_modify_time";
    public static final String GROUP_ANNOUNCEMENT = "group_announcement";
    public static final String GROUP_MODIFY_ANNOUNCEMENT = "group_modify_announcement";
    public static final String REMARKS = "remarks";
    public static final String WECHAT_ACCOUNT = "wechat_account";
    public static final String ALIPAY_ACCOUNT = "alipay_account";
    public static final String DESCRIPTION = "description";
    public static final String SELECTED_MEMBER = "SELECTED_MEMBER";
    public static final String CHOICEGROUP="choiceGroup";
    public static final String IS_ADD_GROUP_MEMBER = "isAddGroupMember";
    public static final String ADD_GROUP_MEMBER = "addGroupMember";
    public static final String LISTS = "lists";
    public static final String ADD = "add";
    public static final String IS_DELETE_GROUP_MEMBER = "isDeleteGroupMember";
    public static final String IS_MASTERS = "isMaster";
    public static final String REQUEST_ID = "request_id";
    public static final String STATUS = "status";
    public static final String EXAMINE = "examine";
    public static final String IS_PROTECT_GROUPUSER = "is_protect_groupuser";
    public static final String IS_ALLOW_RECEIVE_REDPACKET = "is_allow_receive_redpacket";
    public static final String FROM_TYPE_FRIENDS="fromType";
    public static final String GIF = ".gif";
    public static final String FILE_CODE = "filecode";
    public static final String TYPE_ID = "typeid";
    public static final String PIX_W = "pixW";
    public static final String PIX_H = "pixH";
    public static final String UP_FILE = "upfile";
    public static final String AT_INS = "[有人@你]";



    public static final int REQUEST_CODE_ADD_GROUP_MEMBERS = 100;
    public static final int REQUEST_CODE_REMOVE_GROUP_MEMBERS = 101;

    /**
     * p2p聊天创建群
     */
    public static final int REQUESTCODE_CREATE_GROUP = 0x1000;
    public static final int RESULTCODE_CREATE_GROUP = 0x1001;

    /**
     * 二维码识别类型
     */
    public static class QRType {
        // 个人二维码
        public static final String PERSION = "person_qrcode";
        // 群组二维码
        public static final String GROUP = "group_qrcode";
        // 斗圈收款码
        public static final String CNPAY = "cnpay_qrcode";
    }
    public static final String QR_CODE = "erweima";
    public static final String ALIPAY = "alipay";
    /**
     * 客户端跳转域名
     */
    public static final String OPEN_CLIENT_UNIQUE = "www.meetsn.com";
    public static final String HTTP = "http";
    public static final String HTTPS="HTTPS";
    public static final String HTTPSLOW="https";
    public static final long TWO_THOUSAND_MILLISECONDS = 2000L;
    public static final String APLAY_URL = "alipayurl";
    public static final String PHOTO_ALBUM_LIST_DATA = "album_datas";
    //自定义表情
    public static final int PHOTO_REQUEST_CAMERA = 1;
    public static final String UPDATE_EMOTION_LIST = "update_emotion";


    public static class AddUserDetail {
        public static final String FRIEND = "friend";
        // 群组ID
        public static final String GROUP_ID = "GroupId";
        public static final String IS_FROMTYPE = "isFromType";
        // 是否是群主
        public static final String IS_MASTER = "is_master";
        // 是否是管理员
        public static final String IS_ADMIN = "is_admin";
        // 是否是名片
        public static final String IS_CARD = "is_card";
        // 是否是搜索进入
        public static final String IS_SEARCH = "isSearch";
        public static final String IS_EXIT_MEMBER = "is_exit_member";
    }
    //SDK广播接收
    public static final String RECEIVER_PATH = "com.opensdk.qingchat.utils.ReceiveCNBroadcastReceiver";
    //SDK广播接收name
    public static final String RECEIVER_ACTION = "com.opensdk.qingchat";
    public static final String SDK_MAIN_FINISH="x0000003";//sdk使用 finish MainActivity conversationActivity
    /**
     * sdk分享
     */
    public interface SDKShare {
        String TYPE = "type";
        String TITLE = "title";
        String CONTENT = "content";
        String URL = "url";
        String URL_INTENT = "urlIntent";
        String APP_ID = "appId";
        String APP_SECRET = "appSecret";
        String APP_NAME = "appName";
        String APP_LOGO = "appLogo";
        String BACKINFO = "backInfo";
        String PACKAGE_NAME = "packageName";
        String EXTRA = "extra";

        int SUCCESS = 1000;
        int FAIL = 1100;
    }
    /**
     * sdk登录
     */
    public interface SDKLogin {
        String SDK_LOGIN_KEY = "sdk_login_key";
    }


    public interface aides{
        String PLUGIN_BEAN = "plugin_bean";
        String PLUGIN_TYPE = "plugin_type";

        String PLUGIN_NAME = "plugin_name";
        String PLUGIN_ID = "plugin_id";
        String PLUGIN_IS_EXIT = "plugin_is_exit";
    }

    //收藏
    public static final String FROM_UID = "from_uid";
    public static final String MESSAGE_ID = "message_id";
    public static final String CONTENT = "content";
    public static final String COLLETION_DIRECTION = "collection/";//收藏路径 目前只是用于截取字符串
    public static final String OSS_VIDEO_FRAME_ANDROID = "?x-oss-process=video/snapshot,t_0000,f_jpg";//视频截帧android
    public static final String OSS_VIDEO_FRAME_IOS = "?x-oss-process=video/snapshot,t_0000,f_jpg";//视频截帧ios ,w_200,h_400

    /**
     * 支付宝收款码
     */
    public interface AliPaymentCode {
        // 会话类型
        String SESSION_TYPE = "session_type";
        // 群组ID
        String TARGET_ID = "target_id";
        // 图片地址
        String IMAGE_URI = "image_uri";
        //        // 图片URI地址
//        String IMAGE_URL = "image_uri";
        // 是否上传图片
        String IS_UPLOAD_IMAGE = "is_upload_image";
    }

    public interface Wallet {

        String PWD_PAY_NEW = "pwd_pay_new";

        String BANK_CARD_BEAN = "bank_card_bean";

        String CARDHOLDNAME = "cardholdname";
        //卡号
        String CARD_NO = "card_no";
        String CARDID = "cardid";
        //城市
        String CITYCODE = "citycode";
        //银行名称
        String BANKNAME = "bankname";
        //设置id
        String FRMS_IMEI = "frms_imei";
        //绑定的手机号
        String BIND_MOBILE = "bind_mobile";
        //设备来源（ANDROID、IOS、WEB、WAP）
        String SOURCE = "source";
        //支付密码
        String PWD_PAY = "pwd_pay";
        String CONFIRM_PWD_PAY = "confirm_pwd_pay";
        String RANDOM_VALUE = "random_value";

        String TOTAL_AMOUNT = "total_amount";

        String FLAG_CHECK = "flag_check";//操作类型：0：修改个人信息；1：修改绑定手机号码
        //mac地址
        String FRMS_MAC_ADDR = "frms_mac_addr";
        //机器编码
        String FRMS_MECHINE_ID = "frms_mechine_id";

        String CHOICE_PROFESSIONAL = "choice_professional";
        int REQUEST_CODE_PROFESSIONAL = 1234;
        int REQUEST_CODE_RECHARGE_CHOICE_BANK = 1235;
        int REQUEST_CODE_CASH_OUT_CHOICE_BANK = 1236;

        String MODIFY_PWD = "1";
        String FORGOT_PWD = "2";

        //我的银行卡
        String MY_BANK_CARD = "1";
        //充值选择银行卡
        String RECHARGE_CHOICE_BANK_CARD = "2";
        //提现选择银行卡
        String CASH_OUT_CHOICE_BANK_CARD = "3";

        //正常的添加银行卡号
        String ADD_CARD_NONE = "1";
        //忘记密码添加银行卡号校验
        String ADD_CARD_FORGOT_PASSWORD = "2";

        //添加银行卡绑定银行信息短信验证
        String SMS_CODE_BIND_CARD_INFO = "1";
        //忘记支付密码短信验证
        String SMS_CODE_FORGOT_PWD= "2";
        //充值短信验证
        String SMS_CODE_RECHARGE = "3";

        String VERIFY_TOKEN = "verify_token";
        String VERIFY_CODE = "verify_code";
        String OWN_ORDER_ID = "own_order_id";

    }

    public interface WalletOption {
        // 设置密码
        String SET_PWD = "1";
        // 操作银行卡（添加移除）
        String CARD_OPTION = "2";
        // 充值
        String RECHARGE = "3";
        // 提现
        String DEPOSIT = "4";
    }

    public static final String DATA = "intentUrl";

    //账号登录，密码错误1-4次
    public static final int LOGIN_PASSWORD_ERROR_CODE = 100305;
    //账号登录，密码错误5次
    public static final int LOGIN_PASSWORD_ERROR_MAX_CODE = 100306;
    //重新设置登录密码,输入错误
    public static final int RESET_LOGIN_PASSWORD_ERROR_CODE = 100606;
    //发红包密码错误
    public static final int SEND_RED_PASSWORD_ERROR_CODE = 102913;
    //AA付款密码错误
    public static final int AA_RECEIPTS_PASSWORD_ERROR_CODE = 109007;
    //充值密码错误
    public static final int RECHARGE_PASSWORD_ERROR_CODE = 106004;
    //旧用户提现密码错误
    public static final int OLD_DEPOSIT_PASSWORD_ERROR_CODE = 105908;
    //新用户提现密码错误
    public static final int DEPOSIT_PASSWORD_ERROR_CODE = 110508;
    //转账密码错误
    public static final int TRANSFER_PASSWORD_ERROR_CODE = 103603;
    //检验设置支付密码
    public static final int CHECK_PAY_PASSWORD_ERROR_CODE = 105003;
    //修改支付密码密码错误
    public static final int MODIFY_PAY_PASSWORD_ERROR_CODE = 105104;

    public interface Remark {
        String TYPE = "type";
        String TO_UID = "to_uid";
        String REMARKS  = "remarks";
        String PHONES   = "phones";
        String DESCRIPTIONS = "descriptions";
        String CARD = "card";
        String FRIEND_REMARK_BEAN = "friend_remark_bean";
        String FRIEND_REMARK_BEAN_RESULT = "friend_remark_bean_result";
    }

    /** 邀请联系人 */
    public static final String INVITE_MOBILE = "members";

    /**
     * 系统拓展字段
     */
    public interface SystemExpandContact{
        String KEY_OBJECTNAME = "objectName";
    }

}
