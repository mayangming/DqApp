package com.wd.daquan.glide.progress;

import android.text.TextUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProgressManager {

    private static Map<String, OnProgressListener> sHashMap = new ConcurrentHashMap<>();
    private volatile static OkHttpClient okHttpClient;

    private ProgressManager() {

    }

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (ProgressManager.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient.Builder().addNetworkInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            Response response = chain.proceed(request);

                            return response.newBuilder()
                                    .body(new ProgressResponseBody(request.url().toString(), mInternalProgressListener, response.body()))
                                    .build();
                        }
                    }).build();
                }
            }
        }
        return okHttpClient;
    }

    private static final ProgressResponseBody.InternalProgressListener mInternalProgressListener = new ProgressResponseBody.InternalProgressListener() {
        @Override
        public void onProgress(String url, long curBytes, long totalBytes) {
            OnProgressListener listener = getProgressListener(url);
            if (listener == null) {
                return;
            }

            int percent = (int) ((curBytes * 1f / totalBytes) * 100f);
            boolean isComplete = percent >= 100;
            listener.onProgress(isComplete, percent, curBytes, totalBytes);

            if (isComplete) {
                removeListener(url);
            }
        }
    };

    public static void addListener(String url, OnProgressListener listener) {
        if (TextUtils.isEmpty(url) || listener == null) {
            return;
        }
        sHashMap.put(url, listener);
        listener.onProgress(false, 1, 0, 0);
    }

    public static void removeListener(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        sHashMap.remove(url);
    }

    public static OnProgressListener getProgressListener(String url) {
        if (TextUtils.isEmpty(url) || sHashMap == null || sHashMap.size() == 0) {
            return null;
        }

        OnProgressListener onProgressListener = sHashMap.get(url);
        if (onProgressListener != null) {
            return onProgressListener;
        }
        return null;
    }

}
