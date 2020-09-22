package com.wd.daquan.common.constant;


import com.wd.daquan.model.BuildConfig;

/**
 * Created by DELL on 2018/9/10.
 */

public class DqUrl {
//    第三方分享到APP使用域名
//    public static final String SERVER_OPEN = "https://open.meetsn.com/";

//    /**
//     * 测试服务器地址
//      */
//    public static final String SERVER = "http://t.x1.meetsn.com/";
//    /**
//     * 生产服务器地址
//     */
//    public static final String SERVER = "https://x1.meetsn.com/";
    /**
     * 第三方分享到APP使用域名
     */
    public static String SERVER_OPEN = BuildConfig.SERVER_OPEN;

    /**
     * App内部使用域名
     */
    public static String SERVER = BuildConfig.SERVER;

//    /**
//     * FILE生产服务器文件地址
//     */
//    public static final String SERVER_OFFICIAL = "https://www.meetsn.com/";
    /**
     * FILE生产服务器文件地址
     */
//    public static final String SERVER_OFFICIAL = "http://api.dq.ink:9010/";
    public static final String SERVER_OFFICIAL = BuildConfig.SERVER;
    /**
     * 官网基础地址
     */
    public static final String SERVER_FILE = "http://oss.meetsn.com/";

    /**
     * 帮助页面 https://www.meetsn.com/wap/help/index  https://www.meetqs.com/wap/help/index
     */
    public static final String url_help = "https://www.meetsn.com/wap/help/index";
    /**
     * 帮助页面过滤 https://www.meetsn.com/wap/help/index  https://www.meetqs.com/wap/help/index
     */
    public static final String url_help_filter = "https://www.meetsn.com/wap/help";
    /**
     * 获取验证码
     */
    public static String url_get_phone_msg = "user/get_phone_msg";
    /**
     * 获取图形验证码
     */
    public static String url_get_captcha_img = "user/get_captcha_img";

    /**
     * 获取图形验证码
     */
    public static String url_get_getImageVerifyCode = "user/getImageVerifyCode";
    /**
     * 校验图形验证码
     */
    public static String url_get_verifyImageCode = "user/verifyImageCode";
    /**
     * 注册
     */
    public static String url_reg = "user/register";
    /**
     * 登录
     */
    public static String url_pwd_login = "user/login";

    /**
     * 验证码登录
     */
    public static String url_msg_login = "user/login";

    /**
     * 设置昵称和头像 user/set_userinfo
     */
    public static String url_set_userinfo = "user/set_user_info";
    /**
     * 设置昵称和头像 user/set_userinfo
     */
    public static String url_upload_headpic = "user/upload_headpic";
    /**
     * 退出登录 user/logout
     */
    public static String url_logout = "user/logout";
    /**
     * 设置密码 user/setPwd
     */
    public static String url_set_pwd = "user/set_pwd";
    /**
     * 忘记密码后设置密码 user/forgetPwd
     */
    public static String url_forget_pwd = "user/forget_pwd";
    /**
     * 根据手机号查找用户  user/find_user
     */
    public static String url_find_user = "user/find_user";
    /**
     * 发送好友申请 user/friend_request
     */
    public static String url_friend_request = "user/friend_request";

    /**
     * 邀请联系人，通过发短信的方式邀请
     */
    public static String url_invitation_sms = "user/send_batch_sms";

    /**
     * 获取其他用户信息 user/get_userinfo
     */
    public static String url_get_userinfo = "user/get_userinfo";
    /**
     * 添加好友请求列表接口 user/friend_request_list
     */
    public static String url_friend_request_list = "user/friend_request_list";
    /**
     * 被添加好友者应答 user/friend_response
     */
    public static String url_friend_response = "user/friend_response";
    /**
     * 好友列表接口
     */
    public static String url_friend_list = "user/friend_list";
    /**
     * 设置黑名单接口
     */
    public static String url_add_black = "user/add_black";
    /**
     * 移除黑名单接口 user/remove_blacklist
     */
    public static String url_remove_blacklist = "user/remove_black";
    /**
     * 黑名单列表接口
     */
    public static String url_get_black_list = "user/get_black_list";
    /**
     * 删除好友接口
     */
    public static String url_delete_friend = "user/delete_friend";

    /**
     * 手机通讯录匹配接口 user/match_phone_address_book
     */
    public static String url_match_phone_address_book = "user/match_phone_address_book";

