package com.wd.daquan.model.api;

import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.OpenSdkLoginBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/** sdk中间接口 */
public interface SdkApi {
    /**
     * 获取第三方应用信息
     */
    @POST
    Call<DataBean<OpenSdkLoginBean>> getAppInfo(@Url String url, @Body RequestBody requestBody);

    /**
     * 获取应用申请的code
     */
    @POST
    Call<DataBean<OpenSdkLoginBean>> getAppCode(@Url String url, @Body RequestBody requestBody);

    /**
     * 获取应用申请的accessToken
     */
    @POST
    Call<DataBean<OpenSdkLoginBean>> getAccessToken(@Url String url, @Body RequestBody requestBody);
}