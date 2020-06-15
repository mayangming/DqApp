package com.wd.daquan.chat.group.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.dq.im.model.ImMessageBaseModel;

/**
 * @Author: 方志
 * @Time: 2019/5/14 14:37
 * @Description:
 */
public class SearchChatBean implements Parcelable {

    public String remark;
    public ImMessageBaseModel imMessage;

    public SearchChatBean(String remark, ImMessageBaseModel imMessage) {
        this.remark = remark;
        this.imMessage = imMessage;
    }

    public String getRemark() {
        return remark == null ? "" : remark;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.remark);
        dest.writeSerializable(this.imMessage);
    }

    public SearchChatBean() {
    }

    protected SearchChatBean(Parcel in) {
        this.remark = in.readString();
        this.imMessage = (ImMessageBaseModel) in.readSerializable();
    }

    public static final Parcelable.Creator<SearchChatBean> CREATOR = new Parcelable.Creator<SearchChatBean>() {
        @Override
        public SearchChatBean createFromParcel(Parcel source) {
            return new SearchChatBean(source);
        }

        @Override
        public SearchChatBean[] newArray(int size) {
            return new SearchChatBean[size];
        }
    };
}
