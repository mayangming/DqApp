package com.wd.daquan.glide.listener;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.GlideException;

/**
 * @Author: 方志
 * @Time: 2018/8/20 9:26
 * @Description: 加载Drawable监听
 */
public interface ILoadDrawableListener {
    void onResourceReady(@NonNull Drawable drawable);
    void onLoadFailed(@NonNull GlideException e);
}
