package com.wd.daquan.util.message_manager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import android.util.Log;

import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Socket发送的消息管理类，最初目的是为了在消息发送出去时候，如果超过某个时间没有移除掉的话则自动触发失败
 */
public class SocketMessageManager{
    private static int delayDuration = 5 * 1000;//延迟时间
    static int ACTION = 1;
    private static SocketMessageManager socketMessageManager;
//    private List<String> messageList = Collections.synchronizedList(new ArrayList<>());
    private ConcurrentHashMap<String,Long> messageMap = new ConcurrentHashMap<>();//同步的集合 key:clientId, value:加入的时间
    private Handler handler = new Handler(Looper.getMainLooper()){

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what){
                case 1:
                    checkMessage();
                    break;
            }
        }
    };
    static {
        socketMessageManager = new SocketMessageManager();
    }

    public static SocketMessageManager getInstance(){
        return socketMessageManager;
    }

    private SocketMessageManager(){

    }

    /**
     * 添加消息
     * @param key
     */
    public void putMessage(String key){
        messageMap.put(key,System.currentTimeMillis());
        handler.sendEmptyMessageDelayed(ACTION,delayDuration);
    }

    /**
     * 移除消息
     * @param key
     */
    public void removeMessage(String key){
        messageMap.remove(key);
    }

    private void checkMessage(){
        Log.e("YM","倒计时结束");
        Iterator<Map.Entry<String, Long>> entryIterator = messageMap.entrySet().iterator();
        while (entryIterator.hasNext()){
            Map.Entry<String, Long> entry = entryIterator.next();
            String clientId = entry.getKey();
            long lastTime = entry.getValue();
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime >= delayDuration){
                sendTimeOutMessage(clientId);
                entryIterator.remove();
            }
        }
    }

    //    {"messageId":1594284167742,"subSignal":"MS","msgIdServer":"667298404996481024","signal":"PUB_ACK","content":{"conversationType":7,"msgType":1,"msgIdClient":"43a3cb52-6cac-457a-8dfd-7b1c2dac0c5f","line":0,"messageId":1594284167742,"content":{"mentionedTargets":[],"mentionedType":0,"searchableContent":"RrIrqUTeomfjVLw6TCQCfQ==","type":0},"status":"1","msgSecondType":0,"timestamp":1594284168782}}
    private void sendTimeOutMessage(String clientId){
        MsgMgr.getInstance().sendMsg(MsgType.MESSAGE_RECEIVE_CALL_BACK_TIMEOUT, clientId);
    }

}