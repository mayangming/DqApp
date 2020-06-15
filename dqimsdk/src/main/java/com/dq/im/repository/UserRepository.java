package com.dq.im.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.dq.im.dao.UserDao;
import com.dq.im.db.ImRoomDatabase;
import com.dq.im.model.GroupTeamUserModel;
import com.dq.im.model.UserModel;

import java.util.List;

/**
 * 一个Repository类抽象了对多个数据源的访问。该存储库不是体系结构组件库的一部分，但是建议用于代码分离和体系结构的最佳实践。一个Repository类提供了一个干净的API，用于对应用程序其余部分的数据访问。
 * 为什么要使用存储库？
 * 存储库管理查询，并允许您使用多个后端。在最常见的示例中，存储库实现了用于确定是从网络中获取数据还是使用本地数据库中缓存的结果的逻辑
 */
public class UserRepository {
    private UserDao mUserDao;
    private LiveData<List<UserModel>> mAllUser;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public UserRepository(Application application) {
        ImRoomDatabase db = ImRoomDatabase.getDatabase(application);
        mUserDao = db.userDao();
        mAllUser = mUserDao.getAllUser();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<UserModel>> getAllUser() {

        return mAllUser;
    }
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<GroupTeamUserModel>> getAllUserAboutCreateTeamForUser() {
        return mUserDao.getAllUserAboutCreateTeamForUser();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<UserModel> getUserByUserId(String userId) {
        return mUserDao.getUserByUserId(userId);
    }

    public int hasFriend(String userId){
        return mUserDao.hasFriend(userId);
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(UserModel word) {
        ImRoomDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.insert(word);
        });
    }

    public void insert(final List<UserModel> user) {
        ImRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mUserDao.insert(user);
            }
        });
    }
    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void update(UserModel word) {
        ImRoomDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.update(word);
        });
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void updateNameById(UserModel word) {
        ImRoomDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.updateForUserId(word.getUserId(),word.getNick());
        });
    }

}