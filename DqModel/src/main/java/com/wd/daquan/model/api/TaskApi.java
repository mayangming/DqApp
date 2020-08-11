package com.wd.daquan.model.api;

import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.MakeMoneyTaskBean;
import com.wd.daquan.model.bean.MakeMoneyTaskMineBean;
import com.wd.daquan.model.bean.TaskClassificationBean;
import com.wd.daquan.model.bean.TaskDetailsBean;
import com.wd.daquan.model.bean.TaskTypeBean;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 任务接口
 */
public interface TaskApi {
    /**
     * 获取任务列表
     */
    @POST
    Call<DataBean<List<MakeMoneyTaskBean>>> findTaskList(@Url String url, @Body RequestBody requestBody);
    /**
     * 获取任务中我的模块
     */
    @POST
    Call<DataBean<List<MakeMoneyTaskMineBean>>> findTaskMine(@Url String url, @Body RequestBody requestBody);
    /**
     * 任务报名
     */
    @POST
    Call<DataBean<TaskDetailsBean>> findTaskRegistration(@Url String url, @Body RequestBody requestBody);

    /**
     * 任务报名
     */
    @POST
    Call<DataBean<TaskDetailsBean>> findTaskRefresh(@Url String url, @Body RequestBody requestBody);
    /**
     * 获取任务详情
     */
    @POST
    Call<DataBean<TaskDetailsBean>> findTaskDetails(@Url String url, @Body RequestBody requestBody);

    /**
     * 获取任务厂商分类
     */
    @POST
    Call<DataBean<List<TaskTypeBean>>> findTaskType(@Url String url, @Body RequestBody requestBody);


    /**
     * 获取任务类型
     */
    @POST
    Call<DataBean<List<TaskClassificationBean>>> findTaskClassification(@Url String url, @Body RequestBody requestBody);
//    /**
//     * 获取任务详情
//     */
//    @POST
//    Call<DataBean<List<MakeMoneyTaskBean>>> findTaskDetail(@Url String url, @Body RequestBody requestBody);
//    /**
//     * 获取友圈配置内容
//     */
//    @POST
//    Call<DataBean<List<MakeMoneyTaskBean>>> findTaskList(@Url String url, @Body RequestBody requestBody);
//    /**
//     * 获取友圈配置内容
//     */
//    @POST
//    Call<DataBean<List<MakeMoneyTaskBean>>> findTaskList(@Url String url, @Body RequestBody requestBody);
}