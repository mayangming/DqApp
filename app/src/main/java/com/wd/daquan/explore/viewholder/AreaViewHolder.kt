package com.wd.daquan.explore.viewholder

import androidx.annotation.NonNull
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.wd.daquan.R
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder

/**
 * 朋友圈的列表Model1
 */
class AreaViewHolder(@NonNull itemView : View) : RecycleBaseViewHolder(itemView){
    val userName: TextView = itemView.findViewById(R.id.area_user_name)
    val dynamicWord: TextView = itemView.findViewById(R.id.area_dynamic_word_tv)
    val dynamicTimeTv: TextView = itemView.findViewById(R.id.area_dynamic_time_tv)
    val dynamicReviewIv: View = itemView.findViewById(R.id.area_dynamic_review_iv)
    var areaReviewContainer:  View = itemView.findViewById(R.id.area_review_ll)
    var areaDingTv: TextView = itemView.findViewById(R.id.area_ding_tv)
    var areaDividerV: View = itemView.findViewById(R.id.area_divider_v)
    var areaHeadIcon: ImageView = itemView.findViewById(R.id.area_head_icon)
    var areaHeadContainer: View = itemView.findViewById(R.id.area_head_contain)
    var areaDynamicDelTv: TextView = itemView.findViewById(R.id.area_dynamic_del_tv)
    var dynamicPhotoRv : RecyclerView?= null
    var areaReviewRv : RecyclerView?= null
    override fun initView() {
        super.initView()
        dynamicPhotoRv = itemView.findViewById(R.id.area_dynamic_photo_rv)
        areaReviewRv = itemView.findViewById(R.id.area_review_rv)
        dynamicPhotoRv?.apply {
            layoutManager = GridLayoutManager(context, 3)
        }
        areaReviewRv?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }
}