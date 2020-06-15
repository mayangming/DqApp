package com.wd.daquan.wxapi;

import com.netease.nim.uikit.common.util.string.MD5;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.wd.daquan.BuildConfig;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 微信支付工具类
 */
public class WxPayUtils{
    /**
     * sign签名
     * @return 签名内容
     */
    public static String createSign(PayReq request) {
        SortedMap<String,String> packageParams = getWxReqParams(request);
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<String,String>> es = packageParams.entrySet();
        for (Map.Entry<String,String> entry : es) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k).append("=").append(v).append("&");
            }
        }
        sb.append("key=".concat(BuildConfig.WX_PAY_API_KEY));//签名key需要与后端下单KEY保持一致
        return Objects.requireNonNull(MD5.getStringMD5(sb.toString())).toUpperCase();
    }
    private static SortedMap<String,String> getWxReqParams(PayReq request) {//map类型不要修改,SortedMap会对内容进行排序，顺序不同，最终生成的md5内容会不一样
        //相关key跟微信文档一致,相关内容参见下方链接
        //https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_12&index=2&index=2
        SortedMap<String,String> params = new TreeMap<>();
        params.put("appid",request.appId);
        params.put("partnerid",request.partnerId);
        params.put("prepayid",request.prepayId);
        params.put("package",request.packageValue);
        params.put("noncestr",request.nonceStr);
        params.put("timestamp",request.timeStamp);
        return params;
    }
}