package com.dq.im.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;

/**
 * 首页聊天数据的内容
 */
public class ChatHomeModel {
    @Embedded
    private HomeImBaseMode homeImBaseMode;

    @ColumnInfo(name = "headPic")
    private String headIcon;//头像

    public HomeImBaseMode getHomeImBaseMode() {
        return homeImBaseMode;
    }

    public void setHomeImBaseMode(HomeImBaseMode homeImBaseMode) {
        this.homeImBaseMode = homeImBaseMode;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    @Override
    public String toString() {
        return "ChatHomeModel{" +
                "homeImBaseMode=" + homeImBaseMode +
                ", headIcon='" + headIcon + '\'' +
                '}';
    }
}