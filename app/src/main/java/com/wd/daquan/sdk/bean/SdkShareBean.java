package com.wd.daquan.sdk.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <网络请求返回体>
 */
public class SdkShareBean implements Parcelable {
    public static final String SDK_SHARE_DATA = "SdkShareData";
    public static final String SDK_SHARE_BEAN = "SdkShareBean";

    public String url;
    public String intentUrl;
    public String title;
    public String content;
    public ShareType type;
    public String extra;
    public String backInfo;
    public String appId;
    public String appSecret;
    public String appName;
    public String appLogo;
    public String packageName;

    public enum ShareType{
        Text( 1),
        Image( 2),
        Link( 3);

        public int value;

        ShareType(int value){
            this.value = value;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.intentUrl);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.appId);
        dest.writeString(this.appSecret);
        dest.writeString(this.appName);
        dest.writeString(this.appLogo);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeString(this.extra);
        dest.writeString(this.backInfo);
        dest.writeString(this.packageName);
    }

    public SdkShareBean() {
    }

    protected SdkShareBean(Parcel in) {
        this.url = in.readString();
        this.intentUrl = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.appId = in.readString();
        this.appSecret = in.readString();
        this.appName = in.readString();
        this.appLogo = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : ShareType.values()[tmpType];
        this.extra = in.readString();
        this.backInfo = in.readString();
        this.packageName = in.readString();
    }

    public static final Creator<SdkShareBean> CREATOR = new Creator<SdkShareBean>() {
        @Override
        public SdkShareBean createFromParcel(Parcel source) {
            return new SdkShareBean(source);
        }

        @Override
        public SdkShareBean[] newArray(int size) {
            return new SdkShareBean[size];
        }
    };
}
