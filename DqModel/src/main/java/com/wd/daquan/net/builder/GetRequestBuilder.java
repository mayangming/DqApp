package com.wd.daquan.net.builder;

import android.text.TextUtils;
import android.util.Log;
import com.wd.daquan.net.OkHttpHelper;
import com.wd.daquan.net.callback.BaseCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author: dukangkang
 * @date: 2018/4/24 17:15.
 * @description: todo ...
 */
public class GetRequestBuilder extends RequestBuilder {

    @Override
    public Call enqueue(Callback callback) {
        if (TextUtils.isEmpty(url)) {
            Log.e(TAG, "get enqueue request url is empty !");
            return null;
        }
        if (params != null && params.size() > 0) {
            url = appendGetParams(url, params);
        }
        Request.Builder builder = new Request.Builder().url(url);
        appendHeaders(builder, headers);
        if (tag != null) {
            builder.tag(tag);
        }

        Log.w(TAG, "url : " + url);
        Request request = builder.build();

        if (callback instanceof BaseCallback) {
            ((BaseCallback) callback).onStart();
        }
        Call call = OkHttpHelper.getOkHttpClient(isCache).newCall(request);
        call.enqueue(callback);
        return call;
    }

    @Override
    public Response execute() throws Exception {
        if (TextUtils.isEmpty(url)) {
            Log.e(TAG, "get execute request url is empty !");
            return null;
        }
        if (params != null && params.size() > 0) {
            url = appendGetParams(url, params);
        }
        Request.Builder builder = new Request.Builder().url(url);
        appendHeaders(builder, headers);
        if (tag != null) {
            builder.tag(tag);
        }

        Request request = builder.build();
        Call call = OkHttpHelper.getOkHttpClient(isCache).newCall(request);
        return call.execute();
    }


}
