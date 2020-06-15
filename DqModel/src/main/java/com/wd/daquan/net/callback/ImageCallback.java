package com.wd.daquan.net.callback;


import com.wd.daquan.net.parser.ImageParser;
import okhttp3.Call;

/**
 * @author: dukangkang
 * @date: 2018/5/4 18:59.
 * @description:
 *      图片回调接口
 */
public class ImageCallback extends BaseCallback {

    public ImageCallback() {
        super(new ImageParser());
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onSuccess(Call call, String url, int code, Object result) {
    }

    @Override
    public Object onInstallData(Call call, String url, Object result) {
        return result;
    }

    @Override
    public void onFailed(Call call, String url, int code, Object result, Exception e) {
    }

    @Override
    public void onFinish() {
    }
}
