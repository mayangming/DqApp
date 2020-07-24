package com.wd.daquan.explore.activity

import com.wd.daquan.R
import com.wd.daquan.common.activity.DqBaseActivity
import com.wd.daquan.explore.adapter.DynamicMediaAdapter
import com.wd.daquan.explore.presenter.DynamicSendPresenter
import com.wd.daquan.model.bean.DataBean
import kotlinx.android.synthetic.main.activity_media_details.*

/**
 * 朋友圈多媒体内容预览功能
 */
class DynamicMediaDetailsActivity : DqBaseActivity<DynamicSendPresenter, DataBean<Any>>() {

    /**
     * 多媒体内容集合
     */
    private var mediaData : List<String> ?= null
    private var currentIndex = 0
    private var dynamicMediaDetailsAdapter = DynamicMediaAdapter(supportFragmentManager)

    companion object{
        const val MEDIA_DATA = "mediaData" //多媒体数据

        const val MEDIA_DATA_CURRENT = "mediaDataCurrent" //当前多媒体索引位置

    }

    override fun createPresenter() = DynamicSendPresenter()

    override fun setContentView() {
        setContentView(R.layout.activity_media_details)
    }

    override fun initView() {
        media_details_vp.adapter = dynamicMediaDetailsAdapter
    }

    override fun initData() {
        currentIndex = intent.getIntExtra(MEDIA_DATA_CURRENT,0)
        mediaData = intent.getSerializableExtra(MEDIA_DATA) as List<String>
        mediaData?.let {
            media_details_vp.adapter =  dynamicMediaDetailsAdapter
            dynamicMediaDetailsAdapter.mediaList = it
            media_details_vp.currentItem = currentIndex
        }
    }
}