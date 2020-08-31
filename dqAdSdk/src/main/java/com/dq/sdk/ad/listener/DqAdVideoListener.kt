package com.dq.sdk.ad.listener

/**
 * 视频加载监听
 */
interface DqAdVideoListener {

    /**
     * 缓存结束
     */
    fun cached(uri :String)

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
}