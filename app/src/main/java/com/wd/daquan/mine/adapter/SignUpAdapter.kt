package com.wd.daquan.mine.adapter

import android.view.ViewGroup
import com.wd.daquan.R
import com.wd.daquan.imui.adapter.RecycleBaseAdapter
import com.wd.daquan.model.bean.SignUpEntity

/**
 * 签到历史适配器
 */
class SignUpAdapter() : RecycleBaseAdapter<SignUpHolder>() {
    var signUpEntity = SignUpEntity()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    constructor(records :SignUpEntity) :this(){
        this.signUpEntity = records
    }
    override fun getItemCount() = signUpEntity.list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignUpHolder {
        super.onCreateViewHolder(parent, viewType)
        val view = inflater.inflate(R.layout.item_sign,parent,false)
        return SignUpHolder(view)
    }

    override fun onBindViewHolder(holder: SignUpHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val record = signUpEntity.list
        val signAwardEntity = record[position]
        val signUser = signUpEntity.dbUserSign
        holder.signPoints.text = "${signAwardEntity.dbaward}"
        holder.signDay.text = "${position+1}天"
        if (position < signUser.signNum-1){
            holder.rootContainer.alpha = 0.5f
            holder.signGoldCoin.setBackgroundResource(R.mipmap.gold_coin_signed)
        }else if (position > signUser.signNum-1){
            holder.signGoldCoin.setBackgroundResource(R.mipmap.gold_coin_unsign)
            holder.rootContainer.alpha = 1f
        }else{
            holder.rootContainer.alpha = 1f
            holder.signGoldCoin.setBackgroundResource(R.mipmap.gold_coin_signed_next)
        }

    }

}