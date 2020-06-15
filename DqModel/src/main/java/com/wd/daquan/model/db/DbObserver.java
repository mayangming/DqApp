package com.wd.daquan.model.db;

import io.reactivex.FlowableEmitter;

public interface DbObserver<T> {

    void complete(FlowableEmitter<T> emitter);
}