    /**
     * 设置被加为好友时是否需要验证接口 user/set_added_friend_whether_validate
     * jlqln/wzflk
     */
    public static String url_set_added_friend_whether_validate = "user/set_added_friend_whether_validate";
    /**
     * 是否是好友 user/whether_friend
     */
    public static String url_whether_friend = "user/whether_friend";

    /**
     * 设置是否可以通过手机号搜索自己 user/set_allow_search
     */
    public static String url_set_allow_search = "user/set_allow_search";
    /**
     * 设置是否允许通过斗圈号搜索到好友 user/set_allow_douquan_search
     */
    public static String url_set_allow_douquan_search = "user/set_allow_douquan_search";
    /**
     * 设置新消息通知开关 user/set_msg_notify
     */
    public static String url_set_msg_notify = "user/set_msg_notify";
    /**
     * 设置是否接受戳一下消息
     */
    public static String url_set_poke_status = "user/set_poke_status";

    /** 是否允许邀请入群开关设置
     */
    public static String url_allow_group = "user/allow_group";



    /*****************************************   im 单聊，群聊 *********************/
    /**
     * 创建群组接口
     */
     public static String url_create_group = "group/create_group";

    /**
     * 获取群组基本信息
     */
    public static String url_select_group = "group/select_group";
    /**
     * 查询群成员接口
     */
    public static String url_select_group_user = "group/select_group_user";
    /**
     * 设置群组信息接口
     */
    public static String url_edit_group_manager_info = "group/edit_group_manager_info";
    /**
     * 设置群组信息接口
     */
    public static String url_edit_group_info = "group/edit_group_data";

    /**
     * 自己退出群聊接口
     */
    public static String url_user_quit_group = "group/user_quit_group";

    /**
     * 解散群
     */
    public static String url_remove_group = "group/remove_group";


    /**
     * 批量踢出群聊接口
     */
    public static String url_kick_group = "group/kick_group";
    /**
     * 加入群组接口
     */
    public static String url_join_group = "group/join_group";


    /**
     * 单聊截屏设置查询接口
     * mymob/nltmu
     */
    public static String url_setting_chat = "chat/set_screenshot_notify";
    /**
     * 单聊阅后即焚设置接口
     */
    public static String url_burn_set = "chat/set_burn";

    /**
     * 用户群组通讯录查询接口 get_saved_groups
     */
    public static String url_get_saved_groups = "user/get_saved_groups";

    /**
     * 版本更新 upgrade/check  version_update
     * wbjii/jyggv
     */
    public static String url_version_change = "user/version_update";


    /**
     * 自主冻结用户
     * jlqln/pzggo
     */
    public static String url_block = "user/block";
    /**
     * 用户解冻接口
     * jlqln/ycbmb
     */
    public static String url_unblock = "user/unblock";
    /**
     * 查询是否可一键冻结/解冻接口
     */
    public static String url_whether_block = "user/whether_block";

    /**
     * 我的群组
     * lumgw/hhsvl
     */
    public static String url_my_groups = "user/get_saved_groups";

    /**
     * 获取群组指定用户信息
     * lumgw/hlmms
     */
    public static String url_select_group_member = "group/select_group_member";
    /**
     * 设置备注
     * ncuil/gmhrh
     */
    public static String url_set_remark = "user/set_remark";
    /**
     * 微信分享页面 https://www.meetsn.com/wap/share/index
     */
    public static String url_weChat_share = SERVER_OFFICIAL + "wap/share/index";
    /**
     *斗圈logo http://oss.meetsn.com/upload/helper.png   /upload/logo.png
     */
    public static String url_CN_logo = SERVER_FILE + "upload/helper.png";

    /**
     * 注册协议 https://www.meetsn.com/wap/protocol/service
     */
    public static String url_register_agreement = SERVER_OFFICIAL + "dq_protocol.html";
    /**
     * 隐私协议 https://www.meetsn.com/wap/protocol/service
     */
    public static String url_privacy_agreement = SERVER_OFFICIAL + "Privacy_Agreement.html";
    /**
     * 新人须知 https://www.meetsn.com/wap/protocol/service
     */
    public static String url_new_user_tip = SERVER_OFFICIAL + "dq_xrxz.html";
    /**
     * 发布规则 https://www.meetsn.com/wap/protocol/service
     */
    public static String url_fbgz = SERVER_OFFICIAL + "dq_fbgz.html";
    /**
     * 生产环境联系我们
     */
    public static String uri_contact_us = SERVER_OFFICIAL + "wap/help/page?catid=32";


