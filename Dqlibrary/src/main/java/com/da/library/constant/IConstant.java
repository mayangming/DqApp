package com.da.library.constant;

/**
 * @Author: 方志
 * @Time: 2018/9/10 15:02
 * @Description:
 */
public interface IConstant {

    String OK = "OK";
    long TIME_60000 = 60000L;
    long TIME_1000 = 1000L;
    int PERMISSION_REQUEST_CODE = 1000;


    String ENTER_TYPE = "ENTER_TYPE";

    interface Login {
        String PHONE = "phone";
        String TYPE = "type";
        String TOKEN_KEY = "token_key";
        String CAPTCHA = "captcha";
        String LOGIN_TYPE = "1";
        String REGISTER_TYPE = "2";
        String CHATDQ = "chatdq";
        String UID = "uid";

        String MSG = "msg";
        String PWD = "pwd";
        String OLD_PWD = "old_pwd";
        String DEVICE = "device";
        String IMEI = "imei";
        String CODE = "code";
    }

    interface UserInfo {
        String NICKNAME = "nickName";
        String HEADPIC = "headpic";
        String FRIEND = "friend";
        String SESSION_TYPE = "session_type";

        String UID = "uid";
        String OTHER_UID = "other_uid";
        String GROUP_ID = "group_id";
        String IS_MASTER = "is_master";
        String IS_EXIT_MEMBER = "is_exit_member";//是否退群人员
        String IS_CARD = "is_card";//是否是名片
        String IS_SEARCH = "IS_SEARCH";//是否是搜索
        String TO_UID = "to_uid";
        String EXTRA = "extra";

        String REQUEST_ID = "request_id";
        String STATUS = "status";
        String RESPONSESTATUS = "responseStatus";
        String UNREMARK = "unremark";
        String REMARKS = "remarks";

        String NEW_FRIEND_BEAN = "new_friend_bean";
    }

    interface Password{
        String PWD_MD5 = "dqpwd20191010";
    }

    /**
     * 微信
     */
    interface WX {
        //微信返回的参数
        String OPENID = "openid";
        String ACCESSTOKEN = "accessToken";
        //斗圈服务器请求参数
        String ACCESS_TOKEN = "access_token";

//        'appid' => "wx91e9fc9cb6d679a6",
//        'appsecret' => "124938a89031ae3662c74f3c01ff8be7"
        //微信认证的id
        String APP_ID = "wx02e1114117d39fed";
        String WXAPPSECRET = "fdf5fc9a400df9226638b2ac125c0cde";

        String WXMAP = "wxmap";
    }

    interface Contact {
        String PAGE = "page";
        String LENGTH = "length";
        //斗圈小助手
        String WHETHER_HELPER = "whether_helper";
        //自己
        String SHOW_SELF  = "show_self";
        //上一次更新的时间
        String LAST_TIME  = "last_time";//0 全量刷新，1 增量刷新(这时传当前时间戳) 主要用于刷新好友列表
    }

    /**
     * 手机联系人
     */
    interface Mobile {
        String DATA = "data";
    }

    /**
     * 列表选择
     */
    interface Select{
        //单选
        String SINGLE = "single";
        //多选
        String MORE = "more";
        //选中的人员数据
        String SELECT_LIST = "select_list";
        //单选/多选
        String SELECT_MODE = "select_mode";
        //选择管理员
        String SELECT_ADMIN = "select_admin";
        //设置最大选择数
        String SELECT_MAX = "SELECT_MAX";
        //设置最大管理员数
        String SELECT_ADMIN_MAX = "select_admin_max";
    }

    interface Update {
        String UPDATETYPE = "updatetyle";
        String URL = "url";
        String VERSION = "version";
        String DEVEICE_TYPE = "deveice_type";
        String DECEIVE_TYPE = "deceive_type";
        String APP_VERSION = "app_version";
        String ANDROID = "android";
    }

    interface Code{
        //账号登录，密码错误1-4次
        int LOGIN_PASSWORD_ERROR_CODE = 100305;
        //账号登录，密码错误5次
        int LOGIN_PASSWORD_ERROR_MAX_CODE = 100306;
        //重新设置登录密码,输入错误
        int RESET_LOGIN_PASSWORD_ERROR_CODE = 100606;
        //发红包密码错误
        int SEND_RED_PASSWORD_ERROR_CODE = 102913;
        //AA付款密码错误
        int AA_RECEIPTS_PASSWORD_ERROR_CODE = 109007;
        //充值密码错误
        int RECHARGE_PASSWORD_ERROR_CODE = 106004;
        //旧用户提现密码错误
        int OLD_DEPOSIT_PASSWORD_ERROR_CODE = 105908;
        //新用户提现密码错误
        int DEPOSIT_PASSWORD_ERROR_CODE = 110508;
        //转账密码错误
        int TRANSFER_PASSWORD_ERROR_CODE = 103603;
        //检验设置支付密码
        int CHECK_PAY_PASSWORD_ERROR_CODE = 105003;
        //修改支付密码密码错误
        int MODIFY_PAY_PASSWORD_ERROR_CODE = 105104;
    }

    interface Share {

        String SHARE_BAEN = "share_baen";
        String IM_MESSAGE = "im_message";
        String ATTACHMENT = "attachment";
        String PATH = "path";
        String SHARE = "share";
        String FORWARDING = "FORWARDING";
        String TEXT = "text";
        String IMAGE = "image";
    }

    interface AES {
        String KEY = "Dq2019Text606KEY";
        String VALUE = "Dq2019Text6VALUE";
    }
}
