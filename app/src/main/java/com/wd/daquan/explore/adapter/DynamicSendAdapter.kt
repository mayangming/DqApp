package com.wd.daquan.explore.adapter

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.wd.daquan.R
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.explore.bean.DynamicSendPhotoBean
import com.wd.daquan.explore.type.DynamicSendPhotoType
import com.wd.daquan.explore.viewholder.DynamicSendViewHolder
import com.wd.daquan.glide.GlideUtils
import com.wd.daquan.imui.adapter.RecycleBaseAdapter
import com.wd.daquan.model.log.DqToast

/**
 * 动态发送页的适配器
 */
class DynamicSendAdapter()  : RecycleBaseAdapter<DynamicSendViewHolder>() {
    var photos = ArrayList<DynamicSendPhotoBean>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    //添加图片的接口回调
    var addPhotoListener: (() -> Unit?)? = null

    override fun getItemCount():Int {//假如没有图片则在结尾显示一个，有图片则在结尾显示两个
        return photos.size
    }
    constructor(photos: ArrayList<DynamicSendPhotoBean>):this() {
        this.photos = photos
    }

    fun getPhotoCount(): Int = photos.count {
        DynamicSendPhotoType.NORMAL == it.dynamicSendPhotoType
    }

    // 设置添加图片的接口回调
    fun setOnAddPhotoListener(addPhotoListener: () -> Unit) {
        this.addPhotoListener = addPhotoListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DynamicSendViewHolder {
        super.onCreateViewHolder(parent, viewType)
        val view = inflater.inflate(R.layout.item_dynamic_photo,parent,false)
        return DynamicSendViewHolder(view)
    }

    override fun onBindViewHolder(holder: DynamicSendViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val dynamicPhotoBean = photos[position]
        Log.e("YM","当前坐标:${position}  item类型:${dynamicPhotoBean.dynamicSendPhotoType.photoType}")
        when(dynamicPhotoBean.dynamicSendPhotoType){
            DynamicSendPhotoType.ADD -> {
                holder.dynamicSendPhoto.scaleType = ImageView.ScaleType.FIT_XY
                holder.dynamicSendPhoto.setImageResource(R.mipmap.dynamics_photo_add)
            }
            else -> {
                holder.dynamicSendPhoto.scaleType = ImageView.ScaleType.MATRIX
                GlideUtils.load(context, photos[position].uri, holder.dynamicSendPhoto)
            }
        }

        if (dynamicPhotoBean.dynamicSendPhotoType == DynamicSendPhotoType.NORMAL){
            holder.dynamicSendRemoveLabel.visibility = View.VISIBLE
        }else{
            holder.dynamicSendRemoveLabel.visibility = View.GONE
        }
        holder.dynamicSendRemoveLabel.setOnClickListener {
            photos.removeAt(position)
            notifyDataSetChanged()
        }
        val newList = photos.map {
            it.uri.toString()
        }
        holder.dynamicSendPhoto.setOnClickListener {
            when(dynamicPhotoBean.dynamicSendPhotoType){
                DynamicSendPhotoType.ADD -> {
                    DqToast.showCenterShort("添加图片")
                    addPhotoListener?.invoke()
                }
                else -> {
                    val act = context as Activity
                    NavUtils.gotoDynamicMediaDetailsActivity(act,newList,position)
                }
            }

        }
    }

}