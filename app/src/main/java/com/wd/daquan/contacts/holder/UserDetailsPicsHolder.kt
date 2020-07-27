package com.wd.daquan.contacts.holder

import android.view.View
import android.widget.ImageView
import com.wd.daquan.R
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder

class UserDetailsPicsHolder(itemView :View)  : RecycleBaseViewHolder(itemView){
    val areaPhoto : ImageView = itemView.findViewById(R.id.user_info_details_pics)
}