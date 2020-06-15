package com.wd.daquan.chat.group.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 关闭邀请入群
 * 设置里一是否邀请入群
 */
public class GroupCloseInvite implements Parcelable {

    public String id;
    public String nickname;

    protected GroupCloseInvite(Parcel in) {
        id = in.readString();
        nickname = in.readString();
    }

    public static final Creator<GroupCloseInvite> CREATOR = new Creator<GroupCloseInvite>() {
        @Override
        public GroupCloseInvite createFromParcel(Parcel in) {
            return new GroupCloseInvite(in);
        }

        @Override
        public GroupCloseInvite[] newArray(int size) {
            return new GroupCloseInvite[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(nickname);
    }
}
