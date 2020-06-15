package com.wd.daquan.login.listener;

import java.util.Map;

/**
 * Created by zht on 2018/7/4.
 * 微信登录回调
 */

public interface WXLoginListener {
    void loginWX(Map<String, String> map);
}
