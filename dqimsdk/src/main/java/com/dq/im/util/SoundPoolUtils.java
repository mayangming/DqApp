package com.dq.im.util;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

import com.dq.im.R;

/**
 * 提示音播放工具类
 * */
public class SoundPoolUtils implements SoundPool.OnLoadCompleteListener {
    private static final int DEFAULT_INVALID_SOUND_ID = -Integer.MAX_VALUE;
    private static final int DEFAULT_INVALID_STREAM_ID = -Integer.MAX_VALUE;

    private int mSoundId = DEFAULT_INVALID_SOUND_ID;
    private int mStreamID = DEFAULT_INVALID_STREAM_ID;
    private float mCruLeftVolume = 1.0f;
    private float mCurRightVolume = 1.0f; // 用于设置sound pool的左右volume值 0~1f
    private SoundPool mSoundPool;
    private static SoundPoolUtils soundPoolUtils;
    private Context context;
    private SoundPoolUtils(Context context) {
        this.context = context;
    }

    public static SoundPoolUtils getInstance(Context context){
        if (null == soundPoolUtils){
            soundPoolUtils = new SoundPoolUtils(context);
        }
        return soundPoolUtils;
    }
    /**
     * 播放:如果资源还没有加载,则可能会有一小段等待时间
     */
    public void playMayWait() {
        releaseSoundPool();
        createSoundPoolIfNeeded();
        if (mSoundId == DEFAULT_INVALID_SOUND_ID) {  // mSoundId is invalid ,load from res raw for once before mSoundPool is released
            mSoundId = mSoundPool.load(context, R.raw.sdk_notification, 1); // 加载音频资源
        } else {
            // reuse the loaded res
            if (mStreamID == DEFAULT_INVALID_STREAM_ID)
                onLoadComplete(mSoundPool, 0, 0);  // manually call this method when there is a valid mSoundId
        }
    }
    /**
     * 创建SoundPool
     */
    private void createSoundPoolIfNeeded() {
        if (mSoundPool == null) {
            // 5.0 及 之后
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes audioAttributes = null;
                audioAttributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build();

                mSoundPool = new SoundPool.Builder()
                        .setMaxStreams(16)
                        .setAudioAttributes(audioAttributes)
                        .build();
            } else { // 5.0 以前
                mSoundPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 0);  // 创建SoundPool
            }

            mSoundPool.setOnLoadCompleteListener(this);  // 设置加载完成监听
        }
    }

    /**
     * 释放资源
     */
    public void releaseSoundPool() {
        if (mSoundPool != null) {
            mSoundPool.autoPause();
            mSoundPool.unload(mSoundId);
            mSoundId = DEFAULT_INVALID_SOUND_ID;
            mStreamID = DEFAULT_INVALID_STREAM_ID;
            mSoundPool.release();
            mSoundPool = null;
        }
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

        if (mSoundPool != null) {
            if (mStreamID == DEFAULT_INVALID_STREAM_ID)
                mStreamID = mSoundPool.play(mSoundId, mCruLeftVolume, mCurRightVolume, 16, 0, 1.0f);//loop: 0不循环，-1，循环 其他值是循环次数
        }
    }
}