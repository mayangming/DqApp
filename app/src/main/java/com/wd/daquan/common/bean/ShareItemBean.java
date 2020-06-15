package com.wd.daquan.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.dq.im.model.IMContentDataModel;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.type.ImType;
import com.wd.daquan.third.session.extension.CustomAttachment;

/**
 * @Author: 方志
 * @Time: 2018/9/25 9:55
 * @Description: 分享或转发到指定会话的数据
 */
public class ShareItemBean implements Parcelable {

    public String sessionId;//会话id
    public String sessionName;//会话名
    public String sessionPortrait;//会话头像
    public ImType sessionType;//会话类型
    public IMContentDataModel attachment;//分享的附件
    public ImMessageBaseModel shareMessage;//分享的消息
    public String shareType;//分享的消息类型
    public String messageDesc;//分享的描述

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sessionId);
        dest.writeString(this.sessionName);
        dest.writeString(this.sessionPortrait);
        dest.writeInt(this.sessionType == null ? -1 : this.sessionType.ordinal());
        dest.writeSerializable(this.attachment);
        dest.writeSerializable(this.shareMessage);
        dest.writeString(this.shareType);
        dest.writeString(this.messageDesc);
    }

    public ShareItemBean() {
    }

    protected ShareItemBean(Parcel in) {
        this.sessionId = in.readString();
        this.sessionName = in.readString();
        this.sessionPortrait = in.readString();
        int tmpSessionType = in.readInt();
        this.sessionType = tmpSessionType == -1 ? null : ImType.values()[tmpSessionType];
        this.attachment = (CustomAttachment) in.readSerializable();
        this.shareMessage = (ImMessageBaseModel) in.readSerializable();
        this.shareType = in.readString();
        this.messageDesc = in.readString();
    }

    public static final Creator<ShareItemBean> CREATOR = new Creator<ShareItemBean>() {
        @Override
        public ShareItemBean createFromParcel(Parcel source) {
            return new ShareItemBean(source);
        }

        @Override
        public ShareItemBean[] newArray(int size) {
            return new ShareItemBean[size];
        }
    };
}
