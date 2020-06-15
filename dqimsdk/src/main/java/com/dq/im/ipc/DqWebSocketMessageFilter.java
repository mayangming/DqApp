package com.dq.im.ipc;

import okhttp3.WebSocket;

/**
 * 消息过滤器
 * 最初的目的是过滤某些消息不让向下处理，或者对某些消息处理后再进行向下处理
 * 比如，收到消息后都进行弹出通知栏，已经弹出后就不需要向下传递了
 */
public interface DqWebSocketMessageFilter {
    /**
     * 链接成功
     */
    void connectFail(String fileConnect);

    /**
     * 链接失败
     */
    void connectSuccess(WebSocket webSocket);

    /**
     * 链接关闭
     */
    void connectClose();

    /**
     * 消息接收处理
     * @param message
     */
    void onMessageReceiver(String message);
}