package com.dq.im;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.dq.im.ipc.DqWebSocketMessageFilter;
import com.dq.im.util.JsonCreateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * webSocket回调监听
 */
public class EchoWebSocketListener extends WebSocketListener {
    private static final int SOCKET_HEART_BEAT = 0;//心跳包
    private static final int SOCKET_OPEN = 1;//创建链接
    private static final int SOCKET_MESSAGE = 2;//接收到消息
    private static final int SOCKET_HEART_FAIL = 3;//失败
    private static final int SOCKET_HEART_CLOSE = 4;//关闭

    private WebSocket mSocket;
    private DqWebSocketMessageFilter dqWebSocketListener;
    private int socketHeartDuration = 20 * 1000;//心跳包间隔
    private String userId = "";//用户唯一编号

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SOCKET_HEART_BEAT:
                    String heartJson = JsonCreateUtil.getSystemDeviceJson(userId);
                    mSocket.send(heartJson);
                    handler.sendEmptyMessageDelayed(SOCKET_HEART_BEAT,socketHeartDuration);
                    break;
                case SOCKET_OPEN:
                    if (null != dqWebSocketListener){
                        dqWebSocketListener.connectSuccess(mSocket);
                    }
                    break;
                case SOCKET_MESSAGE:
                    String message = msg.obj.toString();
                    messageReceipt(message);

                    if (!TextUtils.isEmpty(message) && !message.equals("OK")){//每次发消息给服务器，服务器都会回传一个"OK"
                        if (null != dqWebSocketListener){
                            dqWebSocketListener.onMessageReceiver(message);
                        }
                    }
                    break;
                case SOCKET_HEART_FAIL:
                    Object obj = msg.obj;
                    if (null == obj){
                        if (null != dqWebSocketListener){
                            dqWebSocketListener.connectFail(null);
                        }
                        break;
                    }
                    String failContent = obj.toString();
                    if (null != dqWebSocketListener){
                        dqWebSocketListener.connectFail(failContent);
                    }
                    break;
                case SOCKET_HEART_CLOSE:
                    if (null != dqWebSocketListener){
                        dqWebSocketListener.connectClose();
                    }
                    break;
            }
        }
    };

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        mSocket = webSocket;
//        mSocket.cancel();//断开链接
//        String openid = SystemMsgUtil.getAndroidId();
//        //连接成功后，发送登录信息
//        String message = "{\"type\":\"login\",\"userId\":\""+openid+"\"}";
//        mSocket.send(message);
//        output("连接成功！");
        Log.e("YM","链接成功");
        handler.sendEmptyMessage(SOCKET_OPEN);
        handler.sendEmptyMessage(SOCKET_HEART_BEAT);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
//        output("receive bytes:" + bytes.hex());
        Log.e("YM","receive bytes");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
//        output("receive text:" + text);
        Log.e("YM","onMessage:"+text);
        Message message = Message.obtain();
        message.what = SOCKET_MESSAGE;
        message.obj = text;
        handler.sendMessage(message);
        //收到服务器端发送来的信息后，每隔25秒发送一次心跳包
//        final String message = "{\"type\":\"heartbeat\",\"user_id\":\"heartbeat\"}";
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
//        output("closed:" + reason);
        Log.e("YM","onClosed");
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
//        output("closing:" + reason);
        Log.e("YM","onClosing:"+reason);
        handler.sendEmptyMessage(SOCKET_HEART_CLOSE);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
//        output("failure:" + t.getMessage());
        t.printStackTrace();
        Log.e("YM","onFailure:"+t.getMessage());
        Message message = Message.obtain();
        message.what = SOCKET_HEART_FAIL;
        message.obj = t.getMessage();
        handler.sendMessage(message);
    }

    public void setDqWebSocketListener(DqWebSocketMessageFilter dqWebSocketListener){
        this.dqWebSocketListener = dqWebSocketListener;
    }

    /**
     * 获取WebSocket实例
     */
    public WebSocket getWebSocket(){
        return mSocket;
    }

    public boolean senMessage(String message){
        boolean isResult = false;
        if (null != mSocket){
            isResult = mSocket.send(message);
            Log.e("YM","发送消息的结果:"+isResult);
        }
        if (isResult){
            handler.removeMessages(SOCKET_HEART_BEAT);
            handler.sendEmptyMessageDelayed(SOCKET_HEART_BEAT,socketHeartDuration);
        }
        return isResult;
    }

    /**
     * 清除所有消息
     */
    public void clear(){
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * 停止发送心跳包
     */
    public void stopSendHeartBeat(){
        handler.removeMessages(SOCKET_HEART_CLOSE);
    }

    /**
     * 消息回执
     * 收到消息后回执给服务器，告诉本次消息已接收
     */
    private void messageReceipt(String message){
        if (message.equals("OK")){//每次向服务器发消息，服务器都会回一个"OK"
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(message);
            if (!jsonObject.has("msgIdServer")){
                return;
            }
            String msgIdServer = new JSONObject(message).getString("msgIdServer");
            if (TextUtils.isEmpty(msgIdServer)){
                return;
            }
            String receiptJson = JsonCreateUtil.getMessageReceiptJson(msgIdServer);
            mSocket.send(receiptJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}