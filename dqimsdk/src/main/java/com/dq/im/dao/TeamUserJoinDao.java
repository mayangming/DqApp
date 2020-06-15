package com.dq.im.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;

import com.dq.im.model.TeamModel;
import com.dq.im.model.TeamUserJoinModel;
import com.dq.im.model.UserModel;

import java.util.List;

/**
 * 查询群组和成员关系的接口
 */
@Dao
public interface TeamUserJoinDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TeamUserJoinModel teamUserJoinModel);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<TeamUserJoinModel> teamUserJoinModels);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM team_table " +
            "INNER JOIN team_user_join " +
            "ON team_table.groupId = team_user_join.teamId " +
            "WHERE team_user_join.userId = :userId")
    LiveData<List<TeamModel>> getTeamForUser(String userId);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM user_table " +
            "INNER JOIN team_user_join " +
            "ON user_table.userId = team_user_join.userId " +
            "WHERE team_user_join.teamId=:teamId")
    LiveData<List<UserModel>> getUserForTeam(String teamId);
}