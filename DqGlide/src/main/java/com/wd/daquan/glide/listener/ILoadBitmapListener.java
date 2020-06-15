package com.wd.daquan.glide.listener;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;


/**
 * @Author: 方志
 * @Time: 2018/8/20 9:27
 * @Description: 加载BitMap监听器
 */
public interface ILoadBitmapListener {
    void onResourceReady(@NonNull Bitmap bitmap);
}
