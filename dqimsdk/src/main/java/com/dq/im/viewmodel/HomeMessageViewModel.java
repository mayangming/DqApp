package com.dq.im.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dq.im.db.ImRoomDatabase;
import com.dq.im.model.HomeImBaseMode;
import com.dq.im.repository.HomeMessageRepository;

import java.util.List;

/**
 * 数据保存类
 * LiveData中在数据更改时候，原先的数据会同步修改
 */
public class HomeMessageViewModel extends AndroidViewModel {
    private HomeMessageRepository homeMessageRepository;
    private LiveData<List<HomeImBaseMode>> homeImBaseModels;

    public HomeMessageViewModel(@NonNull Application application) {
        super(application);
        homeMessageRepository = new HomeMessageRepository(application);
        homeImBaseModels = homeMessageRepository.getAllMessage();
    }

    public LiveData<List<HomeImBaseMode>> getAllMessage(){
        return homeImBaseModels;
    }

    public void insert(HomeImBaseMode homeImBaseModel){
        homeMessageRepository.insert(homeImBaseModel);
    }

    public void insert(List<HomeImBaseMode> homeImBaseModel){
        homeMessageRepository.insert(homeImBaseModel);
    }

    public void update(HomeImBaseMode homeImBaseModel){
        homeMessageRepository.update(homeImBaseModel);
    }
    public void updateTeFriendUnReadNumber(String friendId, int unReadNum){
        homeMessageRepository.updateTeFriendUnReadNumber(friendId,unReadNum);
    }
    public void updateTeamUnReadNumber(String groupId, int unReadNum){
        homeMessageRepository.updateTeamUnReadNumber(groupId,unReadNum);
    }

    public void deleteForServerMessageId(String msgServerId){
        homeMessageRepository.deleteForServerMessageId(msgServerId);
    }

    public void deleteForUserId(String userId){
        homeMessageRepository.deleteForUserId(userId);
    }

    public void deleteForUserId(HomeImBaseMode homeImBaseMode){
        String userId = ImRoomDatabase.getUserId();
        String friendId = "";
        if (userId.equals(homeImBaseMode.getFromUserId())){
            friendId = homeImBaseMode.getToUserId();
        }else {
            friendId = homeImBaseMode.getFromUserId();
        }

        homeMessageRepository.deleteForUserId(friendId);
    }

    public void deleteForFriendId(String friendId){
        homeMessageRepository.deleteForFriendId(friendId);
    }

    public void deleteForGroupId(String groupId){
        homeMessageRepository.deleteForGroupId(groupId);
    }

    public void deleteAll(){
        homeMessageRepository.deleteAll();
    }

    public void updateHomeMessage(HomeImBaseMode homeImBaseMode,boolean isAddUndReadNum){
        String userId = ImRoomDatabase.getUserId();
        homeMessageRepository.updateHomeMessage(homeImBaseMode,isAddUndReadNum,userId);
    }

//    /**
//     * 通过客户端ID进行数据更新
//     * @param homeImBaseMode
//     * @param isAddUndReadNum
//     */
//    public void updateHomeMessageForClientId(HomeImBaseMode homeImBaseMode,boolean isAddUndReadNum){
//        String userId = ImRoomDatabase.getUserId();
//        homeMessageRepository.updateHomeMessageServerId(homeImBaseMode,isAddUndReadNum,userId);
//    }

//    public LiveData<List<ChatHomeModel>> queryAllChatHomeMessage(){
//        return homeMessageRepository.queryAllChatHomeMessage();
//    }

}