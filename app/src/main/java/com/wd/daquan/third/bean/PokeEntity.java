package com.wd.daquan.third.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.dq.im.type.ImType;

/**
 * @author: dukangkang
 * @date: 2018/12/11 17:44.
 * @description: todo ...
 */
public class PokeEntity implements Parcelable {
    public boolean isGroup = false;
    public String receiveId = "";
    public String receiveName = "";
    public String receivePortraitUrl = "";
    public String targetId = "";
    public String targetName = "";
    public ImType sessionType;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isGroup ? (byte) 1 : (byte) 0);
        dest.writeString(this.receiveId);
        dest.writeString(this.receiveName);
        dest.writeString(this.receivePortraitUrl);
        dest.writeString(this.targetId);
        dest.writeString(this.targetName);
        dest.writeInt(this.sessionType == null ? -1 : this.sessionType.ordinal());
    }

    public PokeEntity() {
    }

    protected PokeEntity(Parcel in) {
        this.isGroup = in.readByte() != 0;
        this.receiveId = in.readString();
        this.receiveName = in.readString();
        this.receivePortraitUrl = in.readString();
        this.targetId = in.readString();
        this.targetName = in.readString();
        int tmpSessionType = in.readInt();
        this.sessionType = tmpSessionType == -1 ? null : ImType.values()[tmpSessionType];
    }

    public static final Creator<PokeEntity> CREATOR = new Creator<PokeEntity>() {
        @Override
        public PokeEntity createFromParcel(Parcel source) {
            return new PokeEntity(source);
        }

        @Override
        public PokeEntity[] newArray(int size) {
            return new PokeEntity[size];
        }
    };

    @Override
    public String toString() {
        return "PokeEntity{" +
                "isGroup=" + isGroup +
                ", receiveId='" + receiveId + '\'' +
                ", receiveName='" + receiveName + '\'' +
                ", receivePortraitUrl='" + receivePortraitUrl + '\'' +
                ", targetId='" + targetId + '\'' +
                ", targetName='" + targetName + '\'' +
                ", sessionType=" + sessionType +
                '}';
    }
}
