package com.wd.daquan.http;

import android.util.Log;
import android.widget.Toast;

import com.dq.im.DqWebSocketClient;
import com.wd.daquan.DqApp;
import com.wd.daquan.imui.bean.im.DqImBaseBean;
import com.wd.daquan.model.utils.GsonUtils;
import com.wd.daquan.util.message_manager.SocketMessageManager;

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
//        String messageContent = MessageJsonRequestUtil.createRequestJson(params);
        String messageContent = GsonUtils.toJson(params);
        Log.e("YM","发送的Socket消息内容:"+messageContent);
        return dqWebSocketClient.sendMessage(messageContent);
    }

    public static boolean sendMessage(Object params){
        if (params instanceof DqImBaseBean){
            DqImBaseBean dqImBaseBean = (DqImBaseBean) params;
            SocketMessageManager.getInstance().putMessage(dqImBaseBean.getContent().getMsgIdClient());
        }
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