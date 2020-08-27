package com.wd.daquan.explore.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.wd.daquan.R
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder

class SendTaskViewHolder(@NonNull itemView :View) : RecycleBaseViewHolder(itemView){
    val taskName : TextView = itemView.findViewById(R.id.task_name)
    val appIcon : ImageView = itemView.findViewById(R.id.task_platform_icon)
    val taskLabelRv : RecyclerView = itemView.findViewById(R.id.task_label)
    val taskSignTime : TextView = itemView.findViewById(R.id.task_sign_time)
    val taskStatus : TextView = itemView.findViewById(R.id.task_status)
}