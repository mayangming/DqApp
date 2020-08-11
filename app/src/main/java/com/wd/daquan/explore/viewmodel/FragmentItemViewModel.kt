package com.wd.daquan.explore.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

/**
 * 持有Fragment的ViewModel
 */
class FragmentItemViewModel :ViewModel(){
    private var nextValue = 1L
    private val items = mutableListOf<String>()
    private val fragmentList = mutableListOf<Fragment>()
    fun getItemById(id: Long): String = items.first { itemToLong(it) == id }
    fun itemId(position: Int): Long = itemToLong(items[position])
    fun contains(itemId: Long): Boolean = items.any { itemToLong(it) == itemId }
    fun addNewAt(position: Int) = items.add(position, longToItem(nextValue++))
    fun addNewAt(fragment: Fragment){
        fragmentList.add(fragment)
        items.add(longToItem(nextValue++))
    }
    fun removeAt(position: Int){
        fragmentList.removeAt(position)
        items.removeAt(position)
    }
    fun createIdSnapshot(): List<Long> = (0 until size).map { position -> itemId(position) }

    fun getFragment(position: Int) = fragmentList[position]

    val size: Int get() = items.size
    private fun longToItem(value: Long): String = "item#$value"
    private fun itemToLong(value: String): Long = value.split("#")[1].toLong()
}