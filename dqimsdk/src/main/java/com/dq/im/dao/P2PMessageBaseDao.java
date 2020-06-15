package com.dq.im.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dq.im.model.ChatP2PModel;
import com.dq.im.model.P2PMessageBaseModel;

import java.util.List;

/**
 * 个人Im消息访问接口
 */
@Dao
public interface P2PMessageBaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(P2PMessageBaseModel p2PMessageBaseModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<P2PMessageBaseModel> p2PMessageBaseModels);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(P2PMessageBaseModel p2PMessageBaseModel);

    @Query("DELETE FROM person_message")
    void deleteAll();

    @Query("DELETE FROM person_message WHERE msgIdClient = :msgClientId")
    void deleteMessageForClientId(String msgClientId);

    /**
     * 清空个人聊天消息记录
     * @param friendId
     */
    @Query("DELETE FROM person_message WHERE fromUserId = :friendId OR toUserId = :friendId")
    void deleteMessageForFriendId(String friendId);

    @Query("SELECT * FROM person_message ")
    LiveData<List<P2PMessageBaseModel>> getAllMessage();

//    @Query("SELECT * FROM person_message ")
//    LiveData<List<ChatP2PModel>> getAllChatMessage();

    /**
     * 根据用户ID查询所有的数据
     * @return
     */
    @Query("SELECT * FROM person_message,user_table WHERE person_message.fromUserId = user_table.userId")
    LiveData<List<ChatP2PModel>> getAllChatMessage();
    /**
     * 根据用户ID查询所有的数据
     * @param userId 不管是自己发的还是对方发的都查出来
     * @return
     */
    @Query("SELECT * FROM person_message,user_table WHERE (person_message.fromUserId = user_table.userId OR person_message.toUserId = user_table.userId) AND user_table.userId = :userId")
    LiveData<List<ChatP2PModel>> getAllChatMessage(String userId);

//    /**
//     * 收信人ID、发信人ID来查询聊天记录, 根据当前时间开始查询
//     * @param fromId 发信人ID
//     */
//    @Query("SELECT * FROM person_message WHERE (person_message.fromUserId = :fromId OR person_message.toUserId = :fromId) AND person_message.createTime <= :createTime LIMIT :limitCount")
//    LiveData<List<P2PMessageBaseModel>> getMessageByFromIdAndToId(String fromId,int limitCount,String createTime);


//    /**
//     * 收信人ID、发信人ID来查询聊天记录
//     * @param fromId 发信人ID
//     */
//    @Query("SELECT * FROM person_message WHERE person_message.fromUserId = :fromId OR person_message.toUserId = :fromId LIMIT :limitCount")
//    LiveData<List<P2PMessageBaseModel>> getMessageByFromIdAndToId(String fromId,int limitCount);

    /**
     * 收信人ID、发信人ID来查询聊天记录, 根据当前时间开始查询
     * @param fromId 发信人ID
     */
    @Query("SELECT * FROM person_message WHERE (person_message.fromUserId = :fromId OR person_message.toUserId = :fromId) AND person_message.createTime < :createTime ORDER BY createTime DESC LIMIT :limitCount ")
    List<P2PMessageBaseModel> getMessageByFromIdAndToId(String fromId,int limitCount,long createTime);

    /**
     * 收信人ID、发信人ID来查询聊天记录
     * @param fromId 发信人ID
     */
    @Query("SELECT * FROM person_message WHERE person_message.fromUserId = :fromId OR person_message.toUserId = :fromId LIMIT :limitCount")
   List<P2PMessageBaseModel> getMessageByFromIdAndToId(String fromId,int limitCount);
}