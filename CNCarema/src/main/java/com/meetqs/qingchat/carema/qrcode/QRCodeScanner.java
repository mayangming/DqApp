/*
 * Copyright (c) 2017 - present, CV4J Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.meetqs.qingchat.carema.qrcode;

import android.util.Log;


import com.meetqs.qingchat.carema.qrcode.binary.ConnectedAreaLabel;
import com.meetqs.qingchat.carema.qrcode.binary.MorphOpen;
import com.meetqs.qingchat.carema.qrcode.binary.Threshold;
import com.meetqs.qingchat.carema.qrcode.datamodel.Rect;
import com.meetqs.qingchat.carema.qrcode.datamodel.Size;

import java.util.ArrayList;
import java.util.List;

public class QRCodeScanner {

    public Rect findQRCodeBounding(ImageProcessor image, int n1, int n2) {

        Rect rect = new Rect();
        image = image.getImage().convert2Gray().getProcessor();
        ByteProcessor src = ((ByteProcessor)image);
        int width = src.getWidth();
        int height = src.getHeight();
        Threshold t = new Threshold();
        t.process(src, Threshold.THRESH_OTSU, Threshold.METHOD_THRESH_BINARY_INV, 20);
        MorphOpen mOpen = new MorphOpen();
        byte[] data = new byte[width*height];
        System.arraycopy(src.getGray(), 0, data, 0, data.length);
        ByteProcessor copy = new ByteProcessor(data, width, height);
        mOpen.process(src, new Size(n1, n2)); // Y方向开操作
        src.getImage().resetBitmap();

        mOpen.process(copy, new Size(n2, n1)); // X方向开操作
        CV4JImage cv4JImage = new CV4JImage(width,height);
        ((ByteProcessor)cv4JImage.getProcessor()).putGray(copy.getGray());

        for(int i=0; i<data.length; i++) {
            int pv = src.getGray()[i]&0xff;
            if(pv == 255) {
                copy.getGray()[i] = (byte)255;
            }
        }
        src.putGray(copy.getGray());

        // 联通组件查找连接区域
        ConnectedAreaLabel ccal = new ConnectedAreaLabel();
        ccal.setFilterNoise(true);
        List<Rect> rectList = new ArrayList<>();
        int[] labelMask = new int[width*height];
        ccal.process(src, labelMask, rectList, true);
        float w = 0;
        float h = 0;
        float rate = 0;
        List<Rect> qrRects = new ArrayList<>();
        for(Rect roi : rectList) {

            if (roi == null) continue;

            if ((roi.width > width / 4 || roi.width < 10) || (roi.height < 10 || roi.height > height / 4)) {
//                Log.w("QRCode Finder", "rect: " + roi.width + " x " + roi.height);
                continue;
            }

            if ((roi.x < 10 || roi.x > width - 10) || (roi.y < 10 || roi.y > height - 10))
                continue;

            w = roi.width;
            h = roi.height;
            rate = (float) Math.abs(w / h - 1.0);
            if (rate < 0.05 && isRect(roi, labelMask, width, height, true)) {
                qrRects.add(roi);
            }
        }

        // find RQ code bounding
        Rect[] blocks = qrRects.toArray(new Rect[0]);
        Log.w("QRCode Finder", "blocks.length : " + blocks.length);

        if (Preconditions.isBlank(blocks)) {

            for(Rect roi : rectList) {

                if (roi == null) continue;

                if((roi.width > width/4 || roi .width < 10) || (roi.height < 10 || roi.height > height/4))
                    continue;

                if((roi.x < 10 || roi.x > width -10)|| (roi.y < 10 || roi.y > height-10))
                    continue;

                w = roi.width;
                h = roi.height;
                rate = (float)Math.abs(w / h  - 1.0);
                if(rate < 0.05 && isRect(roi, labelMask, width, height,false)) {
                    qrRects.add(roi);
                }
            }

            // find RQ code bounding
            blocks = qrRects.toArray(new Rect[0]);
            Log.w("QRCode Finder", "blocks.length : " + blocks.length);
        }

//        // 二维码很小的情况
//        if (blocks.length == 1) {
//            rect.width = 0;
//            rect.height = 0;
//        } else if (blocks.length == 1) {
////            rect.x = blocks[0].x-5;
////            rect.y = blocks[0].y- 5;
////            rect.width= blocks[0].width + 10;
////            rect.height = blocks[0].height + 10;
//
//            Log.w("QRCode Finder", "blocks.x,y : " + blocks[0].x + " x " + blocks[0].y + "   width x height: " + blocks[0].width + " x " + blocks[0].height);
//            rect.x = 20;
//            rect.y = 20;
//            rect.width = width - 40;
//            rect.height = height - 40;
//        } else if (blocks.length == 6 || blocks.length == 3 || blocks.length == 4 || blocks.length == 5 || blocks.length == 2) {
//            for (int i = 0; i < blocks.length-1; i++) {
//                for (int j = i + 1; j < blocks.length; j++) {
//                    int idx1 = blocks[i].tl().y*width + blocks[i].tl().x;
//                    int idx2 = blocks[j].tl().y*width + blocks[j].tl().x;
//                    if (idx2 < idx1){
//                        Rect temp = blocks[i];
//                        blocks[i] = blocks[j];
//                        blocks[j] = temp;
//                    }
//                }
//            }
//            rect.x = blocks[0].x - 5;
//            rect.y = blocks[0].y - 5;
//            rect.height = rect.width = blocks[1].width + (blocks[1].x - blocks[0].x) + 10;
////            if(blocks.length == 3) {
////                rect.height = (blocks[2].height + blocks[2].y - blocks[0].y) + 10;
////            } else if (blocks.length == 4) {
////                rect.height = (blocks[3].height + blocks[3].y - blocks[0].y) + 10;
////            } else {
////                rect.height = (blocks[4].height + blocks[4].y - blocks[0].y) + 10;
////            }
////
////            rect.width = rect.height = Math.max(rect.width, rect.height);
//        } else {
//            rect.width = 0;
//            rect.height = 0;
//        }


        // 二维码很小的情况
        if (blocks.length <= 0) {
            rect.width = 0;
            rect.height = 0;
        } else if (blocks.length == 1) {
            rect.x = 20;
            rect.y = 20;
            rect.width = width - 40;
            rect.height = height - 40;
        } else {
            for (int i = 0; i < blocks.length-1; i++) {
                for (int j = i + 1; j < blocks.length; j++) {
                    int idx1 = blocks[i].tl().y*width + blocks[i].tl().x;
                    int idx2 = blocks[j].tl().y*width + blocks[j].tl().x;
                    if (idx2 < idx1){
                        Rect temp = blocks[i];
                        blocks[i] = blocks[j];
                        blocks[j] = temp;
                    }
                }
            }
            rect.x = blocks[0].x - 5;
            rect.y = blocks[0].y - 5;
            rect.height = rect.width = Math.abs(blocks[1].width + (blocks[1].x - blocks[0].x) + 10);
        }
        return rect;
    }

    private boolean isRect(Rect roi, int[] labelMask, int w, int h, boolean useRate) {
        int ox = roi.x;
        int oy = roi.y;
        int width = roi.width;
        int height = roi.height;

        byte[] image = new byte[width*height];
        int label = roi.labelIdx;
        float bcount = 0, wcount = 0;
        for(int row=oy; row<(oy + height); row++) {
            for(int col=ox; col<(ox + width); col++) {
                int v = labelMask[row*w + col];
                if(v == label) {
                    image[(row - oy) * width + col - ox] = (byte)255;
                    wcount++;
                } else {
                    bcount++;
                }
            }
        }


        int cx = width / 2;
        int offset = 0;
        if (width % 2 > 0) {
            offset = 1;
        }

        int v1=0, v2=0;
        float[] data = new float[cx *height];
        for(int row=0; row<height; row++) {
            for(int col=0; col<cx; col++) {
                v1 = image[row*width+ col]&0xff;
                v2 = image[row*width+(width-1-col)]&0xff;
                data[row*cx+col] = Math.abs(v1-v2);
            }
        }

        float[] mdev = Tools.calcMeansAndDev(data);
        Log.i("QRCodeScanner","mdev[0]="+mdev[0]);
        Log.i("QRCodeScanner","mdev[1]="+mdev[1]);

        if (useRate) {
            // 黑色跟白色的像素数目比
            float rate = Math.min(bcount, wcount)/Math.max(bcount, wcount);

            return mdev[0] <= 20 && rate > 0.50f;
        } else {

            return mdev[0] <= 20;
        }
    }
}
