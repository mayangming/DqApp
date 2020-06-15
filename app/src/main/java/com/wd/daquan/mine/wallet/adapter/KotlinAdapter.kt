package com.wd.daquan.mine.wallet.adapter

import android.view.ViewGroup
import com.da.library.adapter.CommRecyclerViewAdapter
import com.wd.daquan.mine.wallet.holder.BaseKotlinHolder

/**
 * @Author: 方志
 * @Time: 2019/5/20 16:28
 * @Description:
 */
abstract class KotlinAdapter<T, VH : BaseKotlinHolder>(layoutId : Int) : CommRecyclerViewAdapter<T, BaseKotlinHolder>() {

    private val mLayoutId = layoutId

    override fun onBindView(parent: ViewGroup?, viewType: Int): BaseKotlinHolder {
        return BaseKotlinHolder(mInflater.inflate(mLayoutId, parent, false))
    }

}