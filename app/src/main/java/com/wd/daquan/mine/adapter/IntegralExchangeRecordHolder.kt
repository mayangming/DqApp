package com.wd.daquan.mine.adapter

import android.view.View
import android.widget.TextView
import com.wd.daquan.R
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder

/**
 * 积分兑换记录
 */
class IntegralExchangeRecordHolder(itemView: View) : RecycleBaseViewHolder(itemView){
    val integralRecordName : TextView = itemView.findViewById(R.id.integral_record_name)
    val integralRecordTime : TextView = itemView.findViewById(R.id.integral_record_time)
    val integralRecordCount : TextView = itemView.findViewById(R.id.integral_record_count)
}