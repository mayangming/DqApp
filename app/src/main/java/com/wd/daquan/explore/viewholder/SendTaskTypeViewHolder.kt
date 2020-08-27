package com.wd.daquan.explore.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import com.wd.daquan.R
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder

/**
 * 任务类型
 */
class SendTaskTypeViewHolder(@NonNull itemView : View) : RecycleBaseViewHolder(itemView){
    val taskIcon : ImageView = itemView.findViewById(R.id.task_icon)
    val taskDesc : TextView = itemView.findViewById(R.id.task_desc)
}