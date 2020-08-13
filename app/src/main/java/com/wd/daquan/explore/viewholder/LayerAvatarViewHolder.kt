package com.wd.daquan.explore.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import com.wd.daquan.R
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder

/**
 * 层叠头像
 */
class LayerAvatarViewHolder(@NonNull itemView : View) : RecycleBaseViewHolder(itemView) {
    val avatar: ImageView = itemView.findViewById(R.id.item_layer_avatar)
    val root: View = itemView.findViewById(R.id.root)
}