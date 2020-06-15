package com.dq.im.bean;

import com.dq.im.type.EventBusBeanType;

/**
 * EventBus事件总线的实体类
 */
public class EventBusBean {
    private EventBusBeanType status = EventBusBeanType.TYPE_NET_WORK;//状态,0 网络状态
    private Object content = null;//内容，这个内容根据不同的状态值进行强转

    public EventBusBeanType getStatus() {
        return status;
    }

    public void setStatus(EventBusBeanType status) {
        this.status = status;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "EventBusModel{" +
                "status=" + status +
                ", content=" + content +
                '}';
    }
}