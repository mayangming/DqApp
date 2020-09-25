package com.wd.daquan.mine.adapter

import android.view.ViewGroup
import com.wd.daquan.R
import com.wd.daquan.glide.GlideUtils
import com.wd.daquan.imui.adapter.RecycleBaseAdapter
import com.wd.daquan.mine.viewholder.IntegralMallGoodsHolder
import com.wd.daquan.model.bean.DqGoodsEntity

/**
 * 商城适配器
 */
class IntegralMallGoodsAdapter() : RecycleBaseAdapter<IntegralMallGoodsHolder>(){
    private var exchangeListener : ((DqGoodsEntity) -> Unit)? = null
    var dqGoodDetails = listOf<DqGoodsEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    constructor(taskCompleteBeans :ArrayList<DqGoodsEntity>) :this(){
        this.dqGoodDetails = taskCompleteBeans
    }
    override fun getItemCount() = dqGoodDetails.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntegralMallGoodsHolder {
        super.onCreateViewHolder(parent, viewType)
        val view=  inflater.inflate(R.layout.item_goods,parent,false)
        return IntegralMallGoodsHolder(view)
    }

    override fun onBindViewHolder(holder: IntegralMallGoodsHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val bean = dqGoodDetails[position]
        GlideUtils.load(context,bean.commoditiesPic,holder.goodsPic)
        holder.goodsName.text = bean.commoditiesNmae
        holder.goodsIntegralCount.text = bean.commoditiesPrice.toString()
        holder.goodsExchange.setOnClickListener {
//            showExchangeBottomDialog(taskCompleteBeans[position])
//            exchangeListener?.invoke(taskCompleteBeans[position])
            exchangeListener?.invoke(bean)
        }
    }

    fun setOnExchangeListener(listener :(DqGoodsEntity) -> Unit){
        exchangeListener = listener
    }

}