package com.wd.daquan.imui.bean;

import java.io.Serializable;

/**
 * 音频时长和路径
 */

public
class VoiceBean implements Serializable {
    public String localString;//本地路径
    public long voiceDuration;//音频时长

    @Override
    public String toString() {
        return "VoiceBean{" +
                "localString='" + localString + '\'' +
                ", voiceDuration=" + voiceDuration +
                '}';
    }
}