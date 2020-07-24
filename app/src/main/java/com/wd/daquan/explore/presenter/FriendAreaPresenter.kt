package com.wd.daquan.explore.presenter

import com.wd.daquan.common.presenter.BasePresenter
import com.wd.daquan.common.presenter.Presenter
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.FindUserDynamicDescBean
import com.wd.daquan.model.bean.UserDynamicBean
import com.wd.daquan.model.interfaces.DqCallBack
import com.wd.daquan.model.retrofit.RetrofitHelp

/**
 * 朋友圈页面
 */
class FriendAreaPresenter : BasePresenter<Presenter.IView<DataBean<Any>>>(){
    /**
     * searchUserId: 查询的用户Id
     * searchType:  0：所有好友 1：个人
     * pageNum: 当前第几页 默认1
     * pageSize: 每页多少条 默认10
     */
    fun getNewDynamicMessage(url :String, hashMap :Map<String, String>){
        RetrofitHelp.getDynamicApi().findUserDynamicDesc(url, getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<ArrayList<FindUserDynamicDescBean>>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<ArrayList<FindUserDynamicDescBean>>) {

//                        hideLoading();
                success(url, code, entity)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<ArrayList<FindUserDynamicDescBean>>) {

//                        hideLoading();
                failed(url, code, entity)
            }
        })
    }

    /**
     * searchUserId: 查询的用户Id
     * searchType:  0：所有好友 1：个人
     */
    fun findUserDynamic(url :String, hashMap :Map<String, String>){
        RetrofitHelp.getDynamicApi().findUserDynamic(url, getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<UserDynamicBean>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<UserDynamicBean>) {

//                        hideLoading();
                success(url, code, entity)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<UserDynamicBean>) {

//                        hideLoading();
                failed(url, code, entity)
            }
        })
    }

    /**
     * 更新朋友圈背景
     */
    fun updateUserDynamic(url :String, hashMap :Map<String, String>){
        RetrofitHelp.getDynamicApi().updateUserDynamic(url, getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<UserDynamicBean>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<UserDynamicBean>) {

//                        hideLoading();
                success(url, code, entity)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<UserDynamicBean>) {

//                        hideLoading();
                failed(url, code, entity)
            }
        })
    }
}