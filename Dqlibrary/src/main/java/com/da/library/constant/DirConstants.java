package com.da.library.constant;

import android.os.Environment;

public class DirConstants {
    //APP目录
    public static final String DIR_WORK = Environment.getExternalStorageDirectory() + "/douquan/";
    //聊天背景 图片缓存目录
    public static final String DIR_CHAT_BG = DIR_WORK + "chatBg/";
    //error目录
    public static final String DIR_ERROR = DIR_WORK + "error/";
    //ImageLoader 图片缓存目录
    public static final String DIR_IMAGES = DIR_WORK + "images/";

    //拍照 图片缓存目录
    public static final String DIR_CAMERA= DIR_WORK + "camera/";

    // 所有视频 录制
    public static final String DIR_VIDEOS = DIR_WORK + "videos/";

    //二维码图片保存目录
    public static final String DIR_ERWEIMA = Environment.getExternalStorageDirectory() + "/erweima/";
    //表情 图片缓存目录
    public static final String DIR_EMOTION= DIR_WORK + "emotion/";

    //拍照 图片缓存目录
    public static final String DIR_SHARE= DIR_WORK + "sharecn/";
    //表情
    public static final String DIR_ADD_EMOTION= DIR_WORK + "addemotion/";
    //表情gif
    public static final String DIR_ADD_GIF_EMOTION= DIR_WORK + "addgifemotion/";
    //图片分享路径
    public static final String DIR_T_SHARE= DIR_WORK + "tshare/";
    //文件缓存目录
    public static final String DIR_FILE = DIR_WORK + "files/";
    //收藏缓存目录
    public static final String DIR_COLLECTION = DIR_WORK + "qccollection";
    // 录制视频拍照目录
    public static final String DIR_RECORED = DIR_WORK + "cnrecored/";
    //相册直接分享到斗圈
    public static final String DIR_PHOTOS= DIR_WORK + "photos/";
    //错误文件路径
    public static final String DIR_ERROR_LOG= DIR_WORK + "errorlog";
    //语言
    public static final String DIR_VOICES = DIR_WORK + "errorlog";
}
