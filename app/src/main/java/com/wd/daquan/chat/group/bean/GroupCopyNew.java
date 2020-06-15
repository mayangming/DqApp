package com.wd.daquan.chat.group.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 一键复制新群
 */
public class GroupCopyNew implements Parcelable {

    public String group_id;
    public String group_pic;
    public String group_name;
    public List<GroupCloseInvite> refuse;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.group_id);
        dest.writeString(this.group_pic);
        dest.writeString(this.group_name);
        dest.writeTypedList(this.refuse);
    }

    public GroupCopyNew() {
    }

    protected GroupCopyNew(Parcel in) {
        this.group_id = in.readString();
        this.group_pic = in.readString();
        this.group_name = in.readString();
        this.refuse = in.createTypedArrayList(GroupCloseInvite.CREATOR);
    }

    public static final Creator<GroupCopyNew> CREATOR = new Creator<GroupCopyNew>() {
        @Override
        public GroupCopyNew createFromParcel(Parcel source) {
            return new GroupCopyNew(source);
        }

        @Override
        public GroupCopyNew[] newArray(int size) {
            return new GroupCopyNew[size];
        }
    };
}
