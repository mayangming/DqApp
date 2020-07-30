package com.dq.im.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.dq.im.model.ChatTeamModel;
import com.dq.im.model.TeamMessageBaseModel;

import java.util.List;

/**
 * 群组Im消息访问接口
 */
@Dao
public interface TeamMessageBaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TeamMessageBaseModel teamMessageBaseModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<TeamMessageBaseModel> teamMessageBaseModels);

    @Update
    void update(TeamMessageBaseModel teamMessageBaseModel);

    @Query("UPDATE team_message SET sourceContent = :sourceContent , messageSendStatus = :messageSendStatus , msgIdServer = :msgServerId WHERE msgIdClient = :msgClientId")
    void updateMessageSendStatus(String sourceContent,int messageSendStatus,String msgServerId,String msgClientId);


    @Query("UPDATE team_message SET msgIdServer = :serverId ,messageSendStatus = :messageSendStatus,sourceContent = :sourceContent WHERE msgIdClient = :clientId")
    void updateTeamPMessageByClientId(String clientId,String serverId,int messageSendStatus,String sourceContent);


    @Query("DELETE FROM team_message")
    void deleteAll();

    @Query("DELETE FROM team_message WHERE groupId = :groupId")
    void deleteMessageForGroupId(String groupId);

    @Query("DELETE FROM team_message WHERE msgIdClient = :msgClientId")
    void deleteMessageForClientId(String msgClientId);

    @Query("SELECT * FROM team_message ORDER BY createTime ASC")
    LiveData<List<TeamMessageBaseModel>> getAllTeamMessage();


//    /**
//     * 根据群Id进行查询
//     * @param teamId 群Id
//     * @return
//     */
//    @Query("SELECT * FROM team_message WHERE team_message.groupId = :teamId ORDER BY createTime ASC")
//    LiveData<List<TeamMessageBaseModel>> getMessageByTeamId(String teamId);
    /**
     * 根据群Id进行查询
     * @param teamId 群Id
     * @return
     */
    @Query("SELECT * FROM team_message WHERE team_message.groupId = :teamId AND team_message.createTime < :createTime ORDER BY createTime DESC LIMIT :limitCount")
    List<TeamMessageBaseModel> getMessageByTeamId(String teamId, long createTime, int limitCount);

    /**
     * 根据群组ID查询所有的数据
     * 只要是这个群组ID的信息
     * 只要发送者ID和用户ID在能匹配就查询出来
     * @param teamId 这个teamId是群组ID
     * @return
     */
    @Query("SELECT * FROM team_message,user_table WHERE team_message.fromUserId = user_table.userId AND team_message.groupId = :teamId ORDER BY createTime ASC")
    LiveData<List<ChatTeamModel>> getMessageAndUserByTeamId(String teamId);

    @Query("SELECT * FROM team_message WHERE msgIdServer = :msgServerId")
    LiveData<TeamMessageBaseModel> getTeamMessageByMessageId(String msgServerId);
}