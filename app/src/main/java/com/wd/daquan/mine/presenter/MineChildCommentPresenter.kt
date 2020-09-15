package com.wd.daquan.mine.presenter

import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.DqGoodDetails
import com.wd.daquan.model.bean.DqIntviteRewardEntity
import com.wd.daquan.model.interfaces.DqCallBack
import com.wd.daquan.model.retrofit.RetrofitHelp

/**
 * 我的页面通用解析接口
 */
class MineChildCommentPresenter : MinePresenter<VipIView<DataBean<Any>>>() {
    /**
     * 获取邀请会员的奖励
     * @param url
     * @param hashMap
     */
    fun getIntviteReward(url: String, hashMap: Map<String, String>) {
        RetrofitHelp.getUserApi().getIntviteReward(url, getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<DqIntviteRewardEntity>>() {
            override fun onSuccess(url: String, code: Int, entity: DataBean<DqIntviteRewardEntity>) {
                success(url, code, entity)
            }

            override fun onFailed(url: String, code: Int, entity: DataBean<DqIntviteRewardEntity>) {
                failed(url, code, entity)
            }
        })
    }
}