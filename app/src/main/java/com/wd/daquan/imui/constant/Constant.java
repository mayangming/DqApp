package com.wd.daquan.imui.constant;

import com.wd.daquan.DqApp;

import java.io.File;

/**
 * 常量字段
 */
public class Constant {
    public static final String ROOT_DIR = File.separator + "dqsdk";
    public static final String AUDIO_RECORD_DIR = File.separator + "audio_record"+File.separator;

    /**
     * 获取录音的文件夹路径
     */
    public static String getAudioRecordDir(){
        return DqApp.sContext.getExternalCacheDir() + ROOT_DIR + AUDIO_RECORD_DIR;
    }
}