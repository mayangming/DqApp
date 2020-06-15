package com.wd.daquan.common.helper;

import com.wd.daquan.common.constant.Constants;
import com.wd.daquan.common.constant.KeyValue;

/**
 * @author: dukangkang
 * @date: 2018/10/31 13:42.
 * @description: todo ...
 */
public class QrcodeManager {

    /**
     * 是否是斗圈二维码中地址
     * @param url
     * @return
     */
    public static boolean isQrcodeUrl(String url) {
        if (!url.contains(KeyValue.QR.KEY_CN_QRCODE)
                && !url.contains(KeyValue.ALIPAY)
                && !url.contains(KeyValue.QR.KEY_QC_WEB_QRCODE)
                && !url.contains(Constants.RECEIPT_QR_CODE)) {
            return false;
        }
        return true;
    }

    /**
     * 是否是web端登录二维码
     * @param url
     * @return
     */
    public static boolean isWebQrcodeUrl(String url) {
        if (url.contains(KeyValue.HTTP) && url.contains(KeyValue.QR.KEY_QC_WEB_QRCODE)) {
            return true;
        }
        return false;
    }

    /**
     * 是否是下载链接
     * @param url
     * @return
     */
    public static boolean isDownloadQrcodeUrl(String url) {
//        if (url.contains(KeyValue.OPEN_CLIENT_UNIQUE) && url.contains(KeyValue.QR.KEY_UNIQUE)) {
//            return true;
//        }
        if (url.contains(KeyValue.QR.KEY_UNIQUE)) {
            return true;
        }
        return false;
    }

    /**
     * 是否是支付宝链接
     * @param url
     * @return
     */
    public static boolean isAlipayQrcodeUrl(String url) {
        if (url.contains(KeyValue.ALIPAY)) {
            return true;
        }
        return false;
    }

}
