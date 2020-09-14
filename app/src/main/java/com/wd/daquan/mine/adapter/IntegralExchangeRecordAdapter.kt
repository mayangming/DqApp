package com.wd.daquan.mine.adapter

import android.view.ViewGroup
import com.da.library.tools.DateUtil
import com.wd.daquan.R
import com.wd.daquan.imui.adapter.RecycleBaseAdapter
import com.wd.daquan.model.bean.DqMoneyHistoryEntity

/**
 * 积分明细的适配器
 */
class IntegralExchangeRecordAdapter() : RecycleBaseAdapter<IntegralExchangeRecordHolder>() {
    private var exchangeListener : ((DqMoneyHistoryEntity) -> Unit)? = null
    var dqMoneyHistoryEntityList = arrayListOf<DqMoneyHistoryEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    constructor(integralExchangeList :ArrayList<DqMoneyHistoryEntity>) :this(){
        this.dqMoneyHistoryEntityList = integralExchangeList
    }

     fun addData(integralExchangeList :List<DqMoneyHistoryEntity>){
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
        holder.integralRecordName.text = entity.tradeName
        holder.integralRecordTime.text = "到账时间:${DateUtil.timeToString(entity.tradTime, DateUtil.yyyy_MM_dd_HH_mm_ss)}"
        when(entity.tradeStatus){
            0 -> {
                holder.integralRecordCount.text = "+${entity.tradeMoney}"
            }
            1 -> {
                holder.integralRecordCount.text = "-${entity.tradeMoney}"
            }
        }
    }

}