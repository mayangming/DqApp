package com.wd.daquan.glide.listener;

import android.graphics.Bitmap;

/**
 * @Author: 方志
 * @Time: 2018/8/20 9:26
 * @Description: 图片加载进度监听
 */
public interface IProgressListener {

    /**
     *  @param progress 进度
     * @param isDone 是否完成
     */
    void onProgress(int progress, boolean isDone, Bitmap bitmap);
}