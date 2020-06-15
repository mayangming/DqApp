package com.wd.daquan.net.builder;

import android.text.TextUtils;
import java.io.File;
import java.util.LinkedList;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author: dukangkang
 * @date: 2018/4/24 17:11.
 * @description: todo ...
 */
public abstract class RequestBuilder {

    protected static final String TAG = "cn okhttp";
    // 是否开启缓存接口
    boolean isCache = false;
    // 是否开启压缩
    boolean isGzip = false;
    // 访问地址
    String url;
    // 传递json数据
    String json;
    // 上传单个文件KEY
    String fileKey;
    // 上传单个文件
    File file;
    // 上传多个文件
    LinkedList<File> files;
    // 请求参数
    Map<String, String> params;
    // 请求Header参数
    Map<String, String> headers;

    protected Object tag;

    public RequestBuilder cache(boolean cache) {
        this.isCache = cache;
        return this;
    }

    public RequestBuilder gzip(boolean gzip) {
        this.isGzip = gzip;
        return this;
    }

    public RequestBuilder url(String url) {
        this.url = url;
        return this;
    }

    public RequestBuilder params(Map params) {
        this.params = params;
        return this;
    }

    public RequestBuilder headers(Map headers) {
        this.headers = headers;
        return this;
    }

    public RequestBuilder file(File file) {
        this.file = file;
        return this;
    }

    public RequestBuilder fileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

    public RequestBuilder files(LinkedList<File> files) {
        this.files = files;
        return this;
    }

    public RequestBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }

    public RequestBuilder json(String json) {
        this.json = json;
        return this;
    }

    /**
     * 异步
     * @param callback
     * @return
     */
    public abstract Call enqueue(Callback callback);

    /**
     * 同步
     */
    public abstract Response execute() throws Exception;

    /**
     * 添加头部信息
     * @param builder
     * @param headers
     */
    protected void appendHeaders(Request.Builder builder, Map<String, String> headers) {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) {
            return;
        }
        for (String key : headers.keySet()) {
            if (key.equals("User-Agent")) {
                headerBuilder.set("User-Agent", headers.get(key));
            } else {
                headerBuilder.add(key, headers.get(key));
            }
        }
        builder.headers(headerBuilder.build());
    }

    /**
     * 添加POST请求参数
     * @param builder
     * @param params
     */
    protected void appendPostParams(FormBody.Builder builder, Map<String, String> params) {
        if (null == params || params.isEmpty()) {
            return;
        }

        for (String key : params.keySet()) {
            String value = params.get(key);
            if (TextUtils.isEmpty(value) || "null".equals(value)) {
                builder.add(key, "");
            } else {
                builder.add(key, value);
            }
        }
    }

    /**
     * 添加多个文件上传参数
     * @param builder
     * @param params
     */
    protected void appendMultiPostParams(MultipartBody.Builder builder, Map<String, String> params) {
        if (null == params || params.isEmpty()) {
            return;
        }
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (TextUtils.isEmpty(value) || "null".equals(value)) {
                builder.addFormDataPart(key, "");
            } else {
                builder.addFormDataPart(key, params.get(key));
            }
        }
    }

    /**
     * 添加GET请求参数
     * @param url
     * @param params
     * @return
     *  返回拼接后的地址
     */
    protected String appendGetParams(String url, Map<String, String> params) {
        StringBuilder builder = new StringBuilder();
        // 注意访问地址中包含"?" | "&"
        if (url.contains("&") || url.contains("?")) {
            builder.append(url);
        } else {
            builder.append(url + "?");
        }
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.append(key).append("=").append(params.get(key)).append("&");
            }
        }
        builder = builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}
