package com.wd.daquan.net.callback;

import com.wd.daquan.net.bean.ResponseEntity;
import com.wd.daquan.net.parser.ObjectParser;
import okhttp3.Call;

/**
 * @author: dukangkang
 * @date: 2018/5/4 18:36.
 * @description:
 *      对象回调接口
 */
public class ObjectCallback<T extends ResponseEntity> extends BaseCallback<T> {

    public ObjectCallback() {
        super(new ObjectParser<T>());
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onSuccess(Call call, String url, int code, T result) {
    }

    @Override
    public T onInstallData(Call call, String url, T result) {
        return result;
    }

    @Override
    public void onFailed(Call call, String url, int code, T result, Exception e) {
    }

    @Override
    public void onFinish() {
    }
}
