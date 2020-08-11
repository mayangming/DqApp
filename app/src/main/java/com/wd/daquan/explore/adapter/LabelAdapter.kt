package com.wd.daquan.explore.adapter

import android.view.ViewGroup
import com.wd.daquan.R
import com.wd.daquan.explore.viewholder.LabelViewHolder
import com.wd.daquan.imui.adapter.RecycleBaseAdapter

class LabelAdapter(): RecycleBaseAdapter<LabelViewHolder>(){
    var labelList = arrayListOf<String>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun getItemCount() = labelList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelViewHolder {
        super.onCreateViewHolder(parent, viewType)
        val view = inflater.inflate(R.layout.item_label,parent,false)
        return LabelViewHolder(view)
    }

    override fun onBindViewHolder(holder: LabelViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.labelContent.text = labelList[position]
    }

}