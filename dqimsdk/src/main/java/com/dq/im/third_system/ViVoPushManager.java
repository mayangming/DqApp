package com.dq.im.third_system;

import android.util.Log;

import com.dq.im.ImProvider;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;
import com.vivo.push.ups.CodeResult;
import com.vivo.push.ups.TokenResult;
import com.vivo.push.ups.UPSRegisterCallback;
import com.vivo.push.ups.UPSTurnCallback;
import com.vivo.push.ups.VUpsManager;

/**
 * VIVO推送
 */
public class ViVoPushManager{
    private static ViVoPushManager viVoPushManager;
    static {
        viVoPushManager = new ViVoPushManager();
    }

    private ViVoPushManager() {
    }

    public static ViVoPushManager getViVoPushManager() {
        return viVoPushManager;
    }

    public void initialize(){
        PushClient.getInstance(ImProvider.context).initialize();

    }
    public void register(String appId,String appKey,String appSecret){
        VUpsManager.getInstance().registerToken(ImProvider.context, appId, appKey, appSecret, new UPSRegisterCallback()   {

            @Override

            public void onResult(TokenResult tokenResult) {

                if   (tokenResult.getReturnCode() == 0) {

                    Log.d("YM_VIVO", "注册成功 regID = " +   tokenResult.getToken());

                } else {

                    Log.d("YM_VIVO", "注册失败");

                }

            }

        });
    }
    public void turnOnPush(){
        // 打开push开关, 关闭为turnOffPush，详见api接入文档
        PushClient.getInstance(ImProvider.context).turnOnPush(new IPushActionListener() {

            @Override

            public void onStateChanged(int state) {

                // TODO: 开关状态处理， 0代表成功
                Log.e("YM","开关:"+state);
            }

        });
        VUpsManager.getInstance().turnOnPush(ImProvider.context, new UPSTurnCallback() {

            @Override

            public void onResult(CodeResult codeResult) {

                if(codeResult.getReturnCode()   == 0){

                    Log.d("YM_VIVO", "初始化成功");

                }else {

                    Log.d("YM_VIVO", "初始化失败");

                }

            }

        });
    }

}