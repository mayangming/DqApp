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

    public void registerXiaoMiSystemReceiver(String miAppId, String miAppKey){
        //初始化push推送服务
        if(shouldInit()) {
            MiPushClient.registerPush(ImProvider.context, miAppId, miAppKey);
        }
    }

    public void registerHwSystemReceiver(){
        getToken();
        initHwBuild();
    }

    public void registerThirdSystemReceiver(){

    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) ImProvider.context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = ImProvider.context.getApplicationInfo().processName;
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    private void getToken() {
        new Thread() {
            @Override
            public void run() {
                try {
                    // read from agconnect-services.json
                    String appId = AGConnectServicesConfig.fromContext(ImProvider.context).getString("client/app_id");
                    String token = HmsInstanceId.getInstance(ImProvider.context).getToken(appId, "HCM");
                    Log.i("YM", "get token:" + token);
                    if(!TextUtils.isEmpty(token)) {
                        sendRegTokenToServer(token);
                    }
                } catch (ApiException e) {
                    Log.e("YM", "get token failed, " + e);
                }
            }
        }.start();
    }
    private void sendRegTokenToServer(String token) {
        Log.i("YM", "sending token to server. token:" + token);
    }

    /**
     * 初始化华为配置
     */
    private void initHwBuild(){
        HmsMessaging.getInstance(ImProvider.context).turnOnPush().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()){
                    Log.e("YM","h华为---turnOnPush Complete");
                }else {
                    Log.e("YM","华为异常:"+task.getException().getMessage());
                }
            }
        });
    }
}