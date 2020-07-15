package com.dq.im.third_system;

import com.dq.im.ImProvider;
import com.heytap.msp.push.HeytapPushManager;
import com.heytap.msp.push.callback.ICallBackResultService;

/**
 * Oppo推送通知
 */
public class OppoPushManager {
    private static OppoPushManager oppoPushManager;
    static {
        oppoPushManager = new OppoPushManager();
    }

    private OppoPushManager() {
    }

    public static OppoPushManager getOppoPushManager() {
        return oppoPushManager;
    }

    public void init(){
        HeytapPushManager.init(ImProvider.context, true);
        HeytapPushManager.register(ImProvider.context, "e3d8c90c895646dda7c3d22db2e646cf", "e3d8c90c895646dda7c3d22db2e646cf", new ICallBackResultService(){
            @Override
            public void onRegister(int i, String s) {

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

}