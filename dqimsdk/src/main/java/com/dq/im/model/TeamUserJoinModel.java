package com.dq.im.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;


/**
 * 定义群组与成员之间的关系
 */
@Entity(tableName = "team_user_join",
        primaryKeys = { "teamId","userId"},
        foreignKeys = {@ForeignKey(entity = TeamModel.class,
                parentColumns = "groupId",
                childColumns = "teamId"),
                @ForeignKey(entity = UserModel.class,
                        parentColumns = "userId",
                        childColumns = "userId")
        },indices = {@Index(value = "teamId"),@Index(value = "userId")})
public class TeamUserJoinModel {
    @ColumnInfo(name = "teamId")
    @NonNull
    private String teamId;

    @ColumnInfo(name = "userId")
    @NonNull
    private String userId = "";

    @NonNull
    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(@NonNull String teamId) {
        this.teamId = teamId;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }
}