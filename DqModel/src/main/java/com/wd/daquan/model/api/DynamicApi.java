package com.wd.daquan.model.api;

import com.wd.daquan.model.bean.AreaUnReadBean;
import com.wd.daquan.model.bean.AreaUnReadSimpleBean;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.FindUserDynamicDescBean;
import com.wd.daquan.model.bean.SaveUserDynamicCommentBean;
import com.wd.daquan.model.bean.SaveUserDynamicDescBean;
import com.wd.daquan.model.bean.SaveUserDynamicLikeBean;
import com.wd.daquan.model.bean.UserDynamicBean;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 朋友圈接口
 */
public interface DynamicApi {
    /**
     * 获取友圈配置内容
     */
    @POST
    Call<DataBean<UserDynamicBean>> findUserDynamic(@Url String url, @Body RequestBody requestBody);

    /**
     * 更新友圈配置内容
     */
    @POST
    Call<DataBean<UserDynamicBean>> updateUserDynamic(@Url String url, @Body RequestBody requestBody);
    /**
     * 保存友圈配置内容
     */
    @POST
    Call<DataBean<FindUserDynamicDescBean>> saveUserDynamicDesc(@Url String url, @Body RequestBody requestBody);
    /**
     * 更新友圈配置内容
     */
    @POST
    Call<DataBean<SaveUserDynamicDescBean>> updateUserDynamicDesc(@Url String url, @Body RequestBody requestBody);
    /**
     * 获取友圈列表
     */
    @POST
    Call<DataBean<ArrayList<FindUserDynamicDescBean>>> findUserDynamicDesc(@Url String url, @Body RequestBody requestBody);
    /**
     * 点赞朋友圈
     */
    @POST
    Call<DataBean<FindUserDynamicDescBean>> saveUserDynamicLike(@Url String url, @Body RequestBody requestBody);
    /**
     * 取消点赞朋友圈
     */
    @POST
    Call<DataBean<FindUserDynamicDescBean>> delUserDynamicLike(@Url String url, @Body RequestBody requestBody);

    /**
     * 评论朋友圈内容
     */
    @POST
    Call<DataBean<FindUserDynamicDescBean>> saveUserDynamicComment(@Url String url, @Body RequestBody requestBody);

    /**
     * 删除评论
     */
    @POST
    Call<DataBean<FindUserDynamicDescBean>> delUserDynamicComment(@Url String url, @Body RequestBody requestBody);

    /**
     * 删除朋友圈内容
     */
    @POST
    Call<DataBean<FindUserDynamicDescBean>> delUserDynamic(@Url String url, @Body RequestBody requestBody);

    /**
     * 获取指定数量的朋友圈图片
     */
    @POST
    Call<DataBean<List<String>>> findUserDynamicPic(@Url String url, @Body RequestBody requestBody);

    /**
     * 获取指定数量的朋友圈图片
     */
    @POST
    Call<DataBean<ArrayList<AreaUnReadBean>>> findUserDynamicMsg(@Url String url, @Body RequestBody requestBody);


    /**
     * 获取指定数量的朋友圈图片
     */
    @POST
    Call<DataBean<AreaUnReadSimpleBean>> findUserDynamicMsgSum(@Url String url, @Body RequestBody requestBody);

    /**
     * 清空朋友圈未读消息
     */
    @POST
    Call<DataBean> delUserDynamicMsg(@Url String url, @Body RequestBody requestBody);
}