package com.dq.im.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.dq.im.dao.TeamDao;
import com.dq.im.db.ImRoomDatabase;
import com.dq.im.model.TeamModel;

import java.util.List;

/**
 * 一个Repository类抽象了对多个数据源的访问。该存储库不是体系结构组件库的一部分，但是建议用于代码分离和体系结构的最佳实践。一个Repository类提供了一个干净的API，用于对应用程序其余部分的数据访问。
 * 为什么要使用存储库？
 * 存储库管理查询，并允许您使用多个后端。在最常见的示例中，存储库实现了用于确定是从网络中获取数据还是使用本地数据库中缓存的结果的逻辑
 */
public class TeamRepository {
    private TeamDao mTeamDao;
    private LiveData<List<TeamModel>> mAllTeam;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public TeamRepository(Application application) {
        ImRoomDatabase db = ImRoomDatabase.getDatabase(application);
        mTeamDao = db.teamDao();
        mAllTeam = mTeamDao.getAllTeam();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<TeamModel>> getAllTeam() {
        return mAllTeam;
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<TeamModel> getTeamByTeamId(String teamId) {
        return mTeamDao.getTeamByTeamId(teamId);
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(TeamModel teamModel) {
        ImRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTeamDao.insert(teamModel);
        });
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(List<TeamModel> teamModels) {
        ImRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTeamDao.insert(teamModels);
        });
    }


    public int hasTeam(String teamId){
        return mTeamDao.hasTeam(teamId);
    };

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void update(TeamModel teamModel) {
        ImRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTeamDao.update(teamModel);
        });
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void updateNameById(TeamModel teamModel) {
        ImRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTeamDao.updateForUserId(teamModel.getGroupId(),teamModel.getGroupName());
        });
    }

}