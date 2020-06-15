package com.wd.daquan.net;


import com.wd.daquan.net.interceptor.CacheInterceptor;
import com.wd.daquan.net.interceptor.GzipInterceptor;
import java.io.File;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * @author: dukangkang
 * @date: 2018/4/25 10:12.
 * @description: todo ...
 */
public class OkHttpClientHelper {

    private volatile static OkHttpClient mOkHttpClient = null;
    private volatile static OkHttpClient mCacheOkHttpClient = null;

    private static String CacheDir = "";
    private final static long CacheSize = 100 * 1024 * 1024; //100M
    private final static long TIME_OUT = 20;
    private final static long READTIME_OUT = 20;
    private final static long WRITETIME_OUT = 20;

    private final static long QUITC_TIME_OUT = 10;
    private final static long QUICK_READTIME_OUT = 10;

    // 证书
    public static SSLSocketFactory sSocketFactory = null;
    public static X509TrustManager sTrustManager = null;

    public static OkHttpClient getOkHttpClient(boolean isCache) {
        if (isCache) {
            return getCacheClient();
        } else {
            return getClient();
        }
    }

    /**
     * 获取OKHttpClient
     * @return
     */
    public static OkHttpClient getClient() {
        if (null == mOkHttpClient) {
            synchronized (OkHttpHelper.class) {
                if (mOkHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
                    builder.readTimeout(READTIME_OUT, TimeUnit.SECONDS);
                    builder.writeTimeout(WRITETIME_OUT, TimeUnit.SECONDS);
                    if (null != sSocketFactory && null != sTrustManager) {
                        builder.sslSocketFactory(sSocketFactory, sTrustManager);
                    }
                    mOkHttpClient = builder.build();
                }
            }
        }
        return mOkHttpClient;
    }

    /**
     * 获取缓存OKHttpClient
     * @return
     */
    public static OkHttpClient getCacheClient() {
        if (null == mCacheOkHttpClient) {
            synchronized (OkHttpHelper.class) {
                if (mCacheOkHttpClient == null) {
                    setCache(CacheDir, CacheSize);
                }
            }
        }
        return mCacheOkHttpClient;
    }

    /**
     * 获取GIZP OKHttpClient
     * @return
     * 注释：暂时未绕过证书
     */
    public static OkHttpClient getGzipClient() {
        return new OkHttpClient.Builder().addInterceptor(new GzipInterceptor())
                .connectTimeout(QUITC_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(QUITC_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(QUICK_READTIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 设置缓存 OkHttpClient
     * @param cacheDir
     * @param cacheSize
     */
    private static void setCache(String cacheDir, long cacheSize) {
        File file = new File(cacheDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        Cache cache = new Cache(file, cacheSize);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cache(cache);
        builder.addNetworkInterceptor(new CacheInterceptor());
        builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        builder.readTimeout(READTIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(WRITETIME_OUT, TimeUnit.SECONDS);
        if (null != sSocketFactory && null != sTrustManager) {
            builder.sslSocketFactory(sSocketFactory, sTrustManager);
        }
        mOkHttpClient = builder.build();

        //        mCacheOkHttpClient = new OkHttpClient.Builder()
//                .cache(cache)
//                .addNetworkInterceptor(new CacheInterceptor())
//                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
//                .readTimeout(READTIME_OUT, TimeUnit.SECONDS)
//                .writeTimeout(WRITETIME_OUT, TimeUnit.SECONDS)
//                .build();
    }


//    /**
//     * 设置证书
//     * @param builder
//     * @param context
//     */
//    public static void setCertificate(OkHttpClient.Builder builder, Context context) {
//        if (null == builder || null == context) {
//            return;
//        }
//        try {
//            InputStream certificates = context.getAssets().open("214489192530091.pem");
//            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
//            Certificate certificate = certificateFactory.generateCertificate(certificates);
//            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            keyStore.load(null, null);
//            keyStore.setCertificateEntry("214489192530091.pem", certificate);
//
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//
//            TrustManagerFactory trustManagerFactory =
//                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            trustManagerFactory.init(keyStore);
//
//
//            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
//            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
//                throw new IllegalStateException("Unexpected default trust managers:"
//                        + Arrays.toString(trustManagers));
//            }
//            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
//            sslContext.init
//                    (
//                            null,
//                            new TrustManager[]{trustManager},
//                            new SecureRandom()
//                    );
//            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//
//            builder.sslSocketFactory(sslSocketFactory, trustManager);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
