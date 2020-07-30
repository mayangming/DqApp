package com.dq.im.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.dq.im.model.HomeImBaseMode;

import java.util.List;

/**
 * 首页消息列表
 */
@Dao
public abstract class HomeImBaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(HomeImBaseMode homeImBaseMode);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<HomeImBaseMode> homeImBaseModes);

    @Update
    public abstract void updateTeamUnReadNumber(HomeImBaseMode homeImBaseMode);

    /**
     * 根据好友Id进行更改消息未读数量
     * 当好友ID为收信人ID或者是发信人ID时候进行更改
     * @param friendId 好友ID
     * @param unReadNum 未读消息数
     */
    @Query("UPDATE home_message SET unReadNumber = :unReadNum WHERE fromUserId = :friendId OR toUserId = :friendId")
    public abstract void updateTeFriendUnReadNumber(String friendId, int unReadNum);

    /**
     * 根据群组Id进行更改消息未读数量
     * @param unReadNum 未读消息数
     */
    @Query("UPDATE home_message SET unReadNumber = :unReadNum WHERE groupId = :groupId")
    public abstract void updateTeamUnReadNumber(String groupId, int unReadNum);

    /**
     * 清空所有未读消息数
     * @param unReadNum 未读消息数
     */
    @Query("UPDATE home_message SET unReadNumber = :unReadNum")
    public abstract void updateAllUnReadNumber(int unReadNum);


    @Query("UPDATE home_message SET msgIdServer = :serverId ,messageSendStatus = :messageSendStatus,sourceContent = :sourceContent WHERE msgIdClient = :clientId")
    public abstract void updateHomeMessageByClientId(String clientId,String serverId,int messageSendStatus,String sourceContent);

    @Query("DELETE FROM home_message")
    public abstract void deleteAll();

    /**
     * 根据客户端消息ID删除消息
     * @param messageId
     */
    @Query("DELETE FROM home_message WHERE msgIdServer = :messageId")
    public abstract void deleteForServerMessageId(String messageId);

    /**
     * 根据用户id删除对应的首页消息
     */
    @Query("DELETE FROM home_message WHERE type = '1' AND (fromUserId = :friendId OR toUserId = :friendId)")
    public abstract void deleteForFriendId(String friendId);

    /**
     * 根据群组id删除对应的首页消息
     */
    @Query("DELETE FROM home_message WHERE groupId = :groupId")
    public abstract void deleteForGroupId(String groupId);

    /**
     * 根据客户端用户ID删除消息
     * @param userId
     */
    @Query("DELETE FROM home_message WHERE fromUserId = :userId OR toUserId = :userId")
    public abstract void deleteForUserId(String userId);

    /**
     * 根据客户端消息ID删除消息
     */
    @Query("SELECT COUNT(*) FROM home_message")
    public abstract int countHomeMessage();

    /**
     * 根据客户端消息ID删除消息
     * @param messageId
     */
    @Query("DELETE FROM home_message WHERE msgIdClient = :messageId")
    public abstract void deleteForClientMessageId(String messageId);

    @Query("SELECT * FROM home_message ORDER BY createTime DESC")
    public abstract LiveData<List<HomeImBaseMode>> query();

    /**
     * 该接口不提供异步更新状态
     * 根据群组ID查询首页消息，判断首页是否有这条消息
     */
    @Query("SELECT * FROM home_message WHERE groupId = :groupId AND type = '2' ORDER BY createTime ASC")
    public abstract HomeImBaseMode queryHomeMessageModelByGroupId(String groupId);

    /**
     * 该接口不提供异步更新状态
     * 根据好友ID查询首页消息，判断首页是否有这条消息
     */
    @Query("SELECT * FROM home_message WHERE (fromUserId = :friendId OR toUserId = :friendId) AND type = '1' ORDER BY createTime DESC")
    public abstract HomeImBaseMode queryHomeMessageModelByFriendId(String friendId);

    /**
     * 启用事务对首页数据进行更新
     * @param homeImBaseMode
     * @param isAddUndReadNum
     * @param userId
     */
    @Transaction
    public void updateHomeMessage(HomeImBaseMode homeImBaseMode,boolean isAddUndReadNum,String userId){
        HomeImBaseMode homeImBaseModeTemp;
        int unReadNum = 0;
        if ("1".equals(homeImBaseMode.getType())){//单聊
            String friendId = "";
            if (userId.equals(homeImBaseMode.getFromUserId())){
                friendId = homeImBaseMode.getToUserId();
            }else {
                friendId = homeImBaseMode.getFromUserId();
            }
            homeImBaseModeTemp = queryHomeMessageModelByFriendId(friendId);
        }else {//群聊
            homeImBaseModeTemp = queryHomeMessageModelByGroupId(homeImBaseMode.getGroupId());
        }
        if (null != homeImBaseModeTemp){//假如存在数据,则删除该条数据
            unReadNum = homeImBaseModeTemp.getUnReadNumber();
            deleteForServerMessageId(homeImBaseModeTemp.getMsgIdServer());
        }else {
        }
        if (isAddUndReadNum){
            unReadNum = unReadNum + 1;
        }
        homeImBaseMode.setUnReadNumber(unReadNum);
        insert(homeImBaseMode);
    }

}