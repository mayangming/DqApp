package com.wd.daquan.explore.adapter

import android.view.ViewGroup
import com.wd.daquan.R
import com.wd.daquan.explore.bean.CustomTaskTypeBean
import com.wd.daquan.explore.viewholder.SendTaskTypeViewHolder
import com.wd.daquan.glide.GlideUtils
import com.wd.daquan.imui.adapter.RecycleBaseAdapter

class SendTaskTypeAdapter() : RecycleBaseAdapter<SendTaskTypeViewHolder>(){
    var customTaskTypeBeanList = listOf<CustomTaskTypeBean>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    constructor(customTaskTypeBean :ArrayList<CustomTaskTypeBean>) :this(){
        this.customTaskTypeBeanList = customTaskTypeBean
    }

    override fun getItemCount() = customTaskTypeBeanList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SendTaskTypeViewHolder {
        super.onCreateViewHolder(parent, viewType)
        val view= inflater.inflate(R.layout.item_task_type,parent,false)
        return SendTaskTypeViewHolder(view)
    }

    override fun onBindViewHolder(holder: SendTaskTypeViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val taskTypeBean = customTaskTypeBeanList[position]
        GlideUtils.loadRound(context,taskTypeBean.typePic,holder.taskIcon,5)
        holder.taskDesc.text = taskTypeBean.typeName
    }
}