    /******************************* 支付宝 *********************************************/
    /**
     * 判断当前用户是否绑定支付宝账户
     * gdovm/qmlzv
     */
    public static String url_isBindAlipay = "lbiiu/obpwa";
    /**
     * 支付宝授权登录
     * gdovm/leagd
     */
    public static String url_index = "lbiiu/atfms";
    /**
     * 获取支付宝授权登陆所需配置信息
     * gdovm/sqgyp
     */
    public static String url_getConfigInfo = "lbiiu/elkxy";
    /**
     * 发放支付宝红包接口
     * nkrum/aguzt
     */
    public static String url_giveRedPacket = "bkfcv/qhbce";

    /**
     * 抢支付宝红包
     * nkrum/luxju
     */
    public static String url_reciveRedPacket = "bkfcv/eshdp";
    /**
     * 支付宝红包领取明细查询接口
     * nkrum/nxrbt
     */
    public static String url_redPacketDetail = "bkfcv/bjtpd";

    /**
     * 支付宝红包领取-开
     * nkrum/yjvbu
     */
    public static String url_openRedpacket = "bkfcv/fupvo";
    /**
     * 我的支付宝红包往来明细
     * nkrum/qucob
     */
    public static String url_myAliRedpacket = "bkfcv/gtypd";
    /**
     * 解除绑定的支付宝账号
     */
    public static String url_cancelBindAlipay = "lbiiu/jxebc";


    /**
     * 我的表情列表
     */
    public static String url_myEmotion = "user/my_emotions";
    /**
     * 添加表情
     */
    public static String url_add_myEmotion = "user/add_emotion";

    /**
     * 删除表情
     * rbtow/uprgw
     */
    public static String url_delete_myEmotion = "user/delete_emotion";

    /**
     * 头像缩略图后缀
     */
    public static String url_avatar_suffix = "?x-oss-process=style/sy-img";

    /**
     * 入群申请列表 url_apply_list修改为 url_group_admin  51./group/apply_list
     * lumgw/utxdx
     */
    public static String url_group_admin = "group/apply_list";
    /**
     * 入群申请 审核
     * lumgw/jdrdg  52./group/apply
     */
    public static String url_apply_response = "group/apply_response";
    /**
     * 用户在斗圈中升级斗圈APP时候调用 upgrade/recordUpgradeData
     * wbjii/txjqv 下载统计
     */
    public static String url_recordUpgradeData = "upgrade/recordUpgradeData";
    /**
     * 群管理人员列表
     * lumgw/qhxfq
     */
    public static String url_group_managers_list = "/group/managers_list";
    /**
     * 设置管理员
     * lumgw/gbkqg
     */
    public static String url_group_set_managers = "group/set_managers";
    /**
     * 转让群主
     * lumgw/myvmr
     */
    public static String url_group_transfer_master = "group/transfer_master";
    /**
     * 退群人员
     * lumgw/ncdxa
     */
    public static String url_group_quit_record = "group/quit_record";
    /**
     * 手机客户端扫描web端登录
     * ljrkq/nvzhb
     */
    public static String url_web_scan_login = "gacoe/pvshe";


    /**
     * 手机的退出网页登录
     * ljrkq/utoif
     */
    public static String url_web_logout = "gacoe/puubb";



    /**
     * H5跳转下载斗圈或打开斗圈
     */
    public static String H5_QRCODE = "https://www.meetsn.com";

    /**
     * 获取阿里云文件授权临时token
     * hrxfa/zxwjy
     */
    public static String url_alioss_token = "vdtqc/naaqq";
    /**
     * 微信授权登录
     * weixinOauth/zynic/moysy
     */
    public static String url_oauth_getUserInfo = "wechat/loginByWeChat";

    /**
     * 微信授权登陆：验证短信验证码 60. /wechat/validate_msg_code
     * weixinOauth/zynic/nwxsl
     */
    public static String url_oauth_checkPhoneCode = "wechat/wechat_user_register";
    /**
     * 获取用户信息
     * wuser/userinfo
     */
    public static String url_oauth_useWeixinInfo = "user/userinfo";
    /**
     * 微信授权登陆：用户解绑微信
     * weixinOauth/zynic/eaqsj
     */
    public static String url_oauth_cancelBindWeixin = "wechat/wechatRemoveBind";
    /**
     * 微信授权登陆：重新绑定微信`
     * weixinOauth/zynic/rpyng
     */
    public static String url_oauth_afreshBindWeixin = "wechat/wechatRebind";
    /**
     * 微信授权登陆：获取斗圈用户的微信解绑状态
     * weixinOauth/zynic/esbpf
     */
    public static String url_oauth_bindWeixinStatus = "wechat/getWechatBindStatus";

