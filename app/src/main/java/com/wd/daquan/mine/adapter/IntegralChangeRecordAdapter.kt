package com.wd.daquan.mine.adapter

import android.view.ViewGroup
import com.da.library.tools.DateUtil
import com.wd.daquan.R
import com.wd.daquan.imui.adapter.RecycleBaseAdapter
import com.wd.daquan.mine.viewholder.IntegralExchangeRecordHolder
import com.wd.daquan.model.bean.DqChangeHistoryEntity

/**
 * 积分兑换记录的适配器
 */
class IntegralChangeRecordAdapter() : RecycleBaseAdapter<IntegralExchangeRecordHolder>() {
    private var exchangeListener : ((DqChangeHistoryEntity) -> Unit)? = null
    var dqMoneyHistoryEntityList = arrayListOf<DqChangeHistoryEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    constructor(integralExchangeList :ArrayList<DqChangeHistoryEntity>) :this(){
        this.dqMoneyHistoryEntityList = integralExchangeList
    }

    fun addData(integralExchangeList :List<DqChangeHistoryEntity>){
        dqMoneyHistoryEntityList.addAll(integralExchangeList)
        notifyDataSetChanged()
    }


    override fun getItemCount() = dqMoneyHistoryEntityList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntegralExchangeRecordHolder {
        super.onCreateViewHolder(parent, viewType)
        val view = inflater.inflate(R.layout.item_integral_record,parent,false)
        return IntegralExchangeRecordHolder(view)
    }

    override fun onBindViewHolder(holder: IntegralExchangeRecordHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val entity = dqMoneyHistoryEntityList[position]
        holder.integralRecordName.text = entity.commoditiesNmae
        holder.integralRecordTime.text = "到账时间:${DateUtil.timeToString(entity.changeTime, DateUtil.yyyy_MM_dd_HH_mm_ss)}"
        holder.integralRecordCount.text = "-${entity.commoditiesPrice}"
    }

}