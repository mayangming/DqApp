package com.wd.daquan.explore.adapter

import android.view.ViewGroup
import com.wd.daquan.R
import com.wd.daquan.explore.viewholder.MakeMoneyTaskScreenViewHolder
import com.wd.daquan.imui.adapter.RecycleBaseAdapter
import com.wd.daquan.model.bean.MakeMoneyTaskScreenBean
import com.wd.daquan.model.bean.TaskTypeBean

/**
 * 任务筛选页面条目适配器
 */
class MakeMoneyTaskTypeAdapter(): RecycleBaseAdapter<MakeMoneyTaskScreenViewHolder>()  {

    var taskScreenList = arrayListOf<TaskTypeBean>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    constructor(taskScreenList :ArrayList<TaskTypeBean>):this(){
        this.taskScreenList = taskScreenList
    }


    override fun getItemCount() = taskScreenList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MakeMoneyTaskScreenViewHolder {
        super.onCreateViewHolder(parent, viewType)
        val view = inflater.inflate(R.layout.item_make_money_task_screen,parent,false)
        return MakeMoneyTaskScreenViewHolder(view)
    }

    override fun onBindViewHolder(holder: MakeMoneyTaskScreenViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val model = taskScreenList[position]
        holder.taskScreenTv.text = model.typeName
        holder.taskScreenContent.isSelected = model.isSelect
        holder.taskScreenContent.setOnClickListener {
            model.isSelect = !model.isSelect
            notifyDataSetChanged()
        }
    }

    /**
     * 清空选中的状态
     */
    fun clearSelected(){
        taskScreenList.forEach {
            it.isSelect = false
        }
        notifyDataSetChanged()
    }

}