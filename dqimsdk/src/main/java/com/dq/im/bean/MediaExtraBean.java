package com.dq.im.bean;

import android.net.Uri;

public class MediaExtraBean {
    private Uri thumbPath;//缩略图路径
    private String localPath = "";//路径
    private String title  = "";//视频名称
    private int duration;//视频时长
    private long size;//视频大小
    private String imagePath;//视频缩略图路径

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Uri getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(Uri thumbPath) {
        this.thumbPath = thumbPath;
    }

    @Override
    public String toString() {
        return "MediaExtraBean{" +
                "thumbPath=" + thumbPath +
                ", localPath='" + localPath + '\'' +
                ", title='" + title + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}