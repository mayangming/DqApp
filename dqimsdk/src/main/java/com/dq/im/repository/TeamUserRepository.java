package com.dq.im.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.dq.im.dao.TeamUserJoinDao;
import com.dq.im.db.ImRoomDatabase;
import com.dq.im.model.TeamModel;
import com.dq.im.model.TeamUserJoinModel;
import com.dq.im.model.UserModel;

import java.util.List;

/**
 * 一个Repository类抽象了对多个数据源的访问。该存储库不是体系结构组件库的一部分，但是建议用于代码分离和体系结构的最佳实践。一个Repository类提供了一个干净的API，用于对应用程序其余部分的数据访问。
 * 为什么要使用存储库？
 * 存储库管理查询，并允许您使用多个后端。在最常见的示例中，存储库实现了用于确定是从网络中获取数据还是使用本地数据库中缓存的结果的逻辑
 */
public class TeamUserRepository {
    private TeamUserJoinDao teamUserJoinDao;
    private LiveData<List<TeamModel>> teamModels;
    private LiveData<List<UserModel>> userModels;

    public TeamUserRepository(Application application) {
        ImRoomDatabase imRoomDatabase = ImRoomDatabase.getDatabase(application);
        teamUserJoinDao = imRoomDatabase.teamUserJoinDao();
    }

    /**
     * 添加新的群组信息
     */
    public void insert(TeamUserJoinModel teamUserJoinModel){
        ImRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                teamUserJoinDao.insert(teamUserJoinModel);
            }
        });
    }

    /**
     * 批量添加新的群组信息
     */
    public void insert(List<TeamUserJoinModel> teamUserJoinModelList){
        ImRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                teamUserJoinDao.insert(teamUserJoinModelList);
            }
        });
    }

    /**
     * 根据用户Id查询其所拥有的群组
     */
    public LiveData<List<TeamModel>> getTeamForUserId(String userId){
        teamModels = teamUserJoinDao.getTeamForUser(userId);
        return teamModels;
    }

    /**
     * 根据群组Id查询其拥有的成员
     */
    public LiveData<List<UserModel>> getUserForTeamId(String teamId){
        userModels = teamUserJoinDao.getUserForTeam(teamId);
        return userModels;
    }
}