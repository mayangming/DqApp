package com.wd.daquan.common.constant;

/**
 * Created by DELL on 2018/9/10.
 */

public interface Constants {

    String WEBVIEW_URL = "webview_url";
    String WEBVIEW_TITLE = "webview_title";
    String GROUP_ASSIST_AUTH = "group_assist_auth";

    String SCREENSHOT_ANDROID = "?x-oss-process=video/snapshot,t_0000,f_jpg";
    String SCREENSHOT_IOS = "";

    String DRAFT = "[草稿] ";

    public static final String GROUP_MASTER_TRANSFER="x0000001";//群主转让

    /**
     * 二维码前缀
     */
    String RECEIPT_QR_CODE= DqUrl.H5_QRCODE + "/"+ KeyValue.QR.KEY_UNIQUE + "?"+KeyValue.QR.DQ;

    /**
     * 客户端跳转域名
     */
    String OPEN_CLIENT_UNIQUE="www.meetsn.com";

    /**
     * 识别二维码使用
     */
    String TYPE = "type";

    /**
     * 二维码中uid 默认加的值
     */
    int TARGET_VALUE = 2000000;

    public interface Web {
        public String Login = "0";
        public String Logout = "2";
    }

    //账号密码登录
    String PWD_MD5 = "zRbbY9ta$lwkhyl&@W&N";
    //支付密码秘钥
    String PAY_MD5="D2@S&7g^@NE795$H";
}
