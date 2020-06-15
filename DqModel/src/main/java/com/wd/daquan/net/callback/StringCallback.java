package com.wd.daquan.net.callback;

import com.wd.daquan.net.parser.StringParser;
import okhttp3.Call;

/**
 * @author: dukangkang
 * @date: 2018/5/4 18:36.
 * @description:
 *      字符串回调接口
 */
public class StringCallback extends BaseCallback {

    public StringCallback() {
        super(new StringParser());
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onSuccess(Call call, String url, int code, Object result) {
    }

    @Override
    public Object onInstallData(Call call, String url,Object result) {
        return result;
    }

    @Override
    public void onFailed(Call call, String url, int code, Object result, Exception e) {
    }

    @Override
    public void onFinish() {
    }
}
