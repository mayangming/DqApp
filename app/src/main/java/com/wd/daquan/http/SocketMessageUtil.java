package com.wd.daquan.http;

import android.widget.Toast;

import com.dq.im.DqWebSocketClient;
import com.wd.daquan.DqApp;

/**
 * socket消息发送工具类
 */
public
class SocketMessageUtil {
    private static DqWebSocketClient dqWebSocketClient;
    static {
        dqWebSocketClient = DqWebSocketClient.getInstance();
    }

    private SocketMessageUtil() {
    }

    public static boolean sendMessage(boolean isCheckSocket, Object params){
        if (isCheckSocket){
            boolean isSocketConnecting = checkSocketConnectStatus();
            if (!isSocketConnecting){
                Toast.makeText(DqApp.sContext,"Socket链接失败,请稍后重试~",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        String messageContent = MessageJsonRequestUtil.createRequestJson(params);
        return dqWebSocketClient.sendMessage(messageContent);
    }

    public static boolean sendMessage(Object params){
        return sendMessage(true,params);
    }

    /**
     * 检查Socket链接状态
     */
    private static boolean checkSocketConnectStatus(){
        boolean isSocketConnecting = DqWebSocketClient.getInstance().isConnectIng();
        if (!isSocketConnecting){
            DqWebSocketClient.getInstance().build();
        }
        return isSocketConnecting;
    }
}