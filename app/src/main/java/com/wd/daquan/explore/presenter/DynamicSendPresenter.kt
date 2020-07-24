package com.wd.daquan.explore.presenter

import com.wd.daquan.common.presenter.BasePresenter
import com.wd.daquan.common.presenter.Presenter.IView
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.FindUserDynamicDescBean
import com.wd.daquan.model.bean.SaveUserDynamicDescBean
import com.wd.daquan.model.interfaces.DqCallBack
import com.wd.daquan.model.retrofit.RetrofitHelp

/**
 * 数据解析层
 */
class DynamicSendPresenter : BasePresenter<IView<DataBean<Any>>>(){

    /**
     * 发布动态
     */
    fun saveUserDynamicDesc(url :String, hashMap :Map<String, Any>){
        RetrofitHelp.getDynamicApi().saveUserDynamicDesc(url, getRequestBodyByObject(hashMap)).enqueue(object : DqCallBack<DataBean<FindUserDynamicDescBean>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<FindUserDynamicDescBean>) {

//                        hideLoading();
                success(url, code, entity)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<FindUserDynamicDescBean>) {

//                        hideLoading();
                failed(url, code, entity)
            }
        })
    }

}