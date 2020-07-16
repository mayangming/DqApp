package com.dq.im.third_system;

import android.text.TextUtils;
import android.util.Log;

import com.dq.im.ImProvider;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.push.HmsMessaging;

import java.lang.reflect.Method;

public class HwPushManager extends ThirdPushManager{
    private static HwPushManager hwPushManager;
    static {
        hwPushManager = new HwPushManager();
    }

    private HwPushManager() {
    }

    public static HwPushManager getHwPushManager() {
        return hwPushManager;
    }


    public void registerHwSystemReceiver(){
        getToken();
        initHwBuild();
    }

    private void getToken() {
        new Thread() {
            @Override
            public void run() {
                try {
                    // read from agconnect-services.json
                    String appId = AGConnectServicesConfig.fromContext(ImProvider.context).getString("client/app_id");
                    String token = HmsInstanceId.getInstance(ImProvider.context).getToken(appId, "HCM");
                    Log.e("YM", "get token:" + token);
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
        HwPushManager.getHwPushManager().sendMessage(ThirdSystemType.HUA_WEI,token);
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
    /**
     * 判断是否可以使用华为推送
     *
     * @return
     */
    public Boolean canHuaWeiPush() {

        int emuiApiLevel = 0;
        try {
            Class cls = Class.forName("android.os.SystemProperties");
            Method method = cls.getDeclaredMethod("get", new Class[]{String.class});
            emuiApiLevel = Integer.parseInt((String) method.invoke(cls, new Object[]{"ro.build.hw_emui_api_level"}));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return emuiApiLevel > 5.0;

    }
}