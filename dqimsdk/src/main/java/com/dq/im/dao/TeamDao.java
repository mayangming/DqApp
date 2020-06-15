package com.dq.im.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dq.im.model.TeamModel;

import java.util.List;

/**
 * 群组信息接口
 */
@Dao
public interface TeamDao {
    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TeamModel word);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<TeamModel> users);

    @Query("SELECT 1 FROM team_table WHERE groupId = :teamId")
    int hasTeam(String teamId);

    @Update
    void update(TeamModel word);//这个会根据唯一主键进行修改

    @Query("UPDATE team_table SET groupName = :name WHERE groupId = :id")
    void updateForUserId(String id, String name);

    @Query("DELETE FROM team_table")
    void deleteAll();

    @Query("SELECT * from team_table ORDER BY groupId ASC")
    LiveData<List<TeamModel>> getAllTeam();

    @Query("SELECT * from team_table WHERE groupId = :teamId")
    LiveData<TeamModel> getTeamByTeamId(String teamId);
}