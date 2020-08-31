package com.dq.sdk.ad.ad

import android.content.Intent
import android.graphics.Point
import android.util.Log
import android.view.Display
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.dq.sdk.ad.activity.RewardActivity
import com.dq.sdk.ad.bean.RewardBean
import com.dq.sdk.ad.listener.DqAdVideoListener
import com.dq.sdk.ad.listener.DqAppDownloadListener
import com.dq.sdk.ad.listener.RewardAdListener
import com.dq.sdk.ad.util.ViewModelUtils
import com.dq.sdk.ad.util.isFullWindow
import com.dq.sdk.ad.view.RewardedView
import com.dq.sdk.ad.viewmodel.AdViewModel

/**
 * 参考链接：
 * 获取控件的宽高:
 * https://blog.csdn.net/qq_26761229/article/details/89878861
 */
class DqRewardAd(private val activity :FragmentActivity) {
    private lateinit var rewardView : RewardedView
    private var rewardListener : RewardAdListener ?= null
    private var adVideoListener : DqAdVideoListener ?= null
    private var rewardBean :RewardBean ?= null
    private var rewardViewW = 0 //控件显示的宽度
    private var rewardViewH = 0 //控件显示的高度
    private var windowW = 0 //屏幕宽度
    private var windowH = 0 //屏幕高度
    private var isFullWindow = false //是否充满屏幕
    private var isCover = false //是否被遮挡
    private val model : AdViewModel by lazy {
        ViewModelUtils.getViewModel(activity,AdViewModel::class.java)
    }

    init {
        initViewModel()
    }

    private fun initViewModel(){
        model.rewardBean.observe(activity, Observer<RewardBean> {
            if (it.adType.isEmpty()){
                return@Observer
            }
//            rewardView.loadAd(it)
            activity.startActivity(Intent(activity, RewardActivity::class.java).putExtra(RewardActivity.ACTION_DATA,it))
        })
        val defaultDisplay: Display = activity.windowManager.defaultDisplay
        val point = Point()
        defaultDisplay.getSize(point)
        val x: Int = point.x
        val y: Int = point.y
    }

    fun loadAd(codeId :String){
        model.getAd(codeId)
    }

    fun showAd(){
        rewardBean?.let {
//            rewardView.showAd()
            rewardView.post(Runnable {
                val width: Int = rewardView.measuredWidth
                val height: Int = rewardView.measuredHeight
                Log.e("YM","控件的宽:$width, 高$height")
                isFullWindow = isFullWindow(windowW,windowH,width,height)
                isCover = rewardView.isCover()
                Log.e("YM","是否遮罩:${isCover}")
            })
        }
    }


    fun setOnRewardAdListener(rewardListener : RewardAdListener){
        this.rewardListener = rewardListener
        model.setRewardAdListener(rewardListener)
    }
}