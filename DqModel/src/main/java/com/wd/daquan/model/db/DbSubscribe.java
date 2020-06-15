package com.wd.daquan.model.db;

import io.reactivex.annotations.NonNull;

public interface DbSubscribe<T> {

    void complete(@NonNull T t);
}
