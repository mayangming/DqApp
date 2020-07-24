package com.wd.daquan.explore.viewholder

import android.support.annotation.NonNull
import android.view.View
import android.widget.ImageView
import com.wd.daquan.R
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder

/**
 * 朋友圈相册
 */
class AreaPhotoViewHolder(@NonNull itemView : View) : RecycleBaseViewHolder(itemView){
    val areaPhoto :ImageView = itemView.findViewById(R.id.item_area_photo)
}