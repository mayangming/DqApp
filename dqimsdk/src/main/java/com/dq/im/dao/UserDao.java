package com.dq.im.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dq.im.model.GroupTeamUserModel;
import com.dq.im.model.UserModel;

import java.util.List;

/**
 * 用户信息访问接口
 */

@Dao
public interface UserDao {
    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserModel word);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<UserModel> users);

    @Query("SELECT 1 FROM user_table WHERE userId = :userId")
    int hasFriend(String userId);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(UserModel userModel);//这个会根据唯一主键进行修改

    @Query("UPDATE user_table SET userName = :name WHERE userId = :id")
    void updateForUserId(String id, String name);

    @Query("DELETE FROM user_table")
    void deleteAll();

    /**
     * 查询好友记录
     * @return
     */
    @Query("SELECT * from user_table WHERE isFriend = 1 ORDER BY userId ASC")
    LiveData<List<UserModel>> getAllUser();

    /**
     * 查询好友记录
     * @return
     */
    @Query("SELECT * from user_table WHERE isFriend = 1 ORDER BY userId ASC")
    LiveData<List<GroupTeamUserModel>> getAllUserAboutCreateTeamForUser();

    @Query("SELECT * from user_table WHERE userId = :userId")
    LiveData<UserModel> getUserByUserId(String userId);
}