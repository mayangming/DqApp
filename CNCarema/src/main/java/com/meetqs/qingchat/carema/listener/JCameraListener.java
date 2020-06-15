package com.meetqs.qingchat.carema.listener;

import android.graphics.Bitmap;

/**
 * =====================================
 * 作    者: 陈嘉桐
 * 版    本：1.1.4
 * 创建日期：2017/4/26
 * 描    述：
 * =====================================
 */
public interface JCameraListener {

    /**
     * 拍照完成
     * @param bitmap 拍照图片
     */
    void captureSuccess(Bitmap bitmap);

    /**
     * 录制完成
     * @param url
     *  录制视频本地地址
     * @param firstFrame
     *  录制视频第一帧，目前没有使用
     * @param duration
     *  录制视频的时长
     */
    /**
     * 录制完成
     * @param url
     *  录制视频本地地址
     * @param firstFrame
     *  录制视频第一帧，目前没有使用
     * @param duration
     *  录制视频的时长
     * @param rotate
     *  录制视频当前角度
     */
    void recordSuccess(String url, Bitmap firstFrame, int duration, int rotate);

    /**
     * 关闭页面
     */
    void caremaFinish();
}