    /**
     * 微信获取accesToken接口
     */
    public static String url_oauth_wx_access_token = "/user/weixin_login";
    /**
     * 微信获取用户信息
     */
    public static String url_oauth_wx_user_info = "/user/weixin_userinfo";

    /**
     * 判断是否在禁止名单内
     * tzaed/ijvwg
     */
    public static String url_forbidRedpacket_check = "plulj/jjeew";


    /**
     * 发现菜单
     * puaat/koxjq
     */
    public static String url_disvover_menu = "ymsdq/uieue";
    /**
     * 意见反馈接口 universal/feedback
     * rrcjh/xjnco
     */
    public static String url_feedback = "tnfxo/badcy";

//    /**
//     * 获取第三方应用基础信息
//     */
//    public static String url_get_app_info = SERVER_OPEN + "sdk/appinfo";
//    /**
//     * 获取授权code
//     */
//    public static String url_get_app_code = SERVER_OPEN + "sdk/oauth2/code";
//    /**
//     * 获取access_token和openid
//     * connect/access_token
//     */
//    public static String url_get_accessToken = SERVER_OPEN + "sdk/oauth2/access_token";
    /**
     * 获取第三方应用基础信息
     */
    public static String url_get_app_info = SERVER_OPEN + "sdk/appinfo";
    public static String url_sdk_app_info = "sdk/appinfo";
    /**
     * 获取授权code
     */
    public static String url_get_app_code = SERVER_OPEN + "sdk/oauth2/code";
    public static String url_sdk_app_code = "sdk/oauth2/code";
    /**
     * 获取access_token和openid
     * connect/access_token
     */
    public static String url_get_accessToken = SERVER_OPEN + "sdk/oauth2/access_token";
    public static String url_sdk_accessToken = "sdk/oauth2/access_token";

    /**
     * SDK APP分享统计
     */
    public static String url_sdk_get_share_statistics = SERVER_OPEN+"share/statistics";

    public static String url_sdk_share_statistics = "share/statistics";
    /**
     * 群组内长时间未领取红包
     * group/redpacket
     * lumgw/smnho
     */
    public static String url_group_redpacket = "jdfkh/gumyd";

    /**
     * 截屏通知接口
     * screenshot/push_msg
     * dtuxl/nzkfq
     */
    public static String url_screenshot_push_msg = "czvtq/rnxbl";

    /**
     * 群助手增删改plugin/set
     * sufnd/strdj
     */
    public static String url_group_aides_pligin_set = "xcbdz/kknha";
    /**
     * 获取服务器配置
     * config/lists
     * kyotp/zmdxa
     */
    public static String url_server_config = "shral/shgdn";

    /**
     * 邀请入群列表
     * group/invite_lists
     * lumgw/ndhsk
     */
    public static String url_group_invite_list = "group/user_group_req_list";
    /**
     * 添加收藏 collection/add
     * kexkx/kfezh
     */
    public static String url_collection_add = "jyeth/odlmf";
    /**
     * 收藏列表 collection/select
     * kexkx/gcpgc
     */
    public static String url_collection_list = "jyeth/ujlnp";
    /**
     * 收藏删除 collection/cancel
     * kexkx/ucrsp
     */
    public static String url_collection_delete = "jyeth/woafb";

    /**
     * 处理邀请入群请求
     * group/dealwith_invite
     * lumgw/nhsko
     */
    public static String url_group_invite_response = "group/user_group_response";

    /**
     * 搜索群助手列表plugin/lists
     * sufnd/shgoe
     */
    public static String url_search_group_aides = "xcbdz/zgnlc";

    /**
     * 群助手授权
     * sufnd/hsirn
     */
    public static String url_group_auth = "xcbdz/fzite";

    /**
     * 查询已加入禁止收发红包成员列表 forbidRedpacket/select
     * tzaed/znfwo
     */
    public static String url_forbidRedpacket_select = "plulj/mnexj";

