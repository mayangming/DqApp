package com.dq.sdk.ad.listener

/**
 * APP下载监听
 */
interface DqAppDownloadListener {
    /**
     * 下载结束
     */
    fun onDownloadStart()

    /**
     * 下载结束
     */
    fun onDownloadSuccess()

    /**
     * 下载失败
     */
    fun onDownloadFail()

    /**
     * 正在下载
     */
    fun onDownloading()

}