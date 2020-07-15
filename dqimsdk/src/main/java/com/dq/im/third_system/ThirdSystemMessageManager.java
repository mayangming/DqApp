package com.dq.im.third_system;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import com.dq.im.ImProvider;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.push.HmsMessaging;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

/**
 * 第三方推送管理类
 */
public class ThirdSystemMessageManager{
    private static ThirdSystemMessageManager thirdSystemMessageManager;

    static {
        thirdSystemMessageManager = new ThirdSystemMessageManager();
    }

    private ThirdSystemMessageManager() {

    }

    public static ThirdSystemMessageManager getInstance(){
        return thirdSystemMessageManager;
    }







}