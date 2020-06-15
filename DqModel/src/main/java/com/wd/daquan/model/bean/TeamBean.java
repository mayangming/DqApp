package com.wd.daquan.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Author: 方志
 * @Time: 2018/9/20 20:08
 * @Description:
 */
public class TeamBean implements Parcelable {

    public String group_id;
    public String group_name;
    public String group_pic;
    public String group_member_num;

    public String getGroup_id() {
        return group_id == null ? "" : group_id;
    }

    public String getGroup_name() {
        return group_name == null ? "" : group_name.trim();
    }

    public String getGroup_member_num() {
        return group_member_num == null ? "" : group_member_num;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.group_id);
        dest.writeString(this.group_name);
        dest.writeString(this.group_pic);
        dest.writeString(this.group_member_num);
    }

    public TeamBean() {
    }

    protected TeamBean(Parcel in) {
        this.group_id = in.readString();
        this.group_name = in.readString();
        this.group_pic = in.readString();
        this.group_member_num = in.readString();
    }

    public static final Creator<TeamBean> CREATOR = new Creator<TeamBean>() {
        @Override
        public TeamBean createFromParcel(Parcel source) {
            return new TeamBean(source);
        }

        @Override
        public TeamBean[] newArray(int size) {
            return new TeamBean[size];
        }
    };
}
