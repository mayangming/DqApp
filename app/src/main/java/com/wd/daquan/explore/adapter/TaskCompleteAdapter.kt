package com.wd.daquan.explore.adapter

import android.view.ViewGroup
import com.wd.daquan.R
import com.wd.daquan.explore.viewholder.TaskCompleteViewHolder
import com.wd.daquan.glide.GlideUtils
import com.wd.daquan.imui.adapter.RecycleBaseAdapter
import com.wd.daquan.model.bean.ReleaseCompleteDetailsBean

/**
 * 任务完成情况
 */
class TaskCompleteAdapter() : RecycleBaseAdapter<TaskCompleteViewHolder>(){

    var taskCompleteBeans = listOf<ReleaseCompleteDetailsBean.TaskCompleteBean>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    constructor(taskCompleteBeans :ArrayList<ReleaseCompleteDetailsBean.TaskCompleteBean>) :this(){
        this.taskCompleteBeans = taskCompleteBeans
    }

    override fun getItemCount() = taskCompleteBeans.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskCompleteViewHolder {
        super.onCreateViewHolder(parent, viewType)
        val view=  inflater.inflate(R.layout.item_task_complete,parent,false)
        return TaskCompleteViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskCompleteViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val bean = taskCompleteBeans[position]
        GlideUtils.loadRound(context,bean.headpic,holder.headIcon)
        holder.userName.text = bean.nickName
        holder.dqNum.text = "斗圈号: ${bean.douquanNum}"
    }
}