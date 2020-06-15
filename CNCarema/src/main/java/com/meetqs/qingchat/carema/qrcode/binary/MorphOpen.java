/*
 * Copyright (c) 2017-present, CV4J Contributors.
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
import com.meetqs.qingchat.carema.qrcode.datamodel.Size;

public class MorphOpen {

    /**
     * in order to remove litter noise block, erode + dilate operator
     *
     * @param binary
     * @param structureElement
     */
    public void process(ByteProcessor binary, Size structureElement) {
        FastErode erode = new FastErode();
        FastDilate dilate = new FastDilate();
        erode.process(binary, structureElement, 1);
        dilate.process(binary, structureElement, 1);
    }
}