    /**
     * 获取群成员列表，[仅限禁止收发红包功能使用] stateMember
     * tzaed/ctuuv
     */
    public static String url_forbidRedpacket_selectMember = "plulj/qhxja";

    /**
     * 加入、移除群员到禁止收发红包名单 forbidRedpacket/joinAndRemove
     * tzaed/pnieh
     */
    public static String url_forbidRedpacket_joinAndRemove = "plulj/yygak";
    /**
     * 复制群 group/copy
     * lumgw/nshjf
     */
    public static String url_group_copy = "jdfkh/bdpvp";

    /**
     * 上传收款码地址
     * setting/paymentCode
     */
    public static String url_payment_code = "noxkn/dssss";

    /**
     * 【开户】接收开通钱包短信验证码
     */
    public static String url_wallet_verification_get_code = "qefrx/qrhqj";
    /**
     * 【开户】开通钱包短信校验码校验接口
     */
    public static String url_wallet_verification_verify = "qefrx/yfnru";
    /**
     * 【开户】提交用户开通钱包信息
     */
    public static String url_wallet_open = "qefrx/sfcgu";
    /**
     * 用户钱包状态查询
     */
    public static String url_wallet_status = "qefrx/qvoqj";
    /**
     * 【添加银行卡】根据银行卡号获取银行卡基本信息
     */
    public static String url_add_bank_card_get_info = "qefrx/sdfds";
    /**
     * 【添加银行卡】添加绑定银行卡信息
     */
    public static String url_add_bank_card_bind_info = "qefrx/jhvoh";
    /**
     * 【充值】创建充值订单
     */
    public static String url_recharge_create_order = "qefrx/ixwju";
    /**
     * 【添加银行卡】验证银行卡绑定信息-短信验证码校验
     */
    public static String url_add_bank_card_get_code = "qefrx/ohvbh";
    /**
     * 【支付密码找回】短信验证码验证
     */
    public static String url_forgot_pwd_get_code = "qefrx/aswds";
    /**
     * 【充值】验证充值短信验证码
     */
    public static String url_recharge_get_code = "qefrx/iolsg";
    /**
     * 【支付密码找回】输入绑定银行卡信息
     */
    public static String url_forgot_pwd_bind_info = "qefrx/plage";
    /**
     * 获取绑定的银行卡列表
     */
    public static String url_get_bank_card_list = "qefrx/umslg";
    /**
     * 解除绑定银行卡
     */
    public static String url_unbind_bank_card = "qefrx/mzazf";
    /**
     * 校验支付密码
     */
    public static String url_check_pay_pwd = "qefrx/ugspl";
    /**
     * 钱包支付密码修改
     */
    public static String url_modify_pay_pwd = "qefrx/xbgcr";
    /**
     * 【提现】创建提现订单
     */
    public static String url_cash_out_create_order = "qefrx/hrjph";
    /**
     * 【提现】计算提现手续费率
     */
    public static String url_cash_out_fee = "qefrx/hlsmc";





    /**
     * 转帐
     */
    public static String url_pay_thbhh_oeutu = "thbhh/oeutu";

    /**
     * 转帐验证码确认
     */
    public static String url_pay_thbhh_slkst = "thbhh/slkst";

    /**
     * 接受转帐
     */
    public static String url_pay_thbhh_lbgcw = "thbhh/lbgcw";


    public static String url_pay_selis_tfces = "selis/tfces";

    /**
     * 零钱明细
     */
    public static String url_loose_detail = "qefrx/jacmq";
    /**
     * 红包收入记录接口
     */
    public static String url_in_redpacket_record = "account/in_redpacket_record";
    /**
     * 红包支出记录接口
     */
    public static String url_out_redpacket_record = "account/out_redpacket_record";


    /**
     * 获取支付密码key
     */
    public static String url_get_pay_pwd_key = "qefrx/emkla";









