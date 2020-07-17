package com.dq.im.constants;

public class Constants {
    public static final String VIDEO = "dq_video_";
    public static final String VOICE = "dq_voice_";
    public static final String IMG = "dq_img_";


    /**
     * 获取音频的名字
     */
    public static String getVoiceName(){
        return VOICE.concat(String.valueOf(System.currentTimeMillis()));
    }
    /**
     * 获取视频的名字
     */
    public static String getVideoName(){
        return VIDEO.concat(String.valueOf(System.currentTimeMillis()));
    }

    /**
     * 获取图片的名字
     */
    public static String getImgName(){
        return IMG.concat(String.valueOf(System.currentTimeMillis()));
    }
}