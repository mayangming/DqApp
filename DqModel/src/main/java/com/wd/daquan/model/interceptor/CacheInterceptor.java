package com.wd.daquan.model.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 *  网络请求缓存拦截器
 */
public class CacheInterceptor implements Interceptor {

    // 默认缓存3分钟
    private final static long CACHE_TIME = 60 * 3;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        return response.newBuilder()
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, max-age=" + CACHE_TIME)
                .removeHeader("Pragma")
                .build();
    }
}
