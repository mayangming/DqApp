package com.wd.daquan.explore.activity

import android.view.View
import com.wd.daquan.R
import com.wd.daquan.common.activity.DqBaseActivity
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.explore.presenter.SendTaskPresenter
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.log.DqToast
import com.wd.daquan.model.rxbus.MsgMgr
import com.wd.daquan.model.rxbus.MsgType
import kotlinx.android.synthetic.main.activity_apply_refund.*
import java.math.BigDecimal

/**
 * 申请退款
 */
class ApplyRefundActivity: DqBaseActivity<SendTaskPresenter, DataBean<Any>>() {
    var taskId = ""
    companion object{
        const val KEY_TASK_ID = "keyTaskId"
    }

    override fun createPresenter() = SendTaskPresenter()

    override fun setContentView() {
        setContentView(R.layout.activity_apply_refund)
    }

    override fun initView() {
        initTitle()
    }

    override fun initListener() {
        super.initListener()
        submit_refund.setOnClickListener(this)
    }

    override fun initData() {
        taskId = intent.getStringExtra(KEY_TASK_ID)
        apply_refund_task_id.text = "任务ID: $taskId"
        refundMoney()
    }

    private fun initTitle(){
        apply_refund_title.title = "申请退款"
        apply_refund_title.leftIv.setOnClickListener { finish() }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v){
            submit_refund -> {
                drawback()
            }
        }
    }

    /**
     * 获取退款金额
     */
    private fun refundMoney(){
        val params = hashMapOf<String,String>()
        params["taskId"] = taskId
        mPresenter.refundMoney(DqUrl.url_task_refundMoney,params)
    }

    /**
     * 退款
     */
    private fun drawback(){
        val params = hashMapOf<String,String>()
        params["taskId"] = taskId
        mPresenter.drawback(DqUrl.url_task_drawback,params)
    }

    private fun updateUi(amount :String){
        val taskmoneyBigDecimal = amount.toBigDecimal()
        val division = 100.toBigDecimal()
        val price = taskmoneyBigDecimal.divide(division,2, BigDecimal.ROUND_UP).toPlainString()
        apply_refund_task_amount.text = "￥$price"
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>) {
        super.onSuccess(url, code, entity)
        when(url){
            DqUrl.url_task_refundMoney -> {//获取退款信息
                val data = entity.data.toString()
                updateUi(data)
            }
            DqUrl.url_task_drawback -> {//退款
                //创建任务后的事件监听
                MsgMgr.getInstance().sendMsg(MsgType.TASK_REFUND, "")
                NavUtils.gotoRefundDetailsActivity(this,taskId)
                finish()
            }
        }
    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onFailed(url, code, entity)
        DqToast.showCenterShort(entity?.content)
    }

}