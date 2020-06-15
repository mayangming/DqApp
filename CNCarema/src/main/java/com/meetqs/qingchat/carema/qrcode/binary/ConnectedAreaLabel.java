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
package com.meetqs.qingchat.carema.qrcode.binary;



import com.meetqs.qingchat.carema.qrcode.ByteProcessor;
import com.meetqs.qingchat.carema.qrcode.datamodel.Rect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * It is very easy way to filter some minimum noise block by number of pixel;
 * default settings: <p>
 * - mNumOfPixels = 100 <br>
 * - mFilterNoise = false
 */
public class ConnectedAreaLabel {

    /**
     * default number of pixels
     */
    private static final int DEFAULT_PIXEL_NUM = 100;
    private static final boolean DEFAULT_FILTER_NOISE = false;

    private int mNumOfPixels;
    private boolean mFilterNoise;

    public ConnectedAreaLabel() {
        mNumOfPixels = DEFAULT_PIXEL_NUM;
        mFilterNoise = DEFAULT_FILTER_NOISE;
    }

    /**
     * init object with number of pixels and whether filter noise
     * @param numOfPixels the number of pixels, default value is 100
     * @param filterNoise whether to filter the noise of picture
     */
    public ConnectedAreaLabel(int numOfPixels, Boolean filterNoise) {
        mNumOfPixels = numOfPixels;
        mFilterNoise = filterNoise;
    }

    public void setNoiseArea(int numOfPixels) {
        this.mNumOfPixels = numOfPixels;
    }

    public void setFilterNoise(boolean filterNoise) {
        this.mFilterNoise = filterNoise;
    }

    /**
     * @param binary    - binary image data
     * @param labelMask - label for each pixel point
     * @return int - total labels of image
     */
    public int process(ByteProcessor binary, int[] labelMask) {
        return this._process(binary, labelMask, null, false);
    }

    /**
     * process noise block with labels
     * @param binary - binary image data
     * @param labelMask - label for each pixel point
     * @param rectangles - rectangles area list want to return
     * @param drawBounding - whether draw bounding
     * @return int - total labels of image
     */
    public int process(ByteProcessor binary, int[] labelMask, List<Rect> rectangles,
                       boolean drawBounding) {
        return this._process(binary,labelMask,rectangles,drawBounding);
    }

    private int _process(ByteProcessor binary, int[] labelMask, List<Rect> rectangles,
                         boolean drawBounding) {
        int width = binary.getWidth();
        int height = binary.getHeight();
        byte[] data = binary.getGray();
        int p1, p2, p3;
        int yMin = 1;
        int xMin = 1;
        int offset = 0;
        int[] labels = new int[(width * height) / 2];
        Arrays.fill(labels, -1);
        int[] pixels = new int[width * height];
        Arrays.fill(pixels, -1);
        int ul = -1;
        int ll = -1;
        int currlabel = 0;
        int[] twoLabels = new int[2];
        for (int row = yMin; row < height; row++) {
            offset = row * width + xMin;
            for (int col = xMin; col < width; col++) {
                p1 = data[offset] & 0xff;
                p2 = data[offset - 1] & 0xff; // left
                p3 = data[offset - width] & 0xff; // upper
                Arrays.fill(twoLabels, -1);
                ll = -1;
                ul = -1;
                if (p1 == 255) {
                    if (p1 == p2) {
                        ll = pixels[offset - 1] < 0 ? -1 : labels[pixels[offset - 1]];
                        twoLabels[0] = ll;
                    }
                    if (p1 == p3) {
                        ul = pixels[offset - width] < 0 ? -1 : labels[pixels[offset - width]];
                        twoLabels[1] = ul;
                    }

                    if (ll < 0 && ul < 0) {
                        pixels[offset] = currlabel;
                        labels[currlabel] = currlabel;
                        currlabel++;
                    } else {
                        Arrays.sort(twoLabels);
                        int smallestLabel = twoLabels[0];
                        if (twoLabels[0] < 0) {
                            smallestLabel = twoLabels[1];
                        }
                        pixels[offset] = smallestLabel;

                        for (int k = 0; k < twoLabels.length; k++) {
                            if (twoLabels[k] < 0) {
                                continue;
                            }
                            int tempLabel = twoLabels[k];
                            int oldSmallestLabe = labels[tempLabel];
                            if (oldSmallestLabe > smallestLabel) {
                                labels[oldSmallestLabe] = smallestLabel;
                                labels[tempLabel] = smallestLabel;
                            } else if (oldSmallestLabe < smallestLabel) {
                                labels[smallestLabel] = oldSmallestLabe;
                            }
                        }
                    }
                }
                offset++;
            }
        }

        int[] labelSet = new int[currlabel];
        System.arraycopy(labels, 0, labelSet, 0, currlabel);
        labels = null;
        for (int i = 2; i < labelSet.length; i++) {
            int curLabel = labelSet[i];
            int preLabel = labelSet[curLabel];
            while (preLabel != curLabel) {
                curLabel = preLabel;
                preLabel = labelSet[preLabel];
            }
            labelSet[i] = curLabel;
        }

        // 2. second pass
        // aggregation the pixels with same label index
        Map<Integer, List<PixelNode>> aggregationMap = new HashMap<Integer, List<PixelNode>>();
        for (int i = 0; i < height; i++) {
            offset = i * width;
            List<PixelNode> pixelList = null;
            PixelNode pn = null;
            for (int j = 0; j < width; j++) {
                int pixelLabel = pixels[offset + j];
                // skip background
                if (pixelLabel < 0) {
                    continue;
                }
                // label each area
                pixels[offset + j] = labelSet[pixelLabel];
                pixelList = aggregationMap.get(labelSet[pixelLabel]);
                if (pixelList == null) {
                    pixelList = new ArrayList<PixelNode>();
                    aggregationMap.put(labelSet[pixelLabel], pixelList);
                }
                pn = new PixelNode();
                pn.row = i;
                pn.col = j;
                pn.index = offset + j;
                pixelList.add(pn);
            }
        }

        // assign labels
        Integer[] keys = aggregationMap.keySet().toArray(new Integer[0]);
        Arrays.fill(labelMask, -1);
        List<PixelNode> pixelList = null;
        int number = 0;
        for (Integer key : keys) {
            pixelList = aggregationMap.get(key);
            if (mFilterNoise && pixelList.size() < mNumOfPixels) {
                continue;
            }
            // tag each pixel
            for (PixelNode pnode : pixelList) {
                labelMask[pnode.index] = key;
            }

            // return each label rectangle
            if (drawBounding && rectangles != null) {
                Rect bounding = boundingRect(pixelList);
                bounding.labelIdx = key;
                rectangles.add(bounding);
            }
            number++;
        }

        return number;
    }

    private Rect boundingRect(List<PixelNode> pixelList) {
        int minX = 10000, maxX = 0;
        int minY = 10000, maxY = 0;
        for (PixelNode pn : pixelList) {
            minX = Math.min(pn.col, minX);
            maxX = Math.max(pn.col, maxX);
            minY = Math.min(pn.row, minY);
            maxY = Math.max(pn.row, maxY);
        }
        int dx = maxX - minX;
        int dy = maxY - minY;
        Rect roi = new Rect();
        roi.x = minX;
        roi.y = minY;
        roi.width = dx;
        roi.height = dy;
        return roi;
    }
}
