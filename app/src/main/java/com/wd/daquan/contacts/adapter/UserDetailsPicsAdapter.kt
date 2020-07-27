package com.wd.daquan.contacts.adapter

import android.view.ViewGroup
import com.wd.daquan.R
import com.wd.daquan.contacts.holder.UserDetailsPicsHolder
import com.wd.daquan.glide.GlideUtils
import com.wd.daquan.imui.adapter.RecycleBaseAdapter

/**
 * 用户详情的朋友圈列表
 */
class UserDetailsPicsAdapter() : RecycleBaseAdapter<UserDetailsPicsHolder>(){
    private var pics = mutableListOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    constructor(pics : MutableList<String>) : this() {
        this.pics = pics
    }

    fun addData(pics : MutableList<String>){
        this.pics.addAll(pics)
        notifyDataSetChanged()
    }

    override fun getItemCount() = pics.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDetailsPicsHolder {
        super.onCreateViewHolder(parent, viewType)
        val contentView = inflater.inflate(R.layout.item_user_details_pics,parent,false)
        return UserDetailsPicsHolder(contentView)
    }

    override fun onBindViewHolder(holder: UserDetailsPicsHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        GlideUtils.load(context, pics[position], holder.areaPhoto)
    }

}