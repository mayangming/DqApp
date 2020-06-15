package com.wd.daquan.net.builder;

import android.text.TextUtils;
import android.util.Log;

import com.wd.daquan.model.utils.GsonUtils;
import com.wd.daquan.net.OkHttpHelper;
import com.wd.daquan.net.callback.BaseCallback;

import java.io.File;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author: dukangkang
 * @date: 2018/4/24 17:15.
 * @description: todo ...
 */
public class PostRequestBuilder extends RequestBuilder {
    // 二进制流数据,常见文件下载／上传
    private final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");
    // Json数据格式
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public Call enqueue(Callback callback) {
        if (TextUtils.isEmpty(url)) {
            Log.e(TAG, "post enqueue url can't be empty !");
            return null;
        }

        Call call = null;
        try {
            Request.Builder builder = new Request.Builder().url(url);
            if (tag != null)
                builder.tag(tag);
            if(TextUtils.isEmpty(json)) {
                json = GsonUtils.toJson(params);
            }


            if (!TextUtils.isEmpty(json)) {
                RequestBody jsonBody = RequestBody.create(JSON, json);
                builder.post(jsonBody);
            } else if (files != null && files.size() > 0) {
                //多文件上传
                /* form的分割线,自己定义 */
                String boundary = "xx--------------------------------------------------------------xx";
                MultipartBody.Builder mBuilder = new MultipartBody.Builder(boundary).setType(MultipartBody.FORM);
                for (int i = 0; i < files.size(); i++) {
                    RequestBody fileBody = RequestBody.create(MEDIA_TYPE_STREAM, (File) files.get(i));
                    mBuilder.addFormDataPart("file" + (i + 1), "file" + (i + 1), fileBody);
                }
                appendMultiPostParams(mBuilder, params);
                MultipartBody body = mBuilder.build();
                builder.post(body);
            } else if (null != file) {
                //单文件上传
                if (TextUtils.isEmpty(fileKey)) {
                    builder.post(RequestBody.create(MEDIA_TYPE_STREAM, file));
                } else {
                    MultipartBody.Builder mBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    String name = file.getName();
                    RequestBody requestBody = RequestBody.create(MEDIA_TYPE_STREAM, file);
                    mBuilder.addFormDataPart(fileKey, name, requestBody);

                    appendMultiPostParams(mBuilder, params);

                    MultipartBody body = mBuilder.build();
                    builder.post(body);
                }
            } else {
                FormBody.Builder postBuilder = new FormBody.Builder();
                appendPostParams(postBuilder, params);
                builder.post(postBuilder.build());
            }
            appendHeaders(builder, headers);
            Request request = builder.build();
            if (callback instanceof BaseCallback) {
                ((BaseCallback) callback).onStart();
            }

            if (isGzip) {
                call = OkHttpHelper.getGzipClient().newCall(request);
            } else {
                call = OkHttpHelper.getOkHttpClient(isCache).newCall(request);
            }
//            Log.e("TAG", "request ： " + request.toString());
//            Log.e("TAG", "params : " + params.toString());
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
            call = null;
        }

        return call;
    }

    @Override
    public Response execute() throws Exception {
        if (TextUtils.isEmpty(url)) {
            Log.e(TAG, "post execute url can't be empty !");
            return null;
        }

        Request.Builder builder = new Request.Builder().url(url);
        if (tag != null) {
            builder.tag(tag);
        }

        FormBody.Builder postBuilder = new FormBody.Builder();
        appendPostParams(postBuilder, params);
        builder.post(postBuilder.build());
        appendHeaders(builder, headers);
        Request request = builder.build();
        Call call = OkHttpHelper.getOkHttpClient(isCache).newCall(request);
        return call.execute();
    }

}
