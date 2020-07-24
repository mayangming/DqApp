package com.wd.daquan.explore.viewholder

import android.support.annotation.NonNull
import android.view.View
import android.widget.TextView
import com.wd.daquan.R
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder

class AreaReviewViewHolder(@NonNull itemView : View) : RecycleBaseViewHolder(itemView) {
    val itemAreaReviewContent : TextView = itemView.findViewById(R.id.item_area_review_content)
}