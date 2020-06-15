package com.wd.daquan.model.retrofit;


import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.wd.daquan.model.BuildConfig;
import com.wd.daquan.model.ModelConfig;
import com.wd.daquan.model.interceptor.CacheInterceptor;
import com.wd.daquan.model.log.DqLog;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * RetrofitClient请求类
 */
public class RetrofitClient {

    private File httpCacheDirectory;
    private Cache cache;
    private final static long CacheSize = 50 * 1024 * 1024; //50M
    //超时时间
    private static final int DEFAULT_TIMEOUT = 10;

    private final static long QUITC_TIME_OUT = 1;
    private final static long QUICK_READTIME_OUT = 1;

    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Retrofit getRetrofit() {

        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(ModelConfig.getContext().getCacheDir(), "dq_cache");
        }

        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, CacheSize);
            }
        } catch (Exception e) {
            DqLog.e("Could not create http cache" + e);
        }
        //HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
//                .addInterceptor(new BaseInterceptor(headers))
                .addInterceptor(new CacheInterceptor())
                .addInterceptor(getLoggingInterceptor())
//                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(4, 10, TimeUnit.SECONDS))
                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里4个，和每个保持时间为10s
                .build();


        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BuildConfig.SERVER)
                .build();
    }

    private LoggingInterceptor getLoggingInterceptor(){
        LoggingInterceptor loggingInterceptor = new LoggingInterceptor.Builder()
                .loggable(BuildConfig.DEBUG)
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .addHeader("version", BuildConfig.VERSION_NAME)
                .addQueryParam("query", "0")
                .enableAndroidStudio_v3_LogsHack(true)
                .enableMock(false, 1000L, request -> {
                    String segment = request.url().pathSegments().get(0);
                    return Okio.buffer(Okio.source(ModelConfig.getContext().getAssets().open(String.format("mock/%s.json", segment)))).readUtf8();
                })
                .executor(Executors.newSingleThreadExecutor())
                .build();
        return loggingInterceptor;
    }

}
