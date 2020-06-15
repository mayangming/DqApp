package com.dq.im.bean.im;

import android.net.Uri;

import com.dq.im.model.IMContentDataModel;

/**
 * 图片模型
 */
public class MessagePhotoBean extends IMContentDataModel {
    private String description;//图片网络路径
    private transient Uri photoUri;//图片的uri地址
    private String localUriString;//图片的本地Uri字符串路径
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

    @Override
    public String toString() {
        return "MessagePhotoBean{" +
                "description='" + description + '\'' +
                ", photoUri=" + photoUri +
                ", localUriString='" + localUriString + '\'' +
                '}';
    }
}