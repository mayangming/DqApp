package com.wd.daquan.explore.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.wd.daquan.explore.fragment.DynamicMediaPhotoDetailsFragment

/**
 * 朋友圈图片预览页面
 */
class DynamicMediaAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {
    var mediaList :List<String> = arrayListOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun getItem(p0: Int) = createFragment(mediaList[p0])

    override fun getCount() = mediaList.size

    private fun createFragment(mediaData :String) : Fragment {
        val fragment: Fragment
        val bundle = Bundle()
        bundle.putString(DynamicMediaPhotoDetailsFragment.ACTION_PHOTO_URL,mediaData)
        fragment = DynamicMediaPhotoDetailsFragment()
        fragment.arguments = bundle
        return fragment
    }
}