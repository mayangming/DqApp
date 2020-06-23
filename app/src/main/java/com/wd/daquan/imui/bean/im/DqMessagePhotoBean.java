package com.wd.daquan.imui.bean.im;

import android.net.Uri;

import com.wd.daquan.util.AESUtil;

/**
 * 斗圈项目中文本消息内容
 */
public class DqMessagePhotoBean extends DqMessageBaseContent{
    private String searchableContent = AESUtil.decode("[图片]");//图片描述
    private String remoteMediaUrl = "";//图片远程路径
    private int mediaType = 1;//消息类型
    private transient Uri photoUri;//图片的uri地址
    private String localUriString;//图片的本地Uri字符串路径
    private String localMediaPath = "";//PC端图片的本地Uri字符串路径
    public String getSearchableContent() {
        return searchableContent;
    }

    public void setSearchableContent(String searchableContent) {
        this.searchableContent = searchableContent;
    }

    public String getRemoteMediaUrl() {
        return remoteMediaUrl;
    }

    public void setRemoteMediaUrl(String remoteMediaUrl) {
        this.remoteMediaUrl = remoteMediaUrl;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
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

    public String getLocalMediaPath() {
        return localMediaPath;
    }

    public void setLocalMediaPath(String localMediaPath) {
        this.localMediaPath = localMediaPath;
    }

    @Override
    public String toString() {
        return "DqMessagePhotoBean{" +
                "searchableContent='" + searchableContent + '\'' +
                ", remoteMediaUrl='" + remoteMediaUrl + '\'' +
                ", mediaType=" + mediaType +
                ", photoUri=" + photoUri +
                ", localUriString='" + localUriString + '\'' +
                ", localMediaPath='" + localMediaPath + '\'' +
                '}';
    }
}