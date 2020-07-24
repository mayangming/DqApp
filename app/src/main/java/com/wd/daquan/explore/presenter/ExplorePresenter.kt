package com.wd.daquan.explore.presenter

import com.wd.daquan.common.presenter.BasePresenter
import com.wd.daquan.common.presenter.Presenter.IView
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.FindUserDynamicDescBean
import com.wd.daquan.model.interfaces.DqCallBack
import com.wd.daquan.model.retrofit.RetrofitHelp

/**
 * 数据解析层
 */
class ExplorePresenter : BasePresenter<IView<DataBean<Any>>>(){
    /**
     * searchUserId: 查询的用户Id
     * searchType:  0：所有好友 1：个人
     * pageNum: 当前第几页 默认1
     * pageSize: 每页多少条 默认10
     */
    fun getNewDynamicMessage(url :String, hashMap :Map<String, String>){
        RetrofitHelp.getDynamicApi().findUserDynamicDesc(url, getRequestBody(hashMap)).enqueue(object :DqCallBack<DataBean<ArrayList<FindUserDynamicDescBean>>>(){
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
}