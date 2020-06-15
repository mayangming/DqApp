package com.wd.daquan.model.api;

import com.wd.daquan.model.bean.DataBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * QueryMap 通常用于get请求，参数拼接在url后
 * FieldMap 通常用于post请求，参数在请求体里面
 * 使用POST请求 使用到FieldMap Field属性，必须使用FormUrlEncoded
 */
public interface DqBaseApi {

    /**
     * 普通数据请求，无data数据
     */
    @POST
    Call<DataBean> request(@Url String url, @Body RequestBody requestBody);
}
