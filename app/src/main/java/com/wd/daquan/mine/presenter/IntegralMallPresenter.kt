package com.wd.daquan.mine.presenter

import com.wd.daquan.model.bean.*
import com.wd.daquan.model.interfaces.DqCallBack
import com.wd.daquan.model.retrofit.RetrofitHelp
import java.util.ArrayList

/**
 * 积分商城解析层
 */
class IntegralMallPresenter : MinePresenter<VipIView<DataBean<Any>>>(){

    /**
     * 商城页面
     * @param url
     * @param hashMap
     */
    fun userDBMoney(url: String, hashMap: Map<String, String>) {
        RetrofitHelp.getUserApi().userDBMoney(url, getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<DqGoodDetails>>() {
            override fun onSuccess(url: String, code: Int, entity: DataBean<DqGoodDetails>) {
                success(url, code, entity)
            }

            override fun onFailed(url: String, code: Int, entity: DataBean<DqGoodDetails>) {
                failed(url, code, entity)
            }
        })
    }


    /**
     * 斗币明细
     * @param url
     * @param hashMap
     * dbsign/getMoneyHistory
     */
    fun getMoneyHistory(url: String, hashMap: Map<String, String>) {
        RetrofitHelp.getUserApi().getMoneyHistory(url, getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<DqMoneyDetailEntity>>() {
            override fun onSuccess(url: String, code: Int, entity: DataBean<DqMoneyDetailEntity>) {
                success(url, code, entity)
            }

            override fun onFailed(url: String, code: Int, entity: DataBean<DqMoneyDetailEntity>) {
                failed(url, code, entity)
            }
        })
    }
    /**
     * 兑换记录
     * @param url
     * @param hashMap
     * dbsign/getMoneyHistory
     */
    fun getChangeHistory(url: String, hashMap: Map<String, String>) {
        RetrofitHelp.getUserApi().getChangeHistory(url, getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<ArrayList<DqChangeHistoryEntity>>>() {
            override fun onSuccess(url: String, code: Int, entity: DataBean<ArrayList<DqChangeHistoryEntity>>) {
                success(url, code, entity)
            }

            override fun onFailed(url: String, code: Int, entity: DataBean<ArrayList<DqChangeHistoryEntity>>) {
                failed(url, code, entity)
            }
        })
    }
}

