package com.dq.im.constants;


import com.dq.im.config.HttpConfig;

import java.util.ResourceBundle;

public class URLUtil {

    /**
     * 测试服务器地址
     */
    public static final String SERVER = " ";
    /**
     * 生产服务器地址
     */
//    public static final String SERVER = "http://39.106.158.176:8086/";
    /**
     * 获取用户信息
     */
    public static final String USER_INFO = "tio/userInfo";

    /**
     * 获取好友列表
     */
    public static final String USER_FRIEND_USER_LIST = "tio/friendUserList";

    /**
     * 获取群组列表
     */
    public static final String GROUP_LIST = "tio/myGroupList";

    /**
     * 获取群组信息
     */
    public static final String GROUP_INFO = "tio/groupInfo";

    /**
     * 创建群组
     */
    public static final String GROUP_CREATE = "tio/createGroup";

    /**
     * 添加好友
     */
    public static final String ADD_FRIEND = "tio/addFriend";

    /**
     * 获取历史聊天记录
     */
    public static final String USER_MSG_LIST = "tio/userMsgList";
    
    /**
     * 发送消息的接口
     */
    public static final String USER_SEND_MSG = "tio/sendMsg";

    /**
     * 登陆接口
     */
    public static final String USER_LOGIN = "tio/userLogin";

    /**
     * 获取红包信息的接口
     */
    public static final String RED_PACKAGE_DETAILS = "tio/getCoupon";

    /**
     * 打开红包接口
     */
    public static final String RED_PACKAGE_CREATE = "tio/createCouponHistory";

    /**
     * 获取红包收取列表信息
     */
    public static final String RED_PACKAGE_HISTORY = "tio/getCouponHistory";

    /**
     * 获取用户接口
     */
    public static String getURL(String url){
        return getServer().concat(url);
    }
    /**
     * 获取用户接口
     */
    public static String getURL(String host,String url){
        return host.concat(url);
    }

    public static String getServer(){
//        return ResourceBundle.getBundle("appConfig").getString("appServer");
        return HttpConfig.getInstance().getHTTP_SERVER_SDK();
    }

}