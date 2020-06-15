package com.dq.im.type;

public enum  EventBusBeanType {
    TYPE_NET_WORK(0),//网络状态
    TYPE_P2P(1),//个人消息
    TYPE_TEAM(2),//群组消息
    TYPE_SYSTEM(3)//系统消息
    ;

    EventBusBeanType(int type) {
    }
}