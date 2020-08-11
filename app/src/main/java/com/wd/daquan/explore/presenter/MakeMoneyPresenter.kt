package com.wd.daquan.explore.presenter

import com.wd.daquan.common.presenter.BasePresenter
import com.wd.daquan.common.presenter.Presenter
import com.wd.daquan.model.bean.*
import com.wd.daquan.model.interfaces.DqCallBack
import com.wd.daquan.model.retrofit.RetrofitHelp

/**
 * 赚钱页面的数据解析层
 */
class MakeMoneyPresenter: BasePresenter<Presenter.IView<DataBean<Any>>>() {

    /**
     * 获取任务列表
     */
    fun getTaskList(url :String, hashMap :Map<String, Any>){
        RetrofitHelp.getTaskApi().findTaskList(url, getRequestBodyByObject(hashMap)).enqueue(object : DqCallBack<DataBean<List<MakeMoneyTaskBean>>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<List<MakeMoneyTaskBean>>) {

//                        hideLoading();
                success(url, code, entity)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<List<MakeMoneyTaskBean>>) {

//                        hideLoading();
                failed(url, code, entity)
            }
        })
    }

    /**
     * 获取我的任务列表
     */
    fun getMyTaskList(url :String, hashMap :Map<String, String>){
        RetrofitHelp.getTaskApi().findTaskMine(url, getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<List<MakeMoneyTaskMineBean>>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<List<MakeMoneyTaskMineBean>>) {

//                        hideLoading();
                success(url, code, entity)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<List<MakeMoneyTaskMineBean>>) {

//                        hideLoading();
                failed(url, code, entity)
            }
        })
    }

    /**
     * 获取任务详情
     */
    fun getTaskDetails(url :String, hashMap :Map<String, String>){
        RetrofitHelp.getTaskApi().findTaskDetails(url, getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<TaskDetailsBean>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<TaskDetailsBean>) {

//                        hideLoading();
                success(url, code, entity)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<TaskDetailsBean>) {

//                        hideLoading();
                failed(url, code, entity)
            }
        })
    }

    /**
     * 获取任务类型
     */
    fun getTaskType(url :String, hashMap :Map<String, String>){
        RetrofitHelp.getTaskApi().findTaskType(url, getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<List<TaskTypeBean>>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<List<TaskTypeBean>>) {

//                        hideLoading();
                success(url, code, entity)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<List<TaskTypeBean>>) {

//                        hideLoading();
                failed(url, code, entity)
            }
        })
    }

    /**
     * 获取任务类型
     */
    fun getTaskClassificationBean(url :String, hashMap :Map<String, String>){
        RetrofitHelp.getTaskApi().findTaskClassification(url, getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<List<TaskClassificationBean>>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<List<TaskClassificationBean>>) {

//                        hideLoading();
                success(url, code, entity)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<List<TaskClassificationBean>>) {

//                        hideLoading();
                failed(url, code, entity)
            }
        })
    }


    /**
     * 任务报名
     */
    fun getTaskRegistration(url :String, hashMap :Map<String, String>){
        RetrofitHelp.getTaskApi().findTaskRegistration(url, getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<TaskDetailsBean>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<TaskDetailsBean>) {

//                        hideLoading();
                success(url, code, entity)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<TaskDetailsBean>) {

//                        hideLoading();
                failed(url, code, entity)
            }
        })
    }
    /**
     * 提交验证
     */
    fun getTaskRefresh(url :String, hashMap :Map<String, String>){
        RetrofitHelp.getTaskApi().findTaskRefresh(url, getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<TaskDetailsBean>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<TaskDetailsBean>) {

//                        hideLoading();
                success(url, code, entity)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<TaskDetailsBean>) {

//                        hideLoading();
                failed(url, code, entity)
            }
        })
    }



}