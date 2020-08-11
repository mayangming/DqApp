package com.wd.daquan.explore.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import com.wd.daquan.R
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder

/**
 * 任务筛选条目
 */
class MakeMoneyTaskScreenViewHolder(@NonNull itemView : View) : RecycleBaseViewHolder(itemView) {
    val taskScreenContent : View = itemView.findViewById(R.id.btn_make_money_task_content)
    val taskScreenTv : TextView = itemView.findViewById(R.id.btn_make_money_task_tv)
}