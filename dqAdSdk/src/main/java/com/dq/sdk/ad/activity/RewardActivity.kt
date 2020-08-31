package com.dq.sdk.ad.activity

import android.os.Bundle
import android.util.Log
import com.dq.sdk.ad.R
import com.dq.sdk.ad.base.BaseActivity
import com.dq.sdk.ad.bean.RewardBean
import com.dq.sdk.ad.listener.DqAdVideoListener
import com.dq.sdk.ad.listener.DqAppDownloadListener
import com.dq.sdk.ad.listener.RewardAdListener
import com.dq.sdk.ad.util.ViewModelUtils
import com.dq.sdk.ad.view.RewardedView
import com.dq.sdk.ad.viewmodel.AdViewModel
import kotlinx.android.synthetic.main.dq_ad_activity_reward.*

/**
 * 激励视频页面
 */
class RewardActivity :BaseActivity(){
    private var rewardListener : RewardAdListener?= null
    private lateinit var rewardView : RewardedView
    private var rewardBean : RewardBean?= null


    companion object{
        const val ACTION_DATA = "actionData"
    }

    private val model : AdViewModel by lazy {
        ViewModelUtils.getViewModel(this,AdViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dq_ad_activity_reward)
        initData()
        rewardBean?.let {
            initRewardView(it)
            dq_ad_reward_container.addView(rewardView)
            rewardView.loadAd(it)
        }
    }

    private fun initData(){
        this.rewardBean = intent.getSerializableExtra(ACTION_DATA) as RewardBean
        rewardListener = model.getRewardAdListener()
    }

    private fun initRewardView(rewardBean :RewardBean){
        this.rewardBean = rewardBean
        rewardView = RewardedView(this)
        rewardView.setOnDqVideoListener(object : DqAdVideoListener {
            override fun onComplete() {
                rewardListener?.onComplete()
                Log.e("YM","视频播放结束onComplete")
            }

            override fun onError() {
                rewardListener?.onError()
            }

            override fun adClose() {
                Log.e("YM","关闭广告")
                rewardListener?.adClose()
                finish()
            }

            override fun cached(videoPath :String) {
                rewardView.loadVideo(videoPath)
                rewardListener?.loadAdSuccess(rewardView)
            }
        })
        rewardView.setOnDownloadAppListener(object : DqAppDownloadListener {
            override fun onDownloadSuccess() {
            }

            override fun onDownloadFail() {
            }

            override fun onDownloading() {
            }

            override fun onDownloadStart() {
                model.downloadCount(rewardBean.historyId)
            }
        })
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }
    override fun onDestroy() {
        super.onDestroy()
        ViewModelUtils.removeViewModel(AdViewModel::class.java)
    }

}