package com.dq.im.type;


import android.support.annotation.IntDef;

/**
 * Socket链接状态
 */
@IntDef({SocketStatus.SOCKET_STATUS_NO_CONNECT,SocketStatus.SOCKET_STATUS_CONNECTING,SocketStatus.SOCKET_STATUS_FAIL, SocketStatus.SOCKET_STATUS_CLOSE})
public @interface SocketStatus{
    int SOCKET_STATUS_NO_CONNECT = 0;
    int SOCKET_STATUS_CONNECTING = 1;
    int SOCKET_STATUS_FAIL = 2;
    int SOCKET_STATUS_CLOSE = 3;
}