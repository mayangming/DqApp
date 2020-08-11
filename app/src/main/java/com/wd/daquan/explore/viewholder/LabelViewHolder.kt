package com.wd.daquan.explore.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.NonNull
import com.wd.daquan.R
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder

/**
 * 标签ViewHolder
 */
class LabelViewHolder(@NonNull itemView : View) : RecycleBaseViewHolder(itemView) {
    val labelContent : TextView = itemView.findViewById(R.id.label_content)
}