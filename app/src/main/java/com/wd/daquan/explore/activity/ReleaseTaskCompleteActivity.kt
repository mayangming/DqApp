package com.wd.daquan.explore.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.da.library.tools.DateUtil
import com.meetqs.qingchat.pickerview.view.OptionsPickerView
import com.netease.nim.uikit.common.ui.recyclerview.decoration.SpacingDecoration
import com.wd.daquan.R
import com.wd.daquan.common.activity.DqBaseActivity
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.common.utils.DialogUtils
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.explore.adapter.TaskCompleteAdapter
import com.wd.daquan.explore.presenter.SendTaskPresenter
import com.wd.daquan.mine.listener.PayDetailListener
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.ReleaseCompleteDetailsBean
import com.wd.daquan.model.bean.SendTaskBean
import com.wd.daquan.model.log.DqLog
import com.wd.daquan.model.log.DqToast
import com.wd.daquan.model.rxbus.MsgMgr
import com.wd.daquan.model.rxbus.MsgType
import com.wd.daquan.model.rxbus.QCObserver
import kotlinx.android.synthetic.main.activity_release_task_details.*
import kotlinx.android.synthetic.main.activity_unread.*
import kotlinx.android.synthetic.main.fragment_make_money_task_mine.*
import java.util.*

/**
 * 发布任务完成详情页
 */
class ReleaseTaskCompleteActivity : DqBaseActivity<SendTaskPresenter, DataBean<Any>>(), PayDetailListener, QCObserver {
    private var taskBean = SendTaskBean()
    private var taskCompleteAdapter = TaskCompleteAdapter()
    private var completeDetailsBean = ReleaseCompleteDetailsBean()
    private var mPvOptions: OptionsPickerView<*>? = null
    var taskId = ""

    companion object{
        const val KEY_TASK_ID = "keyTaskID"
    }

    override fun createPresenter() = SendTaskPresenter()

    override fun setContentView() {
        MsgMgr.getInstance().attach(this)
        setContentView(R.layout.activity_release_task_details)
    }

    override fun initView() {
        initTitle()
        initRecycleView()
    }

    override fun initData() {
        taskId = intent.getStringExtra(KEY_TASK_ID)
        val params = hashMapOf<String,String>()
        params["taskId"] = taskId
        mPresenter.checkTask(DqUrl.url_task_checkTask,params)
        initDateDialog()
    }

    override fun initListener() {
        super.initListener()
        apply_refund.setOnClickListener(this)
        apply_extension.setOnClickListener(this)
        refund_details.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v){
            apply_refund -> {
                NavUtils.gotoApplyRefundActivity(this,taskId)
            }
            apply_extension -> {
                mPvOptions?.show()
            }
            refund_details -> {
                NavUtils.gotoRefundDetailsActivity(this,taskId)
            }
        }
    }

    private fun initTitle(){
        task_send_details_title.title = "任务详情"
        task_send_details_title.leftIv.setOnClickListener { finish() }
    }

    private fun initRecycleView(){
        val linearManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        task_complete_list.layoutManager = linearManager
        task_complete_list.emptyView = layoutInflater.inflate(R.layout.layout_empty_view,friend_unread_refreshLayout,false)
        task_complete_list.addItemDecoration(SpacingDecoration(30,20,false))
        task_complete_list.adapter = taskCompleteAdapter
    }
    private fun initDateDialog(){
        val now = Calendar.getInstance()
        val years = now[Calendar.YEAR]
        val date = now[Calendar.MONTH] + 1
        val listYear = DateUtil.getWheelYearLayer(this, now)
        val listMonth = DateUtil.getWheelMonth(this)
        val listDate = DateUtil.getWheelDate(this)
        mPvOptions = DialogUtils.showYearToDate(this, listYear, listMonth
                , listDate, date, this)
    }

    /**
     * 更改任务内容
     */
    private fun changeTask(extime :String){
        val params = hashMapOf<String,String>()
        params["taskId"] = taskBean.id
        params["extime"] = extime
        mPresenter.changeTime(DqUrl.url_task_changeTime,params)
    }

    /**
     * 更新UI
     */
    private fun updateUI(completeDetailsBean :ReleaseCompleteDetailsBean){
        taskBean = completeDetailsBean.task
        task_complete_id.text = "任务ID：${taskBean.id}"
        task_complete_end_time.text = "${DateUtil.timeToString(taskBean.extime, DateUtil.yyyy_MM_dd)}"
        task_complete_name.text = taskBean.taskname
        task_complete_progress.text = completeDetailsBean.total.toString()
        task_complete_total.text = "/${taskBean.classnum}"

        if(taskBean.expires == 1){
            apply_refund.visibility = View.VISIBLE
            apply_extension.visibility = View.VISIBLE
        }else{
            apply_refund.visibility = View.GONE
            apply_extension.visibility = View.GONE
        }

        if (apply_refund.visibility == View.GONE && apply_extension.visibility == View.GONE &&  refund_details.visibility == View.GONE){
            task_details_label_container.visibility = View.GONE
        }else{
            task_details_label_container.visibility = View.VISIBLE
        }

        if(taskBean.isPay == 2 || taskBean.isPay == 3 || taskBean.isPay == 4 || taskBean.isPay == 5){
            refund_details.visibility = View.VISIBLE
            apply_refund.visibility = View.GONE
            apply_extension.visibility = View.GONE
        }else{
            refund_details.visibility = View.GONE
        }

    }

    override fun onMessage(key: String?, value: Any?) {
        when(key){
            MsgType.TASK_REFUND -> {//退款后结束该页面
                finish()
            }
        }
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onSuccess(url, code, entity)
//        if (null == entity){
//            return
//        }
        when(url){
            DqUrl.url_task_checkTask -> {
                completeDetailsBean = entity?.data as ReleaseCompleteDetailsBean
                taskCompleteAdapter.taskCompleteBeans = completeDetailsBean.list
                updateUI(completeDetailsBean)
            }
            DqUrl.url_task_changeTime -> {
                DqToast.showCenterShort("延期成功!")
                apply_extension.visibility = View.GONE
            }
        }
    }

    override fun payDetailClick(mYear: String?, mMonth: String?, date: String?) {
        val endTime = "$mYear-$mMonth-$date"
        val time = com.da.library.utils.DateUtil.getStringToCalendar(endTime, com.da.library.utils.DateUtil.YMD).timeInMillis
        if (time <= taskBean.extime){
            DqToast.showCenterShort("延期时间不能小于当前的截止时间!")
            return
        }
        val calender = Calendar.getInstance(Locale.CHINESE)
        calender.add(Calendar.DATE,1);//把日期往后增加两天.整数往后推,负数往前移动
        calender.set(Calendar.HOUR_OF_DAY, 0)//时
        calender.set(Calendar.MINUTE, 0)//分
        calender.set(Calendar.SECOND, 0)//秒
        val timeAfter2 = calender.timeInMillis
        if (time <= timeAfter2){
            DqToast.showCenterShort("延期时间不能小于两天后的时间!")
            return
        }
        task_complete_end_time.text = endTime
        changeTask(time.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        MsgMgr.getInstance().detach(this)
    }

}