package com.dq.im.third_system;

import android.text.TextUtils;
import android.util.Log;

import com.dq.im.DqWebSocketClient;
import com.dq.im.ImProvider;
import com.heytap.msp.push.HeytapPushManager;
import com.heytap.msp.push.callback.ICallBackResultService;

/**
 * Oppo推送通知
 */
public class OppoPushManager extends ThirdPushManager{
    private static OppoPushManager oppoPushManager;
    static {
        oppoPushManager = new OppoPushManager();
    }

    private OppoPushManager() {
    }

    public static OppoPushManager getOppoPushManager() {
        return oppoPushManager;
    }

    public void register(String appKey,String appSecret){
        HeytapPushManager.init(ImProvider.context, true);
        HeytapPushManager.register(ImProvider.context, appKey, appSecret, new ICallBackResultService(){
            @Override
            public void onRegister(int responseCode, String registerId) {
                Log.e("YM","OppO注册结果:"+registerId);
//                DqWebSocketClient.getInstance().sendHandlerMessage(DqWebSocketClient.REGISTER_OPPO_SUCCESS,registerId);
                if (!TextUtils.isEmpty(registerId)){
                    sendMessage("OPPO",registerId);
                }
            }

            @Override
            public void onUnRegister(int i) {

            }

            @Override
            public void onSetPushTime(int i, String s) {

            }

            @Override
            public void onGetPushStatus(int i, int i1) {

            }

            @Override
            public void onGetNotificationStatus(int i, int i1) {

            }
        });//setPushCallback接口也可设置callback
        HeytapPushManager.requestNotificationPermission();
    }

    /**
     * 判断Oppo推送是否支持
     * @return
     */
    public boolean isSupport(){
        return HeytapPushManager.isSupportPush();
    }

}