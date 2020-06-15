package com.wd.daquan.net;

import android.content.Context;

import com.wd.daquan.net.bean.RequestEntity;
import com.wd.daquan.net.builder.GetRequestBuilder;
import com.wd.daquan.net.builder.PostRequestBuilder;
import com.wd.daquan.net.callback.ImageCallback;
import com.wd.daquan.net.callback.ObjectCallback;
import com.wd.daquan.net.callback.StringCallback;

import java.io.File;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * @author: dukangkang
 * @date: 2018/4/25 09:48.
 * @description: todo ...
 */
public class OkHttpHelper {

    public static OkHttpClient getOkHttpClient(boolean isCache) {
        return OkHttpClientHelper.getOkHttpClient(isCache);
    }

    public static OkHttpClient getGzipClient() {
        return OkHttpClientHelper.getGzipClient();
    }

    public static void postByObject(RequestEntity entity, ObjectCallback callback) {
        new PostRequestBuilder().url(entity.getUrl()).tag(entity.getTag()).cache(entity.isCache()).params(entity.getParams()).headers(entity.getHeaders()).enqueue(callback);
    }

    public static void postByObject(RequestEntity entity, File file, String fileKey, ObjectCallback callback) {
        new PostRequestBuilder().url(entity.getUrl()).tag(entity.getTag()).cache(entity.isCache()).file(file).fileKey(fileKey).params(entity.getParams()).headers(entity.getHeaders()).enqueue(callback);
    }

    public static void postByString(RequestEntity entity, StringCallback callback) {
        new PostRequestBuilder().url(entity.getUrl()).tag(entity.getTag()).cache(entity.isCache()).params(entity.getParams()).enqueue(callback);
    }


    public static void loadImage(String url, ImageCallback callback) {
        new GetRequestBuilder().url(url).enqueue(callback);
    }

    /**
     * 设置证书
     * @param context
     */
    public static void setCertificate(Context context, String certificateName) {
        if (null == context) {
            return;
        }
        try {
            InputStream is = context.getAssets().open(certificateName);
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Certificate certificate = certificateFactory.generateCertificate(is);
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry(certificateName, certificate);

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);


            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
            sslContext.init
                    (
                            null,
                            new TrustManager[]{trustManager},
                            new SecureRandom()
                    );
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClientHelper.sSocketFactory = sslSocketFactory;
            OkHttpClientHelper.sTrustManager = trustManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
