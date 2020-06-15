package com.meetqs.qingchat.carema.listener;

import android.view.SurfaceHolder;

/**
 * @author: dukangkang
 * @date: 2018/6/28 14:15.
 * @description: todo ...
 */
public interface SurfaceCallback {
    public void surfaceCreated(SurfaceHolder holder);

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height);

    public void surfaceDestroyed(SurfaceHolder holder);
}
