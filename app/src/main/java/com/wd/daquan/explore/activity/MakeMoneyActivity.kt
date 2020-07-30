package com.wd.daquan.explore.activity

import com.wd.daquan.common.activity.DqBaseActivity
import com.wd.daquan.explore.presenter.MakeMoneyPresenter
import com.wd.daquan.model.bean.DataBean

/**
 * 赚钱页面
 */
class MakeMoneyActivity: DqBaseActivity<MakeMoneyPresenter, DataBean<Any>>(){
    override fun createPresenter() = MakeMoneyPresenter()

    override fun setContentView() {
    }

    override fun initView() {
    }

    override fun initData() {

    }
}