package com.dq.im.bean.im;

import com.dq.im.model.IMContentDataModel;

/**
 * IM的视频数据模型
 */
public class MessageVideoBean extends IMContentDataModel {
    private String description;
    private String videoPath;//视频网络路径
    private String thumbPath;//缩略图网络路径
    private String videoLocalPath;//视频本地路径
    private String thumbLocalPath;//缩略图本地路径
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getVideoLocalPath() {
        return videoLocalPath;
    }

    public void setVideoLocalPath(String videoLocalPath) {
        this.videoLocalPath = videoLocalPath;
    }

    public String getThumbLocalPath() {
        return thumbLocalPath;
    }

    public void setThumbLocalPath(String thumbLocalPath) {
        this.thumbLocalPath = thumbLocalPath;
    }

    @Override
    public String toString() {
        return "MessageVideoBean{" +
                "description='" + description + '\'' +
                ", videoPath='" + videoPath + '\'' +
                ", thumbPath='" + thumbPath + '\'' +
                ", videoLocalPath='" + videoLocalPath + '\'' +
                ", thumbLocalPath='" + thumbLocalPath + '\'' +
                '}';
    }
}