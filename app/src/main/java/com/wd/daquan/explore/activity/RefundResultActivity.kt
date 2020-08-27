package com.wd.daquan.explore.activity

import android.view.View
import com.wd.daquan.R
import com.wd.daquan.common.activity.DqBaseActivity
import com.wd.daquan.explore.presenter.SendTaskPresenter
import com.wd.daquan.model.bean.DataBean
import kotlinx.android.synthetic.main.activity_pay_result.*

/**
 * 支付结果页
 */
class RefundResultActivity : DqBaseActivity<SendTaskPresenter, DataBean<Any>>(){
    override fun createPresenter() = SendTaskPresenter()

    override fun setContentView() {
        setContentView(R.layout.activity_pay_result)
    }

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() {
        super.initListener()
        pay_success_sure.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v){
            pay_success_sure -> {
                finish()
            }
        }
    }
}