package com.dq.im.third_system;

import android.content.Context;
import android.util.Log;

import com.dq.im.DqWebSocketClient;
import com.vivo.push.PushClient;
import com.vivo.push.model.UPSNotificationMessage;
import com.vivo.push.sdk.OpenClientPushMessageReceiver;

/**
 * ViVo推送接收
 */
public class ViVoSystemMessageReceiver extends OpenClientPushMessageReceiver {

    /***
     * 当通知被点击时回调此方法
     * @param context 应用上下文
     * @param msg 通知详情，详细信息见API接入文档
     */
    @Override
    public void onNotificationMessageClicked(Context context, UPSNotificationMessage upsNotificationMessage) {

    }
    /***
     * 当首次turnOnPush成功或regId发生改变时，回调此方法
     * 如需获取regId，请使用PushClient.getInstance(context).getRegId()
     * @param context 应用上下文
     * @param s 注册id
     */
    @Override
    public void onReceiveRegId(Context context, String s) {
        Log.e("YM","VIVO注册的ID:"+s);
        DqWebSocketClient.getInstance().sendHandlerMessage(DqWebSocketClient.REGISTER_VIVO_SUCCESS,s);
    }
}