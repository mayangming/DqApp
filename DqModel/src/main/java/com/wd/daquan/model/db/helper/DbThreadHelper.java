package com.wd.daquan.model.db.helper;

import com.wd.daquan.model.db.DbObserver;
import com.wd.daquan.model.db.DbSubscribe;

import io.reactivex.BackpressureStrategy;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.schedulers.Schedulers;

/**
 * 存数据最好异步存，取可异步或同步
 */
public class DbThreadHelper {

    public Disposable disposable;

    public <T> void toFlowable(DbObserver<T> observer, DbSubscribe<T> subscribe){

        disposable = FlowableProcessor
                .create((FlowableOnSubscribe<T>) emitter -> {
                    if(observer != null) {
                        observer.complete(emitter);
                        //Log.e("dq", "observer thread ： " + Thread.currentThread().getName());
                    }
                }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tData -> {
                    if(tData != null && subscribe != null) {
                        subscribe.complete(tData);
                        //Log.e("dq", "subscribe thread ： " + Thread.currentThread().getName());
                    }
                });
    }

    public void clear(){
        if(disposable != null) {
            disposable.dispose();
        }
    }
}
