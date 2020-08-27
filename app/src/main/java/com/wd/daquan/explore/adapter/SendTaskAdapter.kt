package com.wd.daquan.explore.adapter

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.da.library.tools.DateUtil
import com.da.library.utils.BigDecimalUtils
import com.wd.daquan.R
import com.wd.daquan.explore.viewholder.SendTaskViewHolder
import com.wd.daquan.glide.GlideUtils
import com.wd.daquan.imui.adapter.RecycleBaseAdapter
import com.wd.daquan.model.bean.SendTaskBean
import com.wd.daquan.model.log.DqLog

/**
 * 任务页面
 */
class SendTaskAdapter() : RecycleBaseAdapter<SendTaskViewHolder>(){
    var makeMoneyTaskBeanList = arrayListOf<SendTaskBean>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    constructor(makeMoneyTaskBeanList :ArrayList<SendTaskBean>) :this(){
        this.makeMoneyTaskBeanList = makeMoneyTaskBeanList
    }

    override fun getItemCount():Int {
        return makeMoneyTaskBeanList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SendTaskViewHolder {
        super.onCreateViewHolder(parent, viewType)
        val view = inflater.inflate(R.layout.item_send_task_manager,parent,false)
        return SendTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: SendTaskViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val bean = makeMoneyTaskBeanList[position]
        GlideUtils.load(context,bean.typePic,holder.appIcon)
        holder.taskName.text = bean.taskname
        holder.taskSignTime.text = "报名时间:  ${DateUtil.timeToString(bean.createtime, DateUtil.yyyy_MM_dd_HH_mm_ss)}"
        updateStatus(bean,holder.taskStatus)
        initRecycleView(holder.taskLabelRv)
        val labelList = arrayListOf<String>(bean.typeName,bean.className)
        holder.taskLabelRv.apply {
            val labelAdapter = LabelAdapter()
            labelAdapter.labelList = labelList
            adapter = labelAdapter
        }
    }

    /**
     * 更改状态
     */
    private fun updateStatus(bean :SendTaskBean,tv :TextView){
        if(bean.isPass == 0){
            tv.text = "待提交"
            tv.setTextColor(context.resources.getColor(R.color.color_ff999999))
            tv.setBackgroundResource(R.drawable.btn_border_999999)
        }else if (bean.isPass == 1){//审核中
            tv.text = "审核中"
            tv.setTextColor(context.resources.getColor(R.color.color_F39825))
            tv.setBackgroundResource(R.drawable.btn_border_f39825)
        }else if (bean.isPass == 2){//通过
            tv.text = "已上架"
            tv.setTextColor(context.resources.getColor(R.color.color_1FA23E))
            tv.setBackgroundResource(R.drawable.btn_border_1fa23e)
        }else if (bean.isPass == 3){//拒绝
            tv.text = "未通过"
            tv.setTextColor(context.resources.getColor(R.color.color_EF5B40))
            tv.setBackgroundResource(R.drawable.btn_border_ef5b40)
        }

        if (bean.expires == 1){
            tv.text = "已过期"
            tv.setTextColor(context.resources.getColor(R.color.color_ff999999))
            tv.setBackgroundResource(R.drawable.btn_border_999999)
        }
    }

    fun getPrice(price :Long) = BigDecimalUtils.penny2Dollar(price).toPlainString()

    private fun initRecycleView(recycleView :RecyclerView){
        recycleView.layoutManager = LinearLayoutManager(recycleView.context,RecyclerView.HORIZONTAL,false)
    }

}