package com.wd.daquan.common.scancode;

import android.graphics.Bitmap;

import com.google.zxing.Result;

/**
 * @author: dukangkang
 * @date: 2018/4/28 13:51.
 * @description: todo ...
 */
public interface ScanCodeListener {

    public void invalidate();

    public void decodeSucceeded(Result result, Bitmap bitmap);

}
