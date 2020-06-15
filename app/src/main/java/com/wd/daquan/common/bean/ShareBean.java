package com.wd.daquan.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.da.library.constant.IConstant;
import com.dq.im.model.IMContentDataModel;
import com.dq.im.model.ImMessageBaseModel;

/**
 * @Author: 方志
 * @Time: 2018/10/17 16:19
 * @Description: 分享或转发的数据
 */
public class ShareBean implements Parcelable {

    public String enterType = IConstant.Share.SHARE;//分享/转发，默认分享
    public String textOrImage = IConstant.Share.TEXT;//本地图片或文本类型 默认文字
    public String path;//文本内容/图片路径
    public ImMessageBaseModel imMessage;//消息
    public IMContentDataModel attachment;//消息附件

    public ShareBean(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.enterType);
        dest.writeString(this.textOrImage);
        dest.writeString(this.path);
        dest.writeSerializable(this.imMessage);
        dest.writeSerializable(this.attachment);
    }


    private ShareBean(Parcel in) {
        this.enterType = in.readString();
        this.textOrImage = in.readString();
        this.path = in.readString();
        this.imMessage = (ImMessageBaseModel) in.readSerializable();
        this.attachment = (IMContentDataModel) in.readSerializable();
    }

    public static final Parcelable.Creator<ShareBean> CREATOR = new Parcelable.Creator<ShareBean>() {
        @Override
        public ShareBean createFromParcel(Parcel source) {
            return new ShareBean(source);
        }

        @Override
        public ShareBean[] newArray(int size) {
            return new ShareBean[size];
        }
    };
}
