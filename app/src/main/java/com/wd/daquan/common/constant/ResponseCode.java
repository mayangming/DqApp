package com.wd.daquan.common.constant;

/**
 * @author: dukangkang
 * @date: 2018/9/13 09:48.
 * @description: todo ...
 */
public interface ResponseCode {
    // 授权过期
    int EXPIRY_AUTH = KeyValue.Code.TOKEN_ERR;
    // 支付宝授权
    int ALIAPY_AUTH = 107801;
    // 红包已抢完
    int ALIAPY_RP_EMPTY = 103002;


}
