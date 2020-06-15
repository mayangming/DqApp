package com.wd.daquan.glide.helper;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * @Author: 方志
 * @Time: 2019/5/7 10:11
 * @Description:
 */
public class ImageHelp {

    /**
     * 图片旋转角度
     * @param bm bitmap
     * @param orientationDegree 角度  1为竖屏 0为横屏
     * @return Bitmap
     */
    public static Bitmap rotation(Bitmap bm, int orientationDegree) {
        Matrix m = new Matrix();
        int width = 200;
        int height = 200;

        int w = bm.getWidth();
        int h = bm.getHeight();
        if(orientationDegree == 1) {//竖屏
            m.setRotate(90);
        }else if(orientationDegree == 0){//横屏
            m.setRotate(0);
        }else{
            m.setRotate(90);
        }

        if(w != 0 && width > w) {
            width = w;
        }
        if(h != 0 && height > h) {
            height = h;
        }
        return Bitmap.createBitmap(bm, 0, 0, width, height, m, true);
    }
}
