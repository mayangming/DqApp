package com.wd.daquan.model.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * @Author: 方志
 * @Time: 2018/9/14 11:41
 * @Description:
 */
public class NewFriendBean implements Parcelable {

    /**
     * nickName : 2223
     * response_status : 0
     * phone : 13522832223
     * uid : 32
     */

    public String nickname;
    public String response_status; // （0:等待处理 1:同意 2:拒绝 3:忽略）
    public String phone;
    public String uid;
    public String to_say;
    public String whether_friend; // 0是好友，1不是
    public String source;
    public String sex;//1男 2女
    public String role;//2：群主 1：管理员 0：普通成员
    public String headpic;
    public String request_id;
    public String whether_black;
    public String isVip;//是否是vip 0：不是vip 1：是vip
    public String vipStartTime;//vip开始时间
    public String vipEndTime;//vip结束时间

    @Override
    public int describeContents() {
        return 0;
    }


    public boolean isVip(){
        return (!TextUtils.isEmpty(isVip) && "1".equals(isVip));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nickname);
        dest.writeString(this.response_status);
        dest.writeString(this.phone);
        dest.writeString(this.uid);
        dest.writeString(this.to_say);
        dest.writeString(this.whether_friend);
        dest.writeString(this.source);
        dest.writeString(this.sex);
        dest.writeString(this.role);
        dest.writeString(this.headpic);
        dest.writeString(this.request_id);
        dest.writeString(this.whether_black);
        dest.writeString(this.isVip);
        dest.writeString(this.vipStartTime);
        dest.writeString(this.vipEndTime);
    }

    public NewFriendBean() {
    }

    protected NewFriendBean(Parcel in) {
        this.nickname = in.readString();
        this.response_status = in.readString();
        this.phone = in.readString();
        this.uid = in.readString();
        this.to_say = in.readString();
        this.whether_friend = in.readString();
        this.source = in.readString();
        this.sex = in.readString();
        this.role = in.readString();
        this.headpic = in.readString();
        this.request_id = in.readString();
        this.whether_black = in.readString();
        this.isVip = in.readString();
        this.vipStartTime = in.readString();
        this.vipEndTime = in.readString();
    }

    public static final Parcelable.Creator<NewFriendBean> CREATOR = new Parcelable.Creator<NewFriendBean>() {
        @Override
        public NewFriendBean createFromParcel(Parcel source) {
            return new NewFriendBean(source);
        }

        @Override
        public NewFriendBean[] newArray(int size) {
            return new NewFriendBean[size];
        }
    };
}
