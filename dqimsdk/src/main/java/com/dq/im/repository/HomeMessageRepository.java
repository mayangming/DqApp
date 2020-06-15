package com.dq.im.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.dq.im.dao.HomeImBaseDao;
import com.dq.im.db.ImRoomDatabase;
import com.dq.im.model.HomeImBaseMode;

import java.util.List;

public class HomeMessageRepository {
    private HomeImBaseDao homeImBaseDao;
    private LiveData<List<HomeImBaseMode>> homeImBaseModels;

    public HomeMessageRepository(Application application) {
        ImRoomDatabase imRoomDatabase = ImRoomDatabase.getDatabase(application);
        homeImBaseDao = imRoomDatabase.homeImBaseDao();
        homeImBaseModels = homeImBaseDao.query();
    }

    public LiveData<List<HomeImBaseMode>> getAllMessage(){
        return homeImBaseModels;
    }

    public void insert(HomeImBaseMode homeImBaseMode){
        ImRoomDatabase.databaseWriteExecutor.execute(() -> {
            homeImBaseDao.insert(homeImBaseMode);
        });
    }

    public void insert(List<HomeImBaseMode> homeImBaseMode){
        ImRoomDatabase.databaseWriteExecutor.execute(() -> {
            homeImBaseDao.insert(homeImBaseMode);
        });
    }

    public void update(HomeImBaseMode homeImBaseMode){
        ImRoomDatabase.databaseWriteExecutor.execute(() -> {
            homeImBaseDao.updateTeamUnReadNumber(homeImBaseMode);
        });
    }

    public void updateTeFriendUnReadNumber(String friendId, int unReadNum){
        ImRoomDatabase.databaseWriteExecutor.execute(() -> {
            homeImBaseDao.updateTeFriendUnReadNumber(friendId,unReadNum);
        });
    }

    public void updateTeamUnReadNumber(String groupId, int unReadNum){
        ImRoomDatabase.databaseWriteExecutor.execute(() -> {
            homeImBaseDao.updateTeamUnReadNumber(groupId,unReadNum);
        });
    }

    public void deleteForServerMessageId(String msgServerId){
        ImRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                homeImBaseDao.deleteForServerMessageId(msgServerId);
            }
        });
    }

    public void deleteForUserId(String userId){
        ImRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                homeImBaseDao.deleteForUserId(userId);
            }
        });
    }

    public void deleteForFriendId(String userId){
        ImRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                homeImBaseDao.deleteForFriendId(userId);
            }
        });
    }

    public void deleteForGroupId(String groupId){
        ImRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                homeImBaseDao.deleteForGroupId(groupId);
            }
        });
    }

    public void deleteAll(){
        ImRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                homeImBaseDao.deleteAll();
            }
        });
    }
//
//    public LiveData<List<ChatHomeModel>> queryAllChatHomeMessage(){
//       return homeImBaseDao.getAllChatHomeMessage();
//    }

    /**
     * 更新首页数据，主要是更新未读消息数
     * 当前用户的Id
     */
    public void updateHomeMessage(HomeImBaseMode homeImBaseMode,boolean isAddUndReadNum,String userId){
        ImRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
//                HomeImBaseMode homeImBaseModeTemp;
//                int unReadNum = 0;
//                if ("1".equals(homeImBaseMode.getType())){//单聊
//                    String friendId = "";
//                    if (userId.equals(homeImBaseMode.getFromUserId())){
//                        friendId = homeImBaseMode.getToUserId();
//                    }else {
//                        friendId = homeImBaseMode.getFromUserId();
//                    }
//                    homeImBaseModeTemp = homeImBaseDao.queryHomeMessageModelByFriendId(friendId);
//                }else {//群聊
//                    homeImBaseModeTemp = homeImBaseDao.queryHomeMessageModelByGroupId(homeImBaseMode.getGroupId());
//                }
//
//                if (null != homeImBaseModeTemp){//假如存在数据,则删除该条数据
//                    unReadNum = homeImBaseModeTemp.getUnReadNumber();
//                    Log.e("YM","首页消息删除之前数量统计:"+homeImBaseDao.countHomeMessage());
//                    homeImBaseDao.deleteForServerMessageId(homeImBaseModeTemp.getMsgIdServer());
//                    Log.e("YM","数据查询的结果ServerId:"+homeImBaseModeTemp.toString());
//                    Log.e("YM","首页消息删除之后数量统计:"+homeImBaseDao.countHomeMessage());
//                }else {
//                    Log.e("YM","数据查询的结果:无数据");
//                }
//                if (isAddUndReadNum){
//                    unReadNum = unReadNum + 1;
//                }
//                homeImBaseMode.setUnReadNumber(unReadNum);
//                homeImBaseDao.insert(homeImBaseMode);
                homeImBaseDao.updateHomeMessage(homeImBaseMode,isAddUndReadNum,userId);
            }
        });
    }

    /**
     * 更新首页数据的消息ID，这个ID是通过ClientId更新ServerId
     * 当前用户的Id,不支持同步事务
     */
    public void updateHomeMessageServerId(HomeImBaseMode homeImBaseMode,boolean isAddUndReadNum,String userId){
        ImRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                HomeImBaseMode homeImBaseModeTemp;
                int unReadNum = 0;
                if ("1".equals(homeImBaseMode.getType())){//单聊
                    String friendId = "";
                    if (userId.equals(homeImBaseMode.getFromUserId())){
                        friendId = homeImBaseMode.getToUserId();
                    }else {
                        friendId = homeImBaseMode.getFromUserId();
                    }
                    homeImBaseModeTemp = homeImBaseDao.queryHomeMessageModelByFriendId(friendId);
                }else {//群聊
                    homeImBaseModeTemp = homeImBaseDao.queryHomeMessageModelByGroupId(homeImBaseMode.getGroupId());
                }

                if (null != homeImBaseModeTemp){//假如存在数据,则删除该条数据
                    unReadNum = homeImBaseModeTemp.getUnReadNumber();
                    homeImBaseDao.deleteForClientMessageId(homeImBaseModeTemp.getMsgIdClient());
                }else {
                }
                if (isAddUndReadNum){
                    unReadNum = unReadNum + 1;
                }
                homeImBaseMode.setUnReadNumber(unReadNum);
                homeImBaseDao.insert(homeImBaseMode);
            }
        });
    }
}