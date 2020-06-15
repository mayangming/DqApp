package com.wd.daquan.model.rxbus;

/**
 * RxBus事件监听回调
 * Created by Kind on 2018/9/20.
 */

public interface QCObserver {

    /**
     * 事件回调
     *
     * @param key   事件key
     * @param value 事件传递值
     */
    void onMessage(String key, Object value);
}
