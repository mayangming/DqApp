package com.dq.im.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.dq.im.dao.P2PMessageBaseDao;
import com.dq.im.db.ImRoomDatabase;
import com.dq.im.model.ChatP2PModel;
import com.dq.im.model.P2PMessageBaseModel;

import java.util.List;

/**
 * 单聊仓库
 * 一个Repository类抽象了对多个数据源的访问。该存储库不是体系结构组件库的一部分，但是建议用于代码分离和体系结构的最佳实践。一个Repository类提供了一个干净的API，用于对应用程序其余部分的数据访问。
 * 为什么要使用存储库？
 * 存储库管理查询，并允许您使用多个后端。在最常见的示例中，存储库实现了用于确定是从网络中获取数据还是使用本地数据库中缓存的结果的逻辑
 */
public class P2PMessageRepository {
    private P2PMessageBaseDao p2PMessageBaseDao;
    private LiveData<List<P2PMessageBaseModel>> imBaseModels;

    public P2PMessageRepository(Application application) {
        ImRoomDatabase imRoomDatabase = ImRoomDatabase.getDatabase(application);
        p2PMessageBaseDao = imRoomDatabase.p2pMessageBaseDao();
        imBaseModels = p2PMessageBaseDao.getAllMessage();
    }

    public LiveData<List<P2PMessageBaseModel>> getAllMessage(){
        return imBaseModels;
    }


    public LiveData<List<ChatP2PModel>> getChatModels(String userID){
        return p2PMessageBaseDao.getAllChatMessage(userID);
    }

    public LiveData<List<ChatP2PModel>> getChatModels(){
        return p2PMessageBaseDao.getAllChatMessage();
    }

//    /**
//     * 根据发信人ID和收信人ID进行查询聊天历史记录
//     * @param fromId 发信人
//     * @return 查询的历史记录
//     */
//    public LiveData<List<P2PMessageBaseModel>> getMessageByFromIdAndToId(String fromId,int limitCount,String createTime){
//        return p2PMessageBaseDao.getMessageByFromIdAndToId(fromId,limitCount,createTime);
//    }
    /**
     * 根据发信人ID和收信人ID进行查询聊天历史记录
     * @param fromId 发信人
     * @return 查询的历史记录
     */
    public List<P2PMessageBaseModel> getMessageByFromIdAndToId(String fromId,int limitCount,long createTime){
        return p2PMessageBaseDao.getMessageByFromIdAndToId(fromId,limitCount,createTime);
    }

//    /**
//     * 根据发信人ID和收信人ID进行查询聊天历史记录
//     * @param fromId 发信人
//     * @return 查询的历史记录
//     */
//    public LiveData<List<P2PMessageBaseModel>> getMessageByFromIdAndToId(String fromId,int limitCount){
//        return p2PMessageBaseDao.getMessageByFromIdAndToId(fromId,limitCount);
//    }
    /**
     * 根据发信人ID和收信人ID进行查询聊天历史记录
     * @param fromId 发信人
     * @return 查询的历史记录
     */
    public List<P2PMessageBaseModel> getMessageByFromIdAndToId(String fromId,int limitCount){
        return p2PMessageBaseDao.getMessageByFromIdAndToId(fromId,limitCount);
    }

    public void insert(P2PMessageBaseModel p2PMessageBaseModel){
        ImRoomDatabase.databaseWriteExecutor.execute(() -> {
            p2PMessageBaseDao.insert(p2PMessageBaseModel);
        });
    }

    public void insert(List<P2PMessageBaseModel> p2PMessageBaseModels){
        ImRoomDatabase.databaseWriteExecutor.execute(() -> {
            p2PMessageBaseDao.insert(p2PMessageBaseModels);
        });
    }

    public void update(P2PMessageBaseModel p2PMessageBaseModel){
        ImRoomDatabase.databaseWriteExecutor.execute(() -> {
            p2PMessageBaseDao.update(p2PMessageBaseModel);
        });
    }

    public void deleteAll(){
        ImRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                p2PMessageBaseDao.deleteAll();
            }
        });
    }

    public void deleteForMessageClientId(String messageClientId){
        ImRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                p2PMessageBaseDao.deleteMessageForClientId(messageClientId);
            }
        });
    }

    public void deleteMessageForFriendId(String friendId){
        ImRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                p2PMessageBaseDao.deleteMessageForFriendId(friendId);
            }
        });
    }
}