package com.wd.daquan.net.listener;

import java.io.IOException;

import okhttp3.Call;

/**
 * @Author: 方志
 * @Time: 2018/9/19 14:36
 * @Description:
 */
public interface IProgressListener {
    void onPreExecute(long contentLength);
    void update(long totalBytes, boolean done);
    void onError(Call call, IOException e);
}
