package com.wd.daquan.mine.activity

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.netease.nim.uikit.common.ui.recyclerview.decoration.SpacingDecoration
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.wd.daquan.R
import com.wd.daquan.common.activity.DqBaseActivity
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.mine.adapter.IntegralChangeRecordAdapter
import com.wd.daquan.mine.adapter.IntegralExchangeRecordAdapter
import com.wd.daquan.mine.presenter.IntegralMallPresenter
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.DqChangeHistoryEntity
import kotlinx.android.synthetic.main.activity_integral_exchange.*
import kotlinx.android.synthetic.main.activity_unread.*
import java.util.ArrayList

/**
 * 积分兑换记录
 */
class IntegralExchangeRecordActivity: DqBaseActivity<IntegralMallPresenter, DataBean<Any>>() {

    private var recordAdapter = IntegralChangeRecordAdapter()
    private var pagenum = 1
    private var pagesize = 10
    private var dqChangeHistoryList = arrayListOf<DqChangeHistoryEntity>()
    override fun createPresenter() = IntegralMallPresenter()

    override fun setContentView() {
        setContentView(R.layout.activity_integral_exchange)
    }

    override fun initView() {
        initTitle()
        initRecycleView()
    }

    override fun initData() {
        getChangeHistory()
    }

    override fun initListener() {
        super.initListener()
        initRefreshAndLoadListener()
    }

    private fun initTitle(){
        integral_exchange_title.title = "兑换记录"
        integral_exchange_title.leftIv.setOnClickListener {
            finish()
        }
    }

    /**
     * 初始化列表
     */
    private fun initRecycleView(){
        integral_exchange_mall_brl.apply {
            layoutManager = LinearLayoutManager(this@IntegralExchangeRecordActivity, RecyclerView.VERTICAL,false)
            adapter = recordAdapter
            emptyView = layoutInflater.inflate(R.layout.layout_empty_view,exchange_root as ViewGroup,false)
        }
        integral_exchange_mall_brl.addItemDecoration(SpacingDecoration(30,30,false))
    }

    private fun initRefreshAndLoadListener(){
        integral_exchange_mall_srl.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener{
            override fun onRefresh(refreshLayout: RefreshLayout) {
                pagenum = 1
                getChangeHistory()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                getChangeHistory()
            }
        })
    }
    private fun updateUI(){
        if(pagenum == 1){
            recordAdapter.dqMoneyHistoryEntityList = dqChangeHistoryList
        }else{
            recordAdapter.addData(dqChangeHistoryList)
        }

    }
    /**
     * 兑换记录接口
     */
    private fun getChangeHistory(){
        val params = hashMapOf<String,String>()
        params["pagenum"] = pagenum.toString()
        params["pagesize"] = pagesize.toString()
        mPresenter.getChangeHistory(DqUrl.url_dbsign_getChangeHistory,params)
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onSuccess(url, code, entity)
        if (DqUrl.url_dbsign_getChangeHistory == url){
            dqChangeHistoryList = entity?.data as ArrayList<DqChangeHistoryEntity>
            updateUI()
            pagenum++
            integral_exchange_mall_srl.closeHeaderOrFooter()
        }
    }

}