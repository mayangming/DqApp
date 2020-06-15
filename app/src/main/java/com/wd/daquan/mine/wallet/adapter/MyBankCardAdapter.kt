package com.wd.daquan.mine.wallet.adapter

import android.view.View
import android.view.ViewGroup
import com.wd.daquan.DqApp
import com.wd.daquan.R
import com.da.library.adapter.CommRecyclerViewAdapter
import com.wd.daquan.glide.GlideUtils
import com.wd.daquan.mine.wallet.bean.BankCardBean
import com.wd.daquan.mine.wallet.holder.BaseKotlinHolder
import kotlinx.android.synthetic.main.bank_card_item.view.*

/**
 * @Author: 方志
 * @Time: 2019/5/16 11:56
 * @Description: 我的银行卡适配器
 */
//class MyBankCardAdapter : KotlinAdapter<BankCardBean, MyHolder>(R.layout.bank_card_item) {
class MyBankCardAdapter : CommRecyclerViewAdapter<BankCardBean, BaseKotlinHolder>() {

    private val body : Int = 1
    private val foot : Int = 2
    private var mListener : OnItemClickListener? = null
    private var isShowUnBind : Boolean = false


    override fun getItemViewType(position: Int): Int {
        if (position < allList.size){
            return body
        }
        return foot
    }

    override fun onBindView(parent: ViewGroup?, viewType: Int): BaseKotlinHolder {
        if (body == viewType){
            return BaseKotlinHolder(mInflater.inflate(R.layout.bank_card_item, parent, false))
        }

        return BaseKotlinHolder(mInflater.inflate(R.layout.bank_card_foot_item, parent, false))
    }


    override fun onBindData(holder: BaseKotlinHolder, position: Int) {
        super.onBindData(holder, position)
        when(getItemViewType(position)){
            body ->{
                setBodyData(position, holder)
            }
            foot ->{
                holder.itemView.setOnClickListener {
                    mListener?.onAddBank()
                }
            }
        }
    }

    private fun setBodyData(position: Int, holder: BaseKotlinHolder) {
        val item = getItem(position) ?: return

        if (isShowUnBind){
            holder.itemView.bank_card_unbind_tv.visibility = View.VISIBLE
        }else{
            holder.itemView.bank_card_unbind_tv.visibility = View.GONE
        }
        holder.itemView.bank_card_name_tv.text =  item.bankname
        holder.itemView.bank_card_num_tv.text = item.cardno
        GlideUtils.loadRound(DqApp.sContext, item.bank, holder.itemView.bank_card_bg_iv)

        holder.itemView.bank_card_unbind_tv.setOnClickListener {
            mListener?.onUnBindBank(item)
        }
    }

    override fun getItemCount(): Int {
        if ( allList.size == 0) {
            return 0
        }
        return allList.size + 1
    }

    fun setBankCardListener(listener: OnItemClickListener) {
        this.mListener = listener
    }

    fun isShowUnBind(isShowUnBind: Boolean){
        this.isShowUnBind = isShowUnBind
    }

    interface OnItemClickListener{
        fun onAddBank()
        fun onUnBindBank(bankCardBean: BankCardBean)
    }
}