    /***************************** 斗圈暂时不使用的接口 *****************************/
//    /**
//     * 是否是银行卡
//     */
//    public static String url_whether_card = "bank/card_bin";
//    /**
//     * 发单人红包
//     */
//    public static String url_redpacket_create = "redpacket/create";
//    /**
//     * 查询账户余额
//     */
//    public static String url_account_balance = "account/balance";
//
//    /**
//     * 红包领取
//     */
//    public static String url_redpacket_receive = "redpacket/receive";
//    /**
//     * 红包是否已经抢完
//     */
//    public static String url_redpacket_surplus_amount = "redpacket/redpacket_surplus_amount";
//    /**
//     * 红包已抢记录接口
//     */
//    public static String url_redpacket_record = "redpacket/record";
//    /**
//     * 阅后即焚状态查询接口
//     */
//    public static String url_setting_burn = "mymob/mcwyt";
//
//    /**
//     * 发起转账接口
//     */
//    public static String url_transfer_create = "transfer/create";
//
//    /**
//     * 转账状态查询
//     */
//    public static String url_transfer_status = "transfer/status";
//
//    /**
//     * 接收转账接口
//     */
//    public static String url_transfer_receive = "transfer/receive";
//
//    /**
//     * 零钱明细
//     */
//    public static String url_loose_detail = "account/details";
//    /**
//     * 支付密码校验
//     */
//    public static String url_validate_pwd = "account/validate_pwd";
    /**
     * 修改支付密码接口
     */
    public static String url_modify_pwd = "account/modify_pwd";
    /**
     * 重置支付密码接口
     */
    public static String url_reset_pwd = "account/reset_pwd";
//    /**
//     * 红包收入记录接口
//     */
//    public static String url_in_redpacket_record = "account/in_redpacket_record";
//    /**
//     * 红包支出记录接口
//     */
//    public static String url_out_redpacket_record = "account/out_redpacket_record";
//
//    /**
//     * 银行卡预留信息验证接口
//     */
//    public static String url_check_card = "bank/realname";
//    /**
//     * 银行预留手机号验证
//     */
//    public static String url_check_mobile = "bank/check_mobile";
    /**
     * 开通账户，设置支付密码
     */
    public static String url_open_account = "bank/setPayPassword";
//    /**
//     * 用户解绑银行卡
//     */
//    public static String url_cancelBindCard = "bank/cancelBindCard";
//
//    /**
//     * 用户充值
//     */
//    public static String url_recharge = "bank/recharge";
//    /**
//     * 获取融云token
//     */
//    public static String url_get_rc_token = "user/get_rc_token";
//    /**
//     * 已实名用户绑定银行卡
//     */
//    public static String url_bind_many_card = "bank/bind_many_card";
//    /**
//     * 已实名用户绑定银行卡---验证银行预留手机号
//     */
//    public static String url_input_mobile = "bank/input_mobile";
//    /**
//     * 实名认证信息接口
//     */
//    public static String url_authinfo = "account/authinfo";
//
//    /**
//     * 已保存的群聊
//     */
//    public static String url_exist_groups = "contact/select";
//    /**
//     * 重新发送验证码接口
//     */
//    public static String url_againSendCode = "bank/againSendCode";
//    /**
//     * 用户充值：快捷订单验证接口
//     */
//    public static String url_sendRechargeCode = "bank/sendRechargeCode";
//    /**
//     * 用户协议
//     */
//    public static String url_CN_agreement = "h5/protocol.html";
//
//    /**
//     * 阶梯费率表
//     */
//    public static String url_jieti = "h5/postalfee.html";
//    /**
//     * 我的群组
//     */
//    public static String url_validate = "content/validate";
//    /**
//     * 新消息是否显示详情设置 user/set_news_detail
//     */
//    public static String url_set_news_detail = "jlqln/jvnvk";
//    /**
//     * 发放支付宝红包回调接口
//     */
//    public static String url_appPayCallback = "aliRedpacket/appPayCallback";
//    /**
//     * 敏感词库
//    gvqpb/uwblq
//     */
//    public static String url_sensitivity_lib = "qgdkh/irbtk";
//    /**
//     * 发起AA收款
//     */
//    public static String url_paycreate = "receipt/create";
//    /**
//     * AA收款详细
//     */
//    public static String url_paydetail = "receipt/detail";
//    /**
//     * 支付AA收款
//     */
//    public static String url_paypay = "receipt/pay";
//
//    /**
//     * 获取支持充值的银行列表
//     */
//    public static String url_supportBank = "bank/supportBank";
//    /**
//     * 根据银行卡类型获取限额
//     */
//    public static String url_getBankCardAvailable = "bank/getBankCardAvailable";
//    /**
//     * 获取省市联动数据
//     */
//    public static String url_getTreeProvinceCities = "bank/getTreeProvinceCities";
//    /**
//     * 获取用户的默认银行卡
//     */
//    public static String url_getDefaultCard = "bank/getDefaultCard";
    /**
     * 读取用户已经绑定的银行卡
     */
    public static String url_getBindCard = "bank/getBindCard";
//    /**
//     * 验证当前充值的银行卡是否达到单笔或者每日限额
//     */
//    public static String url_checkBankCardAvailable = "bank/checkBankCardAvailable";
//    /**
//     * 新：用户提现手续费计算接口-2018-02-02
//     */
//    public static String url_v1_4postalFee = "bank/v1_4postalFee";
//    /**
//     * 新：用户提现接口-2018-02-02
//     */
//    public static String url_v1_4postal = "bank/v1_4postal";
//    /**
//     * 生产默认头像图片地址
//     */
//    public static String url_img_file = SERVER_FILE + "upload/head_pic_default.png";
//    /**
//     * 检测是否需要绑定微信  whether_need_bind 1：需要绑定
//     */
//    public static String url_isBind_weChat = "wechat/run";
//    /**
//     * 绑定微信昵称和头像
//     */
//    public static String url_bind_weChat = "wechat/bind";
//
//    /**
//     * 收付款码金额校验
//     */
//    public static String url_scantransferamount = "transfer/scantransferamount";
//    /**
//     * 扫码转账接口
//     */
//    public static String url_scantransfer = "transfer/scantransfer";
//
//    /**
//     * 群组内长时间未领取红包
//     */
//    public static String url_group_redpacket = "lumgw/smnho";
//    /**
//     * 收藏列表
//     */
//    public static String url_collection_list = "collection/select";
//    /**
//     * 添加收藏
//     */
//    public static String url_collection_add = "collection/add";
//    /**
//     * 收藏删除
//     */
//    public static String url_collection_delete = "collection/cancel";
//
//    /**
//     * 我设置的备注
//     */
//    public static String url_remarks_lists = "user/remarks";
//    /**
//     * 追踪红包
//     */
//    public static String url_track_redpacket = "redpacket/track";
//
////    /**
////     * 提现接口：计算手续费
////     */
////    public static String url_postalFee = "bank/postalFee";
////    /**
////     * 表情缩略图后缀
////     */
////    public static String url_emotion_suffix = "?x-oss-process=style/sy-pro-image";
////    /**
////     * 记录红包发放的所有流程日志
////     */
////    public static String url_write_log = "aliRedpacket/writeAliRedpacketLog";

