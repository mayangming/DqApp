package com.wd.daquan.explore.adapter

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wd.daquan.explore.viewmodel.FragmentItemViewModel

/**
 * ViewPager适配器
 */
class MakeMoneyPagerAdapter : FragmentStateAdapter {

//    var fragments :MutableList<Fragment> = arrayListOf()
    var fragmentViewModel : FragmentItemViewModel ?= null
    constructor(fragmentActivity :FragmentActivity):super(fragmentActivity)
    constructor(fragment :Fragment):super(fragment)
    constructor(@NonNull fragmentManager : FragmentManager, lifecycle : Lifecycle):super(fragmentManager,lifecycle)

    override fun getItemCount() = fragmentViewModel?.size ?: 0

    override fun createFragment(position: Int) = fragmentViewModel!!.getFragment(position)

    override fun getItemId(position: Int): Long = fragmentViewModel?.itemId(position) ?: 0
    override fun containsItem(itemId: Long): Boolean = fragmentViewModel?.contains(itemId) ?: false
}