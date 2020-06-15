package com.dq.im.ipc;

/**
 * 斗圈WebSocket回调接口
 */
public interface DqWebSocketListener{
    /**
     * 链接成功
     */
    void connectFail(String fileConnect);

    /**
     * 链接失败
     */
    void connectSuccess();

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