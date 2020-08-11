package com.wd.daquan.explore.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.wd.daquan.R
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder

class MakeMoneyTaskMineViewHolder(@NonNull itemView : View) : RecycleBaseViewHolder(itemView) {
    val taskPlatformIcon : ImageView = itemView.findViewById(R.id.task_platform_icon)
    val taskName : TextView = itemView.findViewById(R.id.task_name)
    val taskPrice : TextView = itemView.findViewById(R.id.task_price_tv)
    val taskSignTime : TextView = itemView.findViewById(R.id.task_sign_time)
    val taskStatus : TextView = itemView.findViewById(R.id.task_status)
    val taskLabelRv : RecyclerView = itemView.findViewById(R.id.task_label)
}