package com.dq.sdk.ad.listener

import android.view.View

/**
 * 激励视频监听
 */
interface RewardAdListener {

    fun loadAdSuccess(view :View)

    /**
     * 开始准备
     */
    fun ide()

    /**
     * 播放完毕
     */
    fun onComplete()
    /**
     * 播放错误
     */
    fun onError()

    /**
     * 广告关闭
     */
    fun adClose()

    /**
     * 下载监听
     */
    fun downloadListener(downLoadListener :DqAppDownloadListener)

}