package com.wd.daquan.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 聊天记忆
 * Created by Kind on 2018/9/29.
 *  草稿
 */

public class Draft implements Parcelable {

    public String sessionID;
    public String sessionType;
    public String content;
    public long time;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sessionID);
        dest.writeString(this.sessionType);
        dest.writeString(this.content);
        dest.writeLong(this.time);
    }

    public Draft() {
    }

    protected Draft(Parcel in) {
        this.sessionID = in.readString();
        this.sessionType = in.readString();
        this.content = in.readString();
        this.time = in.readLong();
    }

    public static final Parcelable.Creator<Draft> CREATOR = new Parcelable.Creator<Draft>() {
        @Override
        public Draft createFromParcel(Parcel source) {
            return new Draft(source);
        }

        @Override
        public Draft[] newArray(int size) {
            return new Draft[size];
        }
    };

    @Override
    public String toString() {
        return "Draft{" +
                "sessionID='" + sessionID + '\'' +
                ", sessionType='" + sessionType + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                '}';
    }
}
