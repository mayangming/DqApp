package com.wd.daquan.chat.group.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Author: 方志
 * @Time: 2019/5/13 20:38
 * @Description:
 */
public class GroupSearchChatType implements Parcelable {

    public String groupId;
    public SearchChatType searchChatType;

    public String getGroupId() {
        return  groupId == null ? "" : groupId;
    }

    public GroupSearchChatType(String groupId, SearchChatType searchChatType) {
        this.groupId = groupId;
        this.searchChatType = searchChatType;
    }

    public enum SearchChatType{
        Trade(0, "交易");

        public int key;
        public String value;

        SearchChatType(int key, String value){
            this.key = key;
            this.value = value;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.groupId);
        dest.writeInt(this.searchChatType == null ? -1 : this.searchChatType.ordinal());
    }

    public GroupSearchChatType() {
    }

    protected GroupSearchChatType(Parcel in) {
        this.groupId = in.readString();
        int tmpSearchChatType = in.readInt();
        this.searchChatType = tmpSearchChatType == -1 ? null : SearchChatType.values()[tmpSearchChatType];
    }

    public static final Creator<GroupSearchChatType> CREATOR = new Creator<GroupSearchChatType>() {
        @Override
        public GroupSearchChatType createFromParcel(Parcel source) {
            return new GroupSearchChatType(source);
        }

        @Override
        public GroupSearchChatType[] newArray(int size) {
            return new GroupSearchChatType[size];
        }
    };
}
