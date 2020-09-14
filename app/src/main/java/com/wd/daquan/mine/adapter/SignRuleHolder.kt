package com.wd.daquan.mine.adapter

import android.view.View
import android.widget.TextView
import com.wd.daquan.R
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder

class SignRuleHolder(itemView: View) : RecycleBaseViewHolder(itemView) {
    val ruleContent : TextView = itemView.findViewById(R.id.sign_rule_content)
}