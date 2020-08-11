package com.wd.daquan.explore.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.wd.daquan.R
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder

class MakeMoneyTaskViewHolder(@NonNull itemView :View) : RecycleBaseViewHolder(itemView){
    val taskName : TextView = itemView.findViewById(R.id.make_money_task_name_tv)
    val appIcon : ImageView = itemView.findViewById(R.id.make_money_task_icon)
    val taskLabelRv : RecyclerView = itemView.findViewById(R.id.make_money_task_label_rv)
    val taskPriceTv : TextView = itemView.findViewById(R.id.make_money_task_price_tv)
    val taskCountTv : TextView = itemView.findViewById(R.id.make_money_task_count_tv)
}