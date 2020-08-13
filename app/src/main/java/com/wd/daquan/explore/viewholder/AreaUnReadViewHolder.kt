package com.wd.daquan.explore.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.wd.daquan.R
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder

/**
 * 朋友圈未读消息的列表
 */
class AreaUnReadViewHolder(itemView :View) : RecycleBaseViewHolder(itemView) {
    val userAvatar      :ImageView = itemView.findViewById(R.id.area_unread_avatar)
    val userName        :TextView  = itemView.findViewById(R.id.area_unread_name)
    val commentContent  :TextView  = itemView.findViewById(R.id.area_comment_content)
    val time            :TextView  = itemView.findViewById(R.id.area_unread_time)
    val areaImg         :ImageView = itemView.findViewById(R.id.area_unread_img)
    val zanImg         :ImageView = itemView.findViewById(R.id.area_unread_zan)
}