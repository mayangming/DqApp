package com.wd.daquan.mine.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.wd.daquan.R
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder

class IntegralMallGoodsHolder(itemView: View) : RecycleBaseViewHolder(itemView){
    val goodsPic : ImageView = itemView.findViewById(R.id.goods_pic)
    val goodsName : TextView = itemView.findViewById(R.id.goods_name)
    val goodsIntegralCount : TextView = itemView.findViewById(R.id.integral_count)
    val goodsExchange : TextView = itemView.findViewById(R.id.integral_exchange)
}