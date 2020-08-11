package com.wd.daquan.explore.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.da.library.utils.BigDecimalUtils
import com.wd.daquan.R
import com.wd.daquan.explore.viewholder.MakeMoneyTaskViewHolder
import com.wd.daquan.glide.GlideUtils
import com.wd.daquan.imui.adapter.RecycleBaseAdapter
import com.wd.daquan.model.bean.MakeMoneyTaskBean

/**
 * 任务页面
 */
class MakeMoneyTaskAdapter() : RecycleBaseAdapter<MakeMoneyTaskViewHolder>(){
    var makeMoneyTaskBeanList = arrayListOf<MakeMoneyTaskBean>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    constructor(makeMoneyTaskBeanList :ArrayList<MakeMoneyTaskBean>) :this(){
        this.makeMoneyTaskBeanList = makeMoneyTaskBeanList
    }

    override fun getItemCount():Int {
        return makeMoneyTaskBeanList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MakeMoneyTaskViewHolder {
        super.onCreateViewHolder(parent, viewType)
        val view = inflater.inflate(R.layout.item_make_money_task,parent,false)
        return MakeMoneyTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: MakeMoneyTaskViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val bean = makeMoneyTaskBeanList[position]
        GlideUtils.load(context,bean.taskpic,holder.appIcon)
        holder.taskName.text = bean.taskname
        holder.taskPriceTv.text = "￥${getPrice(bean.taskmoney)}"
        holder.taskCountTv.text = "共${bean.classnum}/余${bean.lastnum}"
        initRecycleView(holder.taskLabelRv)
        val labelList = arrayListOf<String>(bean.type,bean.classification)
        holder.taskLabelRv.apply {
            val labelAdapter = LabelAdapter()
            labelAdapter.labelList = labelList
            adapter = labelAdapter
        }
    }

    fun getPrice(price :Long) = BigDecimalUtils.penny2Dollar(price).toPlainString()

    private fun initRecycleView(recycleView :RecyclerView){
        recycleView.layoutManager = LinearLayoutManager(recycleView.context,RecyclerView.HORIZONTAL,false)
    }

}