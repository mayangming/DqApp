package com.wd.daquan.explore.viewholder

import android.support.annotation.NonNull
import android.view.View
import android.widget.ImageView
import com.wd.daquan.R
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder

/**
 * 动态发布页
 */
class DynamicSendViewHolder(@NonNull itemView : View) : RecycleBaseViewHolder(itemView) {
    val dynamicSendPhoto: ImageView = itemView.findViewById(R.id.dynamic_send_photo)
    val dynamicSendRemoveLabel: ImageView = itemView.findViewById(R.id.dynamic_send_remove_label)
}