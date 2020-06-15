package com.wd.daquan.net.callback;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.wd.daquan.net.parser.BaseParser;
import com.wd.daquan.net.parser.ParseType;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author: dukangkang
 * @date: 2018/4/24 18:12.
 * @description: todo ...
 */
public abstract class BaseCallback<T> implements Callback {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private BaseParser<T> mParser = null;

    private Type mGenericityType;

    private String mUrl;

    public BaseCallback(BaseParser<T> parser) {
        init();
        mParser = parser;
        mParser.mType = mGenericityType;
    }

    private void init() {
        // 获取泛型类型 ，eg: mGenericityType = com.aides.brother.qingchat.entity.DataBean<com.aides.brother.qingchat.bean.GroupResp>
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            this.mGenericityType = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        } else {
            this.mGenericityType = Object.class;
        }
    }

    @Override
    public void onFailure(final Call call, final IOException e) {
        Log.e("fz", "onFailure ： " + e.toString());

        mHandler.post(new Runnable() {
            @Override
            public void run() {
//                onFailed(call, mUrl, -1, null, new Exception("-1"));
                onFailed(call, mUrl, -1, null, e);
                onFinish();
            }
        });
    }

    @Override
    public void onResponse(final Call call, final Response response) {
        Log.e("fz", "onResponse ： " + response.body().toString());
        try {
            mUrl = response.request().url().toString();
//            Log.w("chuiniu", "请求地址：" + mUrl);
            final T obj = mParser.parseResponse(response);
            // 接口响应状态码
            final int code = response.code();
            // 组装数据
            final T result = onInstallData(call, mUrl, obj);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (response.isSuccessful() && result != null) {
                        // 对象类型
                        if (ParseType.OBJECT.equals(mParser.mParseType)) {
                            if (mParser.isSuccess) {
                                response.body().close();
                                onSuccess(call, mUrl, mParser.respCode, result);
                                onFinish();
                            } else {
                                response.body().close();
                                onFailed(call, mUrl, mParser.respCode, result, new Exception(Integer.toString(mParser.respCode)));
                                onFinish();
                            }
                        }
                        // 图片类型
                        else if (ParseType.IMAGE.equals(mParser.mParseType)) {
                            response.body().close();
                            onSuccess(call, mUrl, 0, result);
                            onFinish();
                        }
                        // 字符串类型
                        else if (ParseType.STRING.equals(mParser.mParseType)) {
                            response.body().close();
                            onSuccess(call, mUrl, 0, result);
                            onFinish();
                        } else {
                            response.body().close();
                            onFailed(call, mUrl, -1, result, new Exception(Integer.toString(-1)));
                            onFinish();
                        }
                    } else {
                        onFailed(call, mUrl, code, result, new Exception(Integer.toString(code)));
                        onFinish();
                    }
                }
            });
        } catch (final Exception e) {
            e.printStackTrace();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onFailed(call, mUrl, -1, null, e);
                    onFinish();
                }
            });
        }
    }

    public abstract void onStart();

    public abstract void onSuccess(Call call, String url, int code, T result);

    /**
     * 异步处理数据
     *
     * @param call
     * @param url
     * @param result
     * @return
     */
    public abstract T onInstallData(Call call, String url, T result);

    public abstract void onFailed(Call call, String url, int code, T result, Exception e);

    public abstract void onFinish();
}
