package com.wd.daquan.mine.adapter

import android.view.View
import android.widget.TextView
import com.wd.daquan.R
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder

/**
 * 签到适配器
 */
class SignUpHolder(itemView: View) : RecycleBaseViewHolder(itemView) {
    val rootContainer : View = itemView.findViewById(R.id.sign_root_container)
    val signGoldCoin : View = itemView.findViewById(R.id.sign_gold_coin)
    val signPoints : TextView = itemView.findViewById(R.id.sign_points)
    val signDay : TextView = itemView.findViewById(R.id.sign_day)
}