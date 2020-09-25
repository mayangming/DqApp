package com.wd.daquan.mine.adapter

import android.view.ViewGroup
import com.wd.daquan.R
import com.wd.daquan.imui.adapter.RecycleBaseAdapter
import com.wd.daquan.mine.viewholder.SignRuleHolder
import com.wd.daquan.model.bean.SignRuleEntity

/**
 * 签到规则适配器
 */
class SignRuleAdapter() : RecycleBaseAdapter<SignRuleHolder>() {
    var signRuleList = listOf<SignRuleEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    constructor(taskCompleteBeans :ArrayList<SignRuleEntity>) :this(){
        this.signRuleList = taskCompleteBeans
    }
    override fun getItemCount() = signRuleList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignRuleHolder {
        super.onCreateViewHolder(parent, viewType)
        val view = inflater.inflate(R.layout.item_sign_rule,parent,false)
        return SignRuleHolder(view)
    }

    override fun onBindViewHolder(holder: SignRuleHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val entity = signRuleList[position]
        holder.ruleContent.text = "${position+1}. ${entity.rule}"
    }

}