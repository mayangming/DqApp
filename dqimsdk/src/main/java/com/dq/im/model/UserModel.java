package com.dq.im.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;


/**
 * 用户信息
 * 该接口存储好友及其个人的用户信息
 */
@Entity(tableName = "user_table",primaryKeys = {"userId" },indices = {@Index(value = "userId",unique = true)})
public class UserModel {

    @NonNull
    @ColumnInfo(name = "userId")
    private String userId = "1";

    @NonNull
    @ColumnInfo(name = "username")
    private String username = "";

    @NonNull
    @ColumnInfo(name = "nick")
    private String nick = "";

    @NonNull
    @ColumnInfo(name = "sex")
    private String sex = "0";//0 男 1 女

    @NonNull
    @ColumnInfo(name = "picUrl")
    private String picUrl = "";//头像

    @NonNull
    @ColumnInfo(name = "isFriend")
    private String isFriend = "";//好友状态，是不是好友 0不是 1是

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getNick() {
        return nick;
    }

    public void setNick(@NonNull String nick) {
        this.nick = nick;
    }

    @NonNull
    public String getSex() {
        return sex;
    }

    public void setSex(@NonNull String sex) {
        this.sex = sex;
    }

    @NonNull
    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(@NonNull String picUrl) {
        this.picUrl = picUrl;
    }

    @NonNull
    public String getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(@NonNull String isFriend) {
        this.isFriend = isFriend;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", nick='" + nick + '\'' +
                ", sex='" + sex + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", isFriend='" + isFriend + '\'' +
                '}';
    }
}