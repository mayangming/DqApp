package com.dq.sdk.ad.view

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.dq.sdk.ad.R
import com.dq.sdk.ad.activity.RewardActivity
import com.dq.sdk.ad.bean.RewardBean
import com.dq.sdk.ad.listener.DqAdVideoListener
import com.dq.sdk.ad.listener.DqAppDownloadListener
import com.dq.sdk.ad.util.AndroidDownloadManagerUtils
import com.dq.sdk.ad.util.GlideUtil
import com.dq.sdk.ad.util.download.HttpDownFileUtils
import com.dq.sdk.ad.util.file.FileManager
import kotlinx.android.synthetic.main.ad_layout_rewarded.view.*
import java.io.File


/**
 * 激励广告的View
 */
class RewardedView :FrameLayout{
    private var countTime = 60
    private var rewardBean :RewardBean ?= null
    private var rewardListener : DqAdVideoListener ?= null
    private var downLoadListener : DqAppDownloadListener ?= null
    private var videoCurPosition = 0 //视频当前播放进度
    private var videoDuration = 0 //视频长度

    private val myHandler : Handler = object :Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg.what){
                TIMER ->{
                    videoCurPosition += 300
                    if (videoCurPosition < videoDuration){
                        sendEmptyMessageDelayed(TIMER,300)
                    }
                }
                STOP -> {
                    videoCurPosition = 0
                }
                PAUSE -> {

                }
                else -> {
                }
            }
        }
    }

    companion object{
        const val TIMER = 0 //视频时间计时中
        const val PAUSE = 1 //视频播放停止
        const val STOP = 2 //视频播放停止
    }

    constructor(context: Context) : super(context){
        initView()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        initView()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initView()
    }

    private fun initView(){
        View.inflate(context,R.layout.ad_layout_rewarded,this)
        reward_down_chronometer.setOnChronometerTickListener {
            countTime -= 1
            it.text = countTime.toString()
            if (countTime <= 0){
                it.stop()
                reward_skip.visibility = View.VISIBLE
            }
            rewardBean?.let { bean ->
                if(bean.adTime - countTime >= bean.adEndTime || bean.adTime - countTime <= 0){
                    reward_skip.visibility = View.VISIBLE
                }
            }
        }
        reward_download_app.setOnClickListener(this::onClick)
        reward_skip.setOnClickListener(this::onClick)
        initVideoListener()
    }

    private fun initVideoListener(){
        videoDuration = 0
        reward_video.setOnCompletionListener {
            myHandler.sendEmptyMessage(STOP)
            rewardListener?.onComplete()
        }
        reward_video.setOnErrorListener { _, _, _ ->
            Log.e("YM","播放监听--错误---->")
            rewardListener?.onError()
            true
        }
        reward_video.setOnPreparedListener {
            reward_video.start()
            if (videoCurPosition > 0){
                it.seekTo(videoCurPosition)
            }else{
                videoStart()
            }
            preview.postDelayed({
                preview.visibility = View.GONE
            },150)
//            reward_video.visibility = View.VISIBLE
            videoDuration = reward_video.duration
            myHandler.sendEmptyMessage(TIMER)
            it.setOnSeekCompleteListener {
//                reward_video.start()
            }
        }

    }

    private fun onClick(view: View){
        when(view){
            reward_download_app -> {
                Toast.makeText(context,"开始下载",Toast.LENGTH_LONG).show()
                rewardBean?.let {
                    downLoadListener?.onDownloadStart()
                    AndroidDownloadManagerUtils(context, it.downUrl, it.appName.plus(".apk")).setOnDownLoadListener(downLoadListener)
                }
            }
            reward_skip -> {
                rewardListener?.adClose()
                if (countTime <= 0){
                    Log.e("YM","视频播放完毕")
                }else{
                    Log.e("YM","视频没有播放完毕")
                }
            }
        }
    }

    fun loadVideo(path :String){
        val uri: Uri = Uri.parse(path)
        Glide.with(context)
                .load(uri )
                .into(preview);
        preview.visibility = View.VISIBLE
        reward_video.setVideoURI(uri)
//        reward_video.start()
    }

//    fun showAd(){
//        reward_video.start()
//    }

    fun loadAd(reward : RewardBean){
        this.rewardBean = reward
        Log.e("YM","显示的广告内容:${reward.toString()}")
        val adType = reward.adType[0]
        countTime = reward.adTime
        reward_down_chronometer.start()
        reward_app_name.text = reward.appName
        reward_comment_count.text = "${reward.adMany}人评价"
        reward_rating.rating = reward.adKPS
        GlideUtil.loadNormalImgByNet(context,reward.adLogo,reward_app_icon)
        downloadVideo(reward.adUrl)
    }

    /**
     * 下载视频
     */
    private fun downloadVideo(mediaUrl :String) {

        val uri = fileIsExists(mediaUrl)

        if (null != uri){
//            loadVideo(uri.toString())
            rewardListener?.cached(uri.toString())
            return
        }

        HttpDownFileUtils.getInstance().downFileFromServiceToPublicDir(mediaUrl, context, Environment.DIRECTORY_MOVIES) { status, obj, proGress, currentDownProGress, totalProGress ->
            if (status == 1) {
                var localPath = "" //10.0之上是uri，10.0之下是本地路径
                if (obj is File) {
                    localPath = obj.absolutePath
                } else if (obj is Uri) {
                    localPath = obj.toString()
                }
//                loadVideo(localPath)
                rewardListener?.cached(localPath)
            }
        }
    }

    /**
     * 文件是否存在
     */
    private fun fileIsExists(downloadUrl :String):Uri?{
        val fileName: String = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1)
        val videoList = FileManager.getInstance(context).videos
        val video = videoList.find {
            it.name == fileName
        }
        return if (null != video){
            ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, video.id)
        }else{
            null
        }
    }

    fun setOnDqVideoListener(rewardListener : DqAdVideoListener){
        this.rewardListener = rewardListener
    }

    fun setOnDownloadAppListener(downLoadListener :DqAppDownloadListener){
        this.downLoadListener = downLoadListener
    }

    /**
     * 视频暂停
     */
    private fun videoPause(){
        videoCurPosition = reward_video.currentPosition
        if (reward_video.isPlaying){
            reward_video.pause()
            videoCurPosition = reward_video.currentPosition
            Log.e("YM","当前进度:${videoCurPosition}")
        }
    }

    private fun videoStart(){
        reward_video.start()
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        if (visibility == View.VISIBLE){
//            videoStart()
        }
        else if(visibility == INVISIBLE || visibility == GONE){
//            videoPause()
            myHandler.sendEmptyMessage(PAUSE)
            myHandler.removeMessages(TIMER)
        }
    }

    /**
     * 检测是否被遮住显示不全
     * @return
     */
    fun isCover(): Boolean {
        var cover = false
        val rect = Rect()
        cover = getGlobalVisibleRect(rect)
        if (cover) {
            if (rect.width() >= measuredWidth && rect.height() >= measuredHeight) {
                return !cover
            }
        }
        return true
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        myHandler.sendEmptyMessage(STOP)
        Log.e("YM","------->View销毁监听")
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.e("YM","------->按键监听:${keyCode}")
        if (keyCode == KeyEvent.KEYCODE_BACK){
            return false
        }
        return super.onKeyUp(keyCode, event)
    }
    /**
     * 自定义下载进度
     */
    inner class CustomDownLoadListener(var rewardBean :RewardBean) : DqAppDownloadListener{
        override fun onDownloadSuccess() {
            Log.e("YM","------->下载成功")
        }

        override fun onDownloadFail() {
            Log.e("YM","------->下载失败")
        }

        override fun onDownloading() {
            Log.e("YM","------->下载过程中")
        }

        override fun onDownloadStart() {
        }
    }

}