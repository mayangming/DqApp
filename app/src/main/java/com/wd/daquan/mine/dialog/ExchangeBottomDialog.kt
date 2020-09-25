package com.wd.daquan.mine.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wd.daquan.R
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.DqGoodDetails
import com.wd.daquan.model.bean.DqGoodsEntity
import com.wd.daquan.model.bean.IntegralMallEntity
import com.wd.daquan.model.interfaces.DqCallBack
import com.wd.daquan.model.log.DqToast
import com.wd.daquan.model.retrofit.RetrofitHelp
import kotlinx.android.synthetic.main.dialog_exchange.*

/**
 * 兑换页面底部输入框
 */
class ExchangeBottomDialog : BottomSheetDialogFragment(){
    var exchangeListener : (() -> Unit)? = null //兑换结果监听
    var entity = DqGoodsEntity()
    var count = 1
    companion object{
        const val KEY_VALUE = "keyValue"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_exchange, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initData()
    }

    private fun initData(){
        entity = arguments?.getSerializable(KEY_VALUE) as DqGoodsEntity
    }

    /**
     * 初始化监听
     */
    private fun initListener(){
        goods_count_reduce.setOnClickListener(this::onClick)
        goods_count_add.setOnClickListener(this::onClick)
        immediately_exchange.setOnClickListener(this::onClick)
    }

    fun setOnExchangeListener(exchangeListener :(() -> Unit)){
        this.exchangeListener = exchangeListener
    }

    fun onClick(v: View?) {
        when(v){
            goods_count_reduce -> {
                count--
                if (count < 1){
                    count = 1
                }
                goods_count.text = count.toString()
            }
            goods_count_add -> {
                count++
                if (count > 10){
                    count = 10
                }
                goods_count.text = count.toString()
            }
            immediately_exchange -> {
                val params = hashMapOf<String,String>()
                params["commoditiesId"] = entity.id.toString()
                params["commoditiesNum"] = count.toString()
                changeDBCommodities(DqUrl.url_dbsign_changeDBCommodities,params)
            }
        }
    }

    /**
     * 兑换商品
     * @param url
     * @param hashMap
     */
    private fun changeDBCommodities(url: String, hashMap: Map<String, String>) {
        RetrofitHelp.getUserApi().changeDBCommodities(url, RetrofitHelp.getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<Any>>() {
            override fun onSuccess(url: String, code: Int, entity: DataBean<Any>) {
                exchangeListener?.invoke()
                count = 1
                DqToast.showCenterShort("兑换成功~")
                dismiss()
            }

            override fun onFailed(url: String, code: Int, entity: DataBean<Any>) {
                DqToast.showCenterShort(entity.content)
            }
        })
    }

}