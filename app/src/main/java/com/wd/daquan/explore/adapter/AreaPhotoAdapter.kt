package com.wd.daquan.explore.adapter

import android.app.Activity
import android.view.ViewGroup
import com.wd.daquan.R
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.explore.viewholder.AreaPhotoViewHolder
import com.wd.daquan.glide.GlideUtils
import com.wd.daquan.imui.adapter.RecycleBaseAdapter

/**
 * 朋友圈相册
 */
class AreaPhotoAdapter() : RecycleBaseAdapter<AreaPhotoViewHolder>() {

    var photos: ArrayList<String> = ArrayList<String>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    constructor(photos:  ArrayList<String>) : this(){
        this.photos = photos
    }

    override fun getItemCount() = photos.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaPhotoViewHolder {
        super.onCreateViewHolder(parent, viewType)
        val view = inflater.inflate(R.layout.item_area_photo,parent,false)
        return AreaPhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AreaPhotoViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        GlideUtils.load(context, photos[position], holder.areaPhoto)
        holder.areaPhoto.setOnClickListener {
            val act = context as Activity
            NavUtils.gotoDynamicMediaDetailsActivity(act,photos,position)
        }
    }
}