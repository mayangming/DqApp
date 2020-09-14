package com.wd.daquan.mine.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.netease.nim.uikit.common.ui.recyclerview.decoration.SpacingDecoration
import com.wd.daquan.R
import com.wd.daquan.common.activity.DqBaseActivity
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.mine.adapter.IntegralMallGoodsAdapter
import com.wd.daquan.mine.dialog.ExchangeBottomDialog
import com.wd.daquan.mine.presenter.IntegralMallPresenter
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.DqGoodChangeEntity
import com.wd.daquan.model.bean.DqGoodDetails
import com.wd.daquan.model.bean.DqGoodsEntity
import com.wd.daquan.model.rxbus.MsgMgr
import com.wd.daquan.model.rxbus.MsgType
import kotlinx.android.synthetic.main.activity_integral_mall.*

/**
 * 积分商城
 */
class IntegralMallActivity : DqBaseActivity<IntegralMallPresenter, DataBean<Any>>() {
    private var exchangeBottomDialog = ExchangeBottomDialog()
    private var goodsAdapter = IntegralMallGoodsAdapter()
    private var dqGoodDetails = DqGoodDetails()
    override fun createPresenter() = IntegralMallPresenter()

    override fun setContentView() {
        setContentView(R.layout.activity_integral_mall)
    }

    override fun initView() {
        initTitle()
        initRecycleView()
    }

    override fun initData() {
        requestData()
    }

    override fun initListener() {
        super.initListener()
        integral_currency_record.setOnClickListener(this::onClick)
        integral_record.setOnClickListener(this)
    }
    private fun initTitle(){
        integral_mall_title.setTitleLayoutBackgroundColor(Color.TRANSPARENT)
        integral_mall_title.leftIv.setOnClickListener {
            finish()
        }
    }

    private fun initRecycleView() {
        integral_mall_brl.apply {
            layoutManager = GridLayoutManager(this@IntegralMallActivity, 2)
            adapter = goodsAdapter
            emptyView = layoutInflater.inflate(R.layout.layout_empty_view,integral_mall_root as ViewGroup,false)
        }
        integral_mall_brl.addItemDecoration(SpacingDecoration(30, 20, false))
        goodsAdapter.setOnExchangeListener {
            showExchangeBottomDialog(it)
        }
    }

    private fun requestData(){
        val params = hashMapOf<String, String>()
        mPresenter.userDBMoney(DqUrl.url_dbsign_userDBMoney, params)
    }

    private fun updateUI(entity: DqGoodDetails){
        mall_count.text = entity.dbMoney.toString()
        goodsAdapter.dqGoodDetails = entity.list
        val list = arrayListOf<DqGoodChangeEntity>()
        scroll_tv.list = entity.changeList
        scroll_tv.startScroll()
    }

    private fun showExchangeBottomDialog(entity: DqGoodsEntity){
        val bundle = Bundle()
        bundle.putSerializable(ExchangeBottomDialog.KEY_VALUE, entity)
        exchangeBottomDialog.arguments = bundle
        exchangeBottomDialog.show(supportFragmentManager, "兑换对话框")
        exchangeBottomDialog.setOnExchangeListener {
            requestData()
            MsgMgr.getInstance().sendMsg(MsgType.INTEGRAL_CHANGE, "")
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v){
            integral_currency_record -> {
                NavUtils.gotoIntegralExchangeDetailActivity(v?.context, dqGoodDetails.dbMoney)
            }
            integral_record -> {
                NavUtils.gotoIntegralExchangeRecordActivity(v?.context)
            }
        }
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onSuccess(url, code, entity)
        if (DqUrl.url_dbsign_userDBMoney == url){
            dqGoodDetails = entity?.data as DqGoodDetails
            updateUI(dqGoodDetails)
        }
    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onFailed(url, code, entity)
    }

}