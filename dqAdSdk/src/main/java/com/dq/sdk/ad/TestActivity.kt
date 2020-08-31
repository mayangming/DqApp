package com.dq.sdk.ad

import android.os.Bundle
import android.util.Log
import android.view.View
import com.dq.sdk.ad.ad.DqRewardAd
import com.dq.sdk.ad.base.BaseActivity
import com.dq.sdk.ad.config.DqAdConfig
import com.dq.sdk.ad.listener.DqAppDownloadListener
import com.dq.sdk.ad.listener.RewardAdListener
import kotlinx.android.synthetic.main.dq_ad_activity_test.*

class TestActivity : BaseActivity(){
    private lateinit var rewardView :View
    private lateinit var dqRewardAd : DqRewardAd
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dq_ad_activity_test)
        initAdSdk()
    }

    private fun initAdSdk(){
        DqAdSdk.init(DqAdConfig().Build().setAppId("1").build())
        dqRewardAd = DqRewardAd(this)
        dqRewardAd.setOnRewardAdListener(object : RewardAdListener{
            override fun ide() {
            }

            override fun onComplete() {
                rewardView.visibility = View.GONE
            }

            override fun onError() {
            }

            override fun adClose() {
                rewardView.visibility = View.GONE
            }

            override fun loadAdSuccess(view: View) {
                Log.e("YM","加载广告成功")
                rewardView = view
                ad_container.addView(view)
            }

            override fun downloadListener(downLoadListener: DqAppDownloadListener) {
                TODO("Not yet implemented")
            }
        })
    }


}