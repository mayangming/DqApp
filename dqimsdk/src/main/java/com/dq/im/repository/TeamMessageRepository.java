package com.dq.im.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.dq.im.dao.TeamMessageBaseDao;
import com.dq.im.db.ImRoomDatabase;
import com.dq.im.model.ChatTeamModel;
import com.dq.im.model.TeamMessageBaseModel;

import java.util.List;

/**
 * 群聊仓库
 * 一个Repository类抽象了对多个数据源的访问。该存储库不是体系结构组件库的一部分，但是建议用于代码分离和体系结构的最佳实践。一个Repository类提供了一个干净的API，用于对应用程序其余部分的数据访问。
 * 为什么要使用存储库？
 * 存储库管理查询，并允许您使用多个后端。在最常见的示例中，存储库实现了用于确定是从网络中获取数据还是使用本地数据库中缓存的结果的逻辑
 */
public class TeamMessageRepository {
    private TeamMessageBaseDao teamMessageBaseDao;
    private LiveData<List<TeamMessageBaseModel>> imBaseModels;

    public TeamMessageRepository(Application application) {
        ImRoomDatabase imRoomDatabase = ImRoomDatabase.getDatabase(application);
        teamMessageBaseDao = imRoomDatabase.teamMessageBaseDao();
        imBaseModels = teamMessageBaseDao.getAllTeamMessage();
    }

    public LiveData<List<TeamMessageBaseModel>> getAllMessage(){
        return imBaseModels;
    }

//    /**
//     * 根据发信人ID和收信人ID进行查询聊天历史记录
//     * @param teamId 发信人
//     * @return 查询的历史记录
//     */
//    public LiveData<List<TeamMessageBaseModel>> getMessageByTeamId(String teamId){
//        return teamMessageBaseDao.getMessageByTeamId(teamId);
//    }
    /**
     * 根据发信人ID和收信人ID进行查询聊天历史记录
     * @param teamId 发信人
     * @return 查询的历史记录
     */
    public List<TeamMessageBaseModel> getMessageByTeamId(String teamId, long createTime, int limitCount){
        return teamMessageBaseDao.getMessageByTeamId(teamId,createTime,limitCount);
    }

    /**
     * 根据发信人ID和收信人ID进行查询聊天历史记录
     * @param teamId 发信人
     * @return 查询的历史记录
     */
    public LiveData<List<ChatTeamModel>> getMessageAndUserByTeamId(String teamId){
        return teamMessageBaseDao.getMessageAndUserByTeamId(teamId);
    }

    public LiveData<TeamMessageBaseModel> getTeamMessageByMessageId(String msgServerId){
        return teamMessageBaseDao.getTeamMessageByMessageId(msgServerId);
    }

    public void insert(TeamMessageBaseModel teamMessageBaseModel){
        ImRoomDatabase.databaseWriteExecutor.execute(() -> {
            teamMessageBaseDao.insert(teamMessageBaseModel);
        });
    }

    public void insert(List<TeamMessageBaseModel> teamMessageBaseModel){
        ImRoomDatabase.databaseWriteExecutor.execute(() -> {
            teamMessageBaseDao.insert(teamMessageBaseModel);
        });
    }

    public void update(TeamMessageBaseModel teamMessageBaseModel){
        ImRoomDatabase.databaseWriteExecutor.execute(() -> {
            teamMessageBaseDao.update(teamMessageBaseModel);
        });
    }

    public void updateMessageSendStatus(TeamMessageBaseModel teamMessageBaseModel){
        ImRoomDatabase.databaseWriteExecutor.execute(() -> {
            teamMessageBaseDao.updateMessageSendStatus(teamMessageBaseModel.getSourceContent(),teamMessageBaseModel.getMessageSendStatus(),teamMessageBaseModel.getMsgIdServer(),teamMessageBaseModel.getMsgIdClient());
        });
    }

    public void deleteAll(){
        ImRoomDatabase.databaseWriteExecutor.execute(() -> teamMessageBaseDao.deleteAll());
    }

    public void deleteMessageForGroupId(String groupId){
        ImRoomDatabase.databaseWriteExecutor.execute(() -> teamMessageBaseDao.deleteMessageForGroupId(groupId));
    }

    public void deleteMessageForClientId(String clientId){
        ImRoomDatabase.databaseWriteExecutor.execute(() -> teamMessageBaseDao.deleteMessageForClientId(clientId));
    }
}