package com.wd.daquan.explore.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import com.wd.daquan.R
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder

class TaskCompleteViewHolder(@NonNull itemView : View) : RecycleBaseViewHolder(itemView) {
    val headIcon : ImageView = itemView.findViewById(R.id.head_icon)
    val userName : TextView = itemView.findViewById(R.id.user_name)
    val dqNum : TextView = itemView.findViewById(R.id.user_dq_num)
    val endTime : TextView = itemView.findViewById(R.id.task_complete_time)
}