    /***************************** VIP *********************/

    public static String url_pay_ali_order_info = "alipay/createOrder";
    public static String url_pay_wechat_order_info = "wxpay/createOrder";
    public static String url_red_package_coupon_history = "wxpay/createCouponHistory";
    public static String url_pay_wechat_order_red_package = "wxpay/createPayOrder";
    public static String url_vip_commodity_list = "user/vip_commodity_list";//获取会员列表
    public static String url_vip_share_link = "dqShareReg.html?shareUid=";//分享的链接
    public static String url_vip_exchange_record = "user/exchange_record";//获取邀请人数
    public static String url_vip_exchange_vip_list = "user/share_exchange_vip_list";//获取兑换列表
    public static String url_vip_exchange_vip = "user/exchange_vip";//兑换VIP

    /**************************** 我的零钱 ******************/
    public static String url_user_cloud_wallet = "user/user_cloud_wallet";//用户零钱信息
    public static String url_user_cash_withdrawal = "user/cash_withdrawal";//用户零钱提现
    public static String url_user_cloud_wallet_transaction_record = "user/cloud_wallet_transaction_record";//用户零钱提现记录
    public static String url_user_transaction_password = "user/transaction_password";//用户零钱支付密码设置/重置

    /**************************** 红包 ********************/
    public static String url_user_open_red_envelope = "active/cloud_red_envelope";//请求红包内容
    public static String url_get_user_red = "active/get_user_red";//获取红包提示内容
    public static String url_get_norRedTime = "active/norRedTime";//获取红包提示内容

    /**************************** 注销账号 ********************/
    public static String url_user_cancellation = "user/user_cancellation";//注销账号

    /********************************* end *******************************************/

    public static String url_user_query = "user/query_user";//查询账号


    public static String url_user_system = "user/userSystem";//提交系统名字


    /******************************** 朋友圈 start ****************************************/

    public static String url_dynamic_findUserDynamic = "dynamic/findUserDynamic";//获取朋友圈配置
    public static String url_dynamic_updateUserDynamic = "dynamic/updateUserDynamic";//更新朋友圈配置
    public static String url_dynamic_saveUserDynamicDesc = "dynamic/saveUserDynamicDesc";//保存朋友圈内容
    public static String url_dynamic_updateUserDynamicDesc = "dynamic/updateUserDynamicDesc";//更新朋友圈内容
    public static String url_dynamic_findUserDynamicDesc = "dynamic/findUserDynamicDesc";//获取朋友圈列表
    public static String url_dynamic_getUnreadDynamic = "dynamic/getUnreadDynamic";//获取朋友圈未读新消息
    public static String url_dynamic_saveUserDynamicLike = "dynamic/saveUserDynamicLike";//点赞朋友圈
    public static String url_dynamic_delUserDynamicLike = "dynamic/delUserDynamicLike";//取消点赞朋友圈
    public static String url_dynamic_saveUserDynamicComment = "dynamic/saveUserDynamicComment";//评论朋友圈
    public static String url_dynamic_delUserDynamicComment = "dynamic/delUserDynamicComment";//删除评论
    public static String url_dynamic_delUserDynamic = "dynamic/delUserDynamic";//删除朋友圈内容
    public static String url_dynamic_findUserDynamicPic = "dynamic/findUserDynamicPic";//获取指定数量的图片
    public static String url_dynamic_findUserDynamicMsg = "dynamic/findUserDynamicMsg";//获取朋友圈未读消息列表
    public static String url_dynamic_findUserDynamicMsgSum = "dynamic/findUserDynamicMsgSum";//获取朋友圈未读消息数
    public static String url_dynamic_delUserDynamicMsg = "dynamic/delUserDynamicMsg";//清空朋友圈未读消息
    public static String url_dynamic_userDynamicMsgMongoDao = "dynamic/userDynamicMsgMongoDao";//标记朋友圈未读的关于自己的动态消息
    public static String url_dynamic_readDynamic = "dynamic/readDynamic";//标记朋友圈未读的好友消息

    /******************************** 朋友圈 end ****************************************/

    /***************************** 赚钱功能 start **************************************/

    public static String url_task_screening = "task/task_screening";//任务筛选
    public static String url_task_list = "task/task_list";//任务排序
    public static String url_task_mine = "task/task_mine";//我的任务
    public static String url_task_registration = "task/task_registration";//任务报名
    public static String url_task_refresh = "task/task_refresh";//任务更新
    public static String url_task_detail = "task/task_detail";//任务详情
    public static String url_task_type = "task/task_type";//任务厂商分类
    public static String url_task_classification = "task/task_classification";//任务类型

    /***************************** 赚钱功能 end **************************************/

    /**************************** 发布任务功能 start **********************************/
    public static String url_task_getUserTask = "task/getUserTask";//获取我发布的所有任务
    public static String url_task_userTaskSelect = "task/userTaskSelect";//根据任务Id查询任务详情
    public static String url_task_createTask = "task/createTask";//创建任务
    public static String url_task_changeTask = "task/changeTask";//修改任务
    public static String url_task_changeTime = "task/changeTime";//修改时间
    public static String url_task_changeStatus = "task/changeStatus";//改变任务状态(提交任务或者撤回任务)
    public static String url_task_createTaskOrder = "wxpay/createTaskOrder";//付款状态
    public static String url_task_checkTask = "task/checkTask";//检查任务完成情况
    public static String url_task_drawback = "task/drawback";//退款
    public static String url_task_refundMoney = "task/refundMoney";//获取退款的金额信息
    /**************************** 发布任务功能 end **********************************/


    /************************** 积分商城功能 start ***********************************/
    public static String url_dbsign_sign = "dbsign/sign";//签到接口
    public static String url_dbsign_userDBMoney = "dbsign/userDBMoney";//积分内容
    public static String url_dbsign_changeDBCommodities = "dbsign/changeDBCommodities";//开始兑换
    public static String url_dbsign_getMoneyHistory = "dbsign/getMoneyHistory";//斗币明细
    public static String url_dbsign_getChangeHistory = "dbsign/getChangeHistory";//兑换记录的表
    public static String url_get_intviteReward = "dbsign/intviteReward";//获取邀请好友获取的奖励信息
    public static String url_get_vipVideoDB = "dbsign/vipVideoDB";//会员抢红包不中获得斗币
    public static String url_get_signRed = "dbsign/signRed";//签到红包
    public static String url_get_getUserRedCount = "dbsign/getUserRedCount";//获取当前签到红包的数量还有多少
    /************************** 积分商城功能 end ***********************************/
}
