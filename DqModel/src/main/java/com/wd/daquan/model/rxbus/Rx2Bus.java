package com.wd.daquan.model.rxbus;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.subscribers.SerializedSubscriber;

/**
 * rxJava事件订阅者
 * Created by Kind on 2018/9/20.
 */

public class Rx2Bus {

    private static FlowableProcessor<Object> mBus;
    private static volatile Rx2Bus mInstance = null;

    private Rx2Bus() {
        mBus = PublishProcessor.create().toSerialized();//调用toSerialized()方法，保证线程安全
    }

    public static synchronized Rx2Bus getInstance() {
        if (mInstance == null) {
            synchronized (Rx2Bus.class) {
                if (mInstance == null) {
                    mInstance = new Rx2Bus();
                }
            }
        }
        return mInstance;
    }

    /**
     * 发送消息
     */
    public void post(Object o) {
        SerializedSubscriber<Object> subscriber =new SerializedSubscriber<>(mBus);
        subscriber.onNext(o);
    }
    /**
     * 发送消息
     */
    public void postP(Object o) {
        SerializedSubscriber<Object> subscriber =new SerializedSubscriber<>(mBus);
        subscriber.onNext(o);
    }

    /**
     * 确定接收消息的类型
     */
    public <T> Flowable<T> toFlowable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }

    /**
     * 判断是否有订阅者
     */
    public boolean hasSubscribers() {
        return mBus.hasSubscribers();
    }
}
