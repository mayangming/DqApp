package com.wd.daquan.explore.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.da.library.tools.DateUtil
import com.da.library.utils.BigDecimalUtils
import com.wd.daquan.R
import com.wd.daquan.explore.type.MakeMonetTaskType
import com.wd.daquan.explore.viewholder.MakeMoneyTaskMineViewHolder
import com.wd.daquan.glide.GlideUtils
import com.wd.daquan.imui.adapter.RecycleBaseAdapter
import com.wd.daquan.model.bean.MakeMoneyTaskMineBean

class MakeMoneyTaskMineAdapter(): RecycleBaseAdapter<MakeMoneyTaskMineViewHolder>() {
    var makeMoneyTaskMineList = arrayListOf<MakeMoneyTaskMineBean>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    override fun getItemCount() = makeMoneyTaskMineList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MakeMoneyTaskMineViewHolder {
        super.onCreateViewHolder(parent, viewType)
        val view = inflater.inflate(R.layout.item_make_money_mine,parent,false)
        return MakeMoneyTaskMineViewHolder(view)
    }

    override fun onBindViewHolder(holder: MakeMoneyTaskMineViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val bean = makeMoneyTaskMineList[position]
        GlideUtils.load(context,bean.taskPlatformIcon,holder.taskPlatformIcon)
        holder.taskName.text = bean.taskName
        holder.taskPrice.text = "￥${getPrice(bean.taskmoney)}"

        holder.taskSignTime.text = "报名时间:  ${DateUtil.timeToString(bean.getTime,DateUtil.yyyy_MM_dd_HH_mm_ss)}"
        initRecycleView(holder.taskLabelRv)
        val labelList = arrayListOf<String>(bean.type,bean.classification)
        holder.taskLabelRv.apply {
            val labelAdapter = LabelAdapter()
            labelAdapter.labelList = labelList
            adapter = labelAdapter
        }

        val taskStatus = MakeMonetTaskType.getMakeMoneyTaskTypeByType(bean.taskStatus)
        if (null != taskStatus){
            holder.taskStatus.setBackgroundResource(taskStatus.drawableRes)
            holder.taskStatus.text = taskStatus.content
            holder.taskStatus.setTextColor(context.resources.getColor(taskStatus.textColor))
        }else{
            holder.taskStatus.setBackgroundResource(R.drawable.btn_border_999999)
            holder.taskStatus.text = "已过期"
            holder.taskStatus.setTextColor(context.resources.getColor(R.color.color_grey_999999))
        }

    }

    fun getPrice(price :Long) = BigDecimalUtils.penny2Dollar(price).toPlainString()

    private fun initRecycleView(recycleView : RecyclerView){
        recycleView.layoutManager = LinearLayoutManager(recycleView.context, RecyclerView.HORIZONTAL,false)
    }

}