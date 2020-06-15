package com.dq.im.bean.im;

import android.net.Uri;

import com.dq.im.model.IMContentDataModel;

/**
 * IM的音频数据模型
 * 音频数据模型和图片视频模型存储内容不一样，图片、视频主要存储在共享文件里面，
 * 音频是存储在本地缓存文件夹里面，该文件不对外公开，所以路径读取方式不一样
 *
 * Android7.0版本后 Uri和文件路径互相转换封装类，实现系统分享功能及 FileProvider：
 * https://www.csdn.net/gather_26/MtTakg5sMDA2Ny1ibG9n.html
 *
 * Android中的Uri详解：
 * https://blog.csdn.net/sinat_37205087/article/details/102815247
 */
public class MessageVoiceBean extends IMContentDataModel {
    private String description;//音频网络路径
    private transient Uri photoUri;//音频的uri地址
    private String localUriString;//音频的本地Uri字符串路径
    private long duration;//音频的播放时长
    private int readStatus = 0;//消息阅读状态 0 未读，1已读
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

    public String getLocalUriString() {
        return localUriString;
    }

    public void setLocalUriString(String localUriString) {
        this.localUriString = localUriString;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }

    @Override
    public String toString() {
        return "MessageVoiceBean{" +
                "description='" + description + '\'' +
                ", photoUri=" + photoUri +
                ", localUriString='" + localUriString + '\'' +
                ", duration=" + duration +
                ", readStatus=" + readStatus +
                '}';
    }
}