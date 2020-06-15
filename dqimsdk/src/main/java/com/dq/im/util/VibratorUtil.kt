package com.dq.im.util

import android.media.AudioAttributes
import android.os.Build
import android.os.Vibrator
import android.util.Log


/**
 * author：xiaofei_dev
 * time：2017/5/15:8:47
 * e-mail：xiaofei.dev@gmail.com
 * desc：coding
 */
//此类用于描述振动属性，即Vibrator类的方法参数值
class VibratorUtil(private val mVibrator: Vibrator) {
    companion object {
        private val TAG = "VibratorUtil"
        //振动模式为断续
        val INTERRUPT = 0
        //振动模式为持续
        val KEEP = 1
        var mPattern = longArrayOf(0, 0, 0)
    }

    //通过设置一个小时时长来模拟持续不停地震动
    private var mDuration = (1000 * 60 * 60).toLong()
    private val mPatternKeep = longArrayOf(1, (1000 * 10).toLong(), 1, (1000 * 10).toLong())
    var isVibrate: Boolean = false
        private set

    private val mAudioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ALARM) //key
            .build()

    fun setDuration(duration: Long) {
        mDuration = duration
    }

    //开始震动
    fun vibrate(mode: Int) {
        Log.d(TAG, "vibrate:0 ")
        isVibrate = true
        when (mode) {
            INTERRUPT -> {
                if (Build.VERSION.SDK_INT >= 21){
                    //适配在高版本系统上无法后台震动的问题
                    mVibrator.vibrate(mPattern, -1, mAudioAttributes)
                }else{
                    mVibrator.vibrate(mPattern, -1)
                }
                Log.d(TAG, "vibrate:0 ")
            }
            KEEP ->
                if (Build.VERSION.SDK_INT >= 21){
                    //适配在高版本系统上无法后台震动的问题
                    mVibrator.vibrate(mPatternKeep, -1, mAudioAttributes)
                }else{
                    mVibrator.vibrate(mPatternKeep, -1)//-1是不循环，如果是0则从0开始
                }
            else -> {
            }
        }
    }

    fun stopVibrate() {
        isVibrate = false
        mVibrator.cancel()
    }

    public fun setVibratePattern(duration: Int) {
//        long[] patter = {1000, 1000, 2000, 50};数组的a[0]表示静止的时间，a[1]代表的是震动的时间，然后数组的a[2]表示静止的时间，a[3]代表的是震动的时间
        mPattern[1] = (duration * 1).toLong()
        mPattern[2] = (duration * 4).toLong()
    }

}