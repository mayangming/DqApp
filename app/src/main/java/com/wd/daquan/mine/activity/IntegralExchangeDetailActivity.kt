package com.wd.daquan.mine.activity

import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.netease.nim.uikit.common.ui.recyclerview.decoration.SpacingDecoration
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.wd.daquan.R
import com.wd.daquan.common.activity.DqBaseActivity
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.mine.adapter.IntegralExchangeRecordAdapter
import com.wd.daquan.mine.presenter.IntegralMallPresenter
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.DqMoneyDetailEntity
import com.wd.daquan.model.bean.DqMoneyHistoryEntity
import com.wd.daquan.model.log.DqLog
import com.wd.daquan.model.log.DqToast
import kotlinx.android.synthetic.main.activity_integral_details.*
import kotlinx.android.synthetic.main.activity_integral_exchange.*
import kotlinx.android.synthetic.main.activity_unread.*

/**
 * 斗币明细记录
 */
class IntegralExchangeDetailActivity: DqBaseActivity<IntegralMallPresenter, DataBean<Any>>() {
    private var recordAdapter = IntegralExchangeRecordAdapter()
    private var popWnd: PopupWindow ?= null
    private var timeLabel1 :View ?= null
    private var timeLabel2 :View ?= null
    private var timeLabel3 :View ?= null
    private var timeLabel4 :View ?= null
    private var recordType = 0 //0: 收入 1:支出
    private var money = 0//斗币数量
    private var dqMoneyHistory = arrayListOf<DqMoneyHistoryEntity>()
    private var dqMoneyDetail = DqMoneyDetailEntity()
    private var pageNum = 1
    private var pagesize = 10
    private var day = 30
    companion object{
        const val KEY_MONEY = "keyMoney"
    }

    override fun createPresenter() = IntegralMallPresenter()
    override fun setContentView() {
        setContentView(R.layout.activity_integral_details)
    }

    override fun initView() {
        initTitle()
        initRecycleView()
        initMenuPop()
    }

    override fun initData() {
        money = intent.getIntExtra(KEY_MONEY,0)
        dq_money.text = money.toString()
        getMoneyHistory(30)
    }

    override fun initListener() {
        super.initListener()
        time_chose.setOnClickListener(this)
        integral_income.setOnClickListener(this)
        integral_expenditure.setOnClickListener(this)
        initRefreshAndLoadListener()
    }

    private fun initTitle(){
        integral_details_title.title = "积分明细"
        integral_details_title.leftIv.setOnClickListener {
            finish()
        }
    }

    /**
     * 初始化列表
     */
    private fun initRecycleView(){
        integral_detail_brl.apply {
            layoutManager = LinearLayoutManager(this@IntegralExchangeDetailActivity, RecyclerView.VERTICAL, false)
            adapter = recordAdapter
            emptyView = layoutInflater.inflate(R.layout.layout_empty_view,detail_root as ViewGroup, false)
        }
        integral_detail_brl.addItemDecoration(SpacingDecoration(30, 15, false))
    }

    private fun initRefreshAndLoadListener(){
        integral_detail_srl.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                pageNum = 1
                getMoneyHistory(30)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                getMoneyHistory(day)
            }
        })
    }

    private fun initMenuPop(){
        val contentView: View = LayoutInflater.from(this).inflate(R.layout.pop_intefral_time, null)
        timeLabel1 = contentView.findViewById(R.id.integral_time_label1)
        timeLabel2 = contentView.findViewById(R.id.integral_time_label2)
        timeLabel3 = contentView.findViewById(R.id.integral_time_label3)
        timeLabel4 = contentView.findViewById(R.id.integral_time_label4)
        timeLabel1?.setOnClickListener(this)
        timeLabel2?.setOnClickListener(this)
        timeLabel3?.setOnClickListener(this)
        timeLabel4?.setOnClickListener(this)
        popWnd = PopupWindow(this)
        popWnd?.contentView = contentView
        popWnd?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        popWnd?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        // 设置PopupWindow是否能响应外部点击事件
        popWnd?.isOutsideTouchable = true
        // 设置PopupWindow是否能响应点击事件
        popWnd?.isTouchable = true
        popWnd?.setBackgroundDrawable(BitmapDrawable())
    }

    private fun showMenuPop(){
        popWnd?.showAsDropDown(time_chose,100,0)
    }

    private fun updateUI(){
        current_day_count.text = dqMoneyDetail.totalMoney.toString()
        if (pageNum == 1){
            recordAdapter.dqMoneyHistoryEntityList = dqMoneyHistory
        }else{
            recordAdapter.addData(dqMoneyHistory)
        }
    }

    private fun getMoneyHistory(day :Int){
        val params = hashMapOf<String,String>()
        params["tradeStatus"] = recordType.toString()
        params["day"] = day.toString()
        params["pageNum"] = pageNum.toString()
        params["pagesize"] = pagesize.toString()
        mPresenter.getMoneyHistory(DqUrl.url_dbsign_getMoneyHistory,params)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v){
            time_chose -> {
                popWnd?.let {
                    if (it.isShowing){
                        it.dismiss()
                    }else{
                        showMenuPop()
                    }
                }
            }
            integral_income -> {
                if(recordType == 0){
                    return
                }
                recordType = 0
                pageNum = 1
                label1.setTextColor(resources.getColor(R.color.color_EF5B40))
                label2.setTextColor(resources.getColor(R.color.color_ff999999))
                label1_divider.visibility = View.VISIBLE
                label2_divider.visibility = View.GONE
                getMoneyHistory(30)
                updateTitle()
            }
            integral_expenditure -> {
                if(recordType == 1){
                    return
                }
                recordType = 1
                pageNum = 1
                label1.setTextColor(resources.getColor(R.color.color_ff999999))
                label2.setTextColor(resources.getColor(R.color.color_EF5B40))
                label1_divider.visibility = View.GONE
                label2_divider.visibility = View.VISIBLE
                getMoneyHistory(30)
                updateTitle()
            }
            timeLabel1 -> {
                pageNum = 1
                day = 30
                time_content.text = "本月"
                getMoneyHistory(30)
                popWnd?.dismiss()
                updateTitle()
            }
            timeLabel2 -> {
                pageNum = 1
                day = 90
                time_content.text = "三个月"
                getMoneyHistory(90)
                popWnd?.dismiss()
                updateTitle()
            }
            timeLabel3 -> {
                pageNum = 1
                day = 180
                time_content.text = "六个月"
                getMoneyHistory(180)
                popWnd?.dismiss()
                updateTitle()
            }
            timeLabel4 -> {
                pageNum = 1
                day = 3650
                time_content.text = "全部"
                getMoneyHistory(3650)
                popWnd?.dismiss()
                updateTitle()
            }
        }
    }

    private fun updateTitle(){
        var content = "最近"
        when(day){
            30 -> {
                content = content.plus("一个月")
            }
            60 -> {
                content = content.plus("二个月")
            }
            90 -> {
                content = content.plus("三个月")
            }
            3650 -> {
                content = "全部"
            }
        }
        content = content.plus("积分")
        when(recordType){
            0 -> {
                content = content.plus("收入")
            }
            1 -> {
                content = content.plus("支出")
            }
        }
        current_day_content.text = content
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onSuccess(url, code, entity)
        if (DqUrl.url_dbsign_getMoneyHistory == url){
            dqMoneyDetail = entity?.data as DqMoneyDetailEntity
            dqMoneyHistory = dqMoneyDetail.list
            updateUI()
            pageNum++
            integral_detail_srl.closeHeaderOrFooter()
        }
    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onFailed(url, code, entity)
        integral_detail_srl.closeHeaderOrFooter()
    }
}