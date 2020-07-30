package com.dq.im.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.annotation.NonNull;


@Entity(tableName = "team_table",primaryKeys = {"groupId" },indices = {@Index(value = "groupId",unique = true)})
public class TeamModel {

    @NonNull
    @ColumnInfo(name = "userId")
    private String userId = "";//群主ID

    @NonNull
    @ColumnInfo(name = "groupId")
    private String groupId = "";//群ID


    /**
     * 头像字段名字需要和用户信息表的头像字段名字保持一致
     */
    @NonNull
    @ColumnInfo(name = "groupAvatarUrl")
    private String groupAvatarUrl = "";//群头像

    @NonNull
    @ColumnInfo(name = "groupName")
    private String groupName = "";//群名称

    @NonNull
    @ColumnInfo(name = "groupNick")
    private String groupNick = "";//群名称

    @NonNull
    @ColumnInfo(name = "createTime")
    private String createTime = "";//创建时间

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(@NonNull String groupId) {
        this.groupId = groupId;
    }

    @NonNull
    public String getGroupAvatarUrl() {
        return groupAvatarUrl;
    }

    public void setGroupAvatarUrl(@NonNull String groupAvatarUrl) {
        this.groupAvatarUrl = groupAvatarUrl;
    }

    @NonNull
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(@NonNull String groupName) {
        this.groupName = groupName;
    }

    @NonNull
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(@NonNull String createTime) {
        this.createTime = createTime;
    }

    @NonNull
    public String getGroupNick() {
        return groupNick;
    }

    public void setGroupNick(@NonNull String groupNick) {
        this.groupNick = groupNick;
    }

    @Override
    public String toString() {
        return "TeamModel{" +
                "userId='" + userId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", groupAvatarUrl='" + groupAvatarUrl + '\'' +
                ", groupName='" + groupName + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}