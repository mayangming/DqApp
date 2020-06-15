package com.wd.daquan.model.api;

import com.wd.daquan.model.bean.DataBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface TestApi {
    /**
     * 注册
     */
    @POST("user/reg")//固定路径
    Call<DataBean> user(@QueryMap Map<String, String> map);

    /**
     * 使用call回调 path中不能有/ 使用下面的url
     * 使用@Path可以使用泛型
     * @param params user下的路径，动态路径
     * @param map 查询参数
     */
    @FormUrlEncoded
    @POST("user/{params}")
    Call<DataBean> user(@Path("params") String params, @FieldMap Map<String, String> map);

    /**
     * 使用call回调 使用@Url不能使用泛型
     * @param url user下的路径，动态路径
     * @param map 查询参数
     */
    @FormUrlEncoded
    @POST
    Call<DataBean> request(@Url String url, @FieldMap Map<String, String> map);

    /**
     * 使用Observable回调
     */
    @POST("user/{params}")
    Observable<DataBean> user2(@Path ("params") String params, @FieldMap Map<String, String> map);
}
