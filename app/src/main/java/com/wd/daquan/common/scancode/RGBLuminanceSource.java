/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wd.daquan.common.scancode;

import android.graphics.Bitmap;

import com.google.zxing.LuminanceSource;

/**
 * This object extends LuminanceSource around an array of YUV intentUrl returned from the camera driver,
 * with the option to crop to a rectangle within the full intentUrl. This can be used to exclude
 * superfluous pixels around the perimeter and speed up decoding.
 *
 * It works for any pixel format where the Y channel is planar and appears first, including
 * YCbCr_420_SP and YCbCr_422_SP.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class RGBLuminanceSource extends LuminanceSource {
  private byte bitmapPixels[];

  protected RGBLuminanceSource(Bitmap bitmap) {
    super(bitmap.getWidth(), bitmap.getHeight());
    // 首先，要取得该图片的像素数组内容
    int[] data = new int[bitmap.getWidth() * bitmap.getHeight()];
    this.bitmapPixels = new byte[bitmap.getWidth() * bitmap.getHeight()];
    bitmap.getPixels(data, 0, getWidth(), 0, 0, getWidth(), getHeight());

    // 将int数组转换为byte数组
    for (int i = 0; i < data.length; i++) {
      this.bitmapPixels[i] = (byte) data[i];
    }
  }

  @Override
  public byte[] getMatrix() {
    // 返回我们生成好的像素数据
    return bitmapPixels;
  }

  @Override
  public byte[] getRow(int y, byte[] row) {
    // 这里要得到指定行的像素数据
    System.arraycopy(bitmapPixels, y * getWidth(), row, 0, getWidth());
    return row;
  }
}
