package com.wd.daquan.explore.activity

import com.wd.daquan.R
import com.wd.daquan.common.activity.DqBaseActivity
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.explore.presenter.SendTaskPresenter
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.SendTaskBean
import com.wd.daquan.model.log.DqToast
import kotlinx.android.synthetic.main.activity_refund_details.*
import kotlinx.android.synthetic.main.activity_send_task.*
import java.math.BigDecimal

/**
 * 退款详情
 */
class RefundDetailsActivity : DqBaseActivity<SendTaskPresenter, DataBean<Any>>(){
    var taskId = ""
    private var sendTaskBean = SendTaskBean()//发布任务后的信息
    companion object{
        const val KEY_TASK_ID = "keyTaskID"
    }

    override fun createPresenter() = SendTaskPresenter()

    override fun setContentView() {
        setContentView(R.layout.activity_refund_details)
    }

    override fun initView() {
        initTitle()
    }

    override fun initData() {
        taskId = intent.getStringExtra(KEY_TASK_ID)
        refund_task_id.text = "任务ID: $taskId"
        refundMoney()
        getUserTaskByTaskId()
    }


    private fun initTitle(){
        refund_details_title.title = "退款详情"
        refund_details_title.leftIv.setOnClickListener { finish() }
    }

    /**
     * 显示退款的金钱
     */
    private fun showRefundAmount(amount :String){
        val taskmoneyBigDecimal = amount.toBigDecimal()
        val division = 100.toBigDecimal()
        val price = taskmoneyBigDecimal.divide(division,2, BigDecimal.ROUND_UP).toPlainString()
        refund_amount_price.text = "￥$price"
    }

    /**
     * 更改退款状态
     */
    private fun changeRefundStatus(taskBean :SendTaskBean){

        when(taskBean.isPay){
            2 -> {
                refund_step_desc_3_tv.text = ""
                refund_step_3.setBackgroundResource(R.drawable.hollow_f74d1b)
            }

            3 -> {
                refund_step_desc_3_tv.text = "审核成功"
                refund_step_3.setBackgroundResource(R.drawable.solid_f74d1b)
            }

            4 -> {
                refund_step_desc_3_tv.text = "审核拒绝"
                refund_step_3.setBackgroundResource(R.drawable.solid_f74d1b)
            }

            5 -> {
                refund_step_desc_3_tv.text = "审核失败"
                refund_step_3.setBackgroundResource(R.drawable.solid_f74d1b)
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
     * 根据任务ID获取任务详情
     */
    private fun getUserTaskByTaskId(){
        val params = hashMapOf<String,String>()
        params["taskId"] = taskId
        if (null == mPresenter) return
        mPresenter.getCreateTask(DqUrl.url_task_userTaskSelect,params)
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onSuccess(url, code, entity)
        when(url){
            DqUrl.url_task_refundMoney -> {
                val amount = entity?.data.toString()
                showRefundAmount(amount)
            }
            DqUrl.url_task_userTaskSelect -> {
                sendTaskBean = entity?.data as SendTaskBean
                changeRefundStatus(sendTaskBean)
            }
        }
    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onFailed(url, code, entity)
        DqToast.showCenterShort(entity?.content)
    }

}