package com.dq.im.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dq.im.model.ChatP2PModel;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.repository.P2PMessageRepository;

import java.util.List;

/**
 * 单聊数据模型
 * 数据保存类
 * LiveData中在数据更改时候，原先的数据会同步修改
 */
public class P2PMessageViewModel extends AndroidViewModel {
    private P2PMessageRepository p2PMessageRepository;
    private LiveData<List<P2PMessageBaseModel>> imBaseModels;

    public P2PMessageViewModel(@NonNull Application application) {
        super(application);
        p2PMessageRepository = new P2PMessageRepository(application);
        imBaseModels = p2PMessageRepository.getAllMessage();
    }

    public LiveData<List<P2PMessageBaseModel>> getAllMessage(){
        return imBaseModels;
    }

    public LiveData<List<ChatP2PModel>> getChatModels(String userId){
        return p2PMessageRepository.getChatModels(userId);
    }

    public LiveData<List<ChatP2PModel>> getChatModels(){
        return p2PMessageRepository.getChatModels();
    }

//    /**
//     * 获取单聊消息
//     * 根据根据好友Id进行查询
//     * @param fromId 发信人
//     * @return 查询的历史记录
//     */
//    public LiveData<List<P2PMessageBaseModel>> getP2PMessageByFromIdAndToId(String friendId,int limitCount,String createTime){
//        return p2PMessageRepository.getMessageByFromIdAndToId(friendId,limitCount,createTime);
//    }

//    /**
//     * 获取单聊消息
//     * 根据根据好友Id进行查询
//     * @param fromId 发信人
//     * @return 查询的历史记录
//     */
//    public LiveData<List<P2PMessageBaseModel>> getP2PMessageByFromIdAndToId(String friendId,int limitCount){
//        return p2PMessageRepository.getMessageByFromIdAndToId(friendId,limitCount);
//    }
    /**
          * 获取单聊消息
          * 根据根据好友Id进行查询
          * @param friendId 发信人
          * @return 查询的历史记录
          */
    public List<P2PMessageBaseModel> getP2PMessageByFromIdAndToId(String friendId,int limitCount,long createTime){
        return p2PMessageRepository.getMessageByFromIdAndToId(friendId,limitCount,createTime);
    }
    /**
     * 获取单聊消息
     * 根据根据好友Id进行查询
     * @param friendId 发信人
     * @return 查询的历史记录
     */
    public List<P2PMessageBaseModel> getP2PMessageByFromIdAndToId(String friendId,int limitCount){
        return p2PMessageRepository.getMessageByFromIdAndToId(friendId,limitCount);
    }

    public void insert(P2PMessageBaseModel p2PMessageBaseModel){
        p2PMessageRepository.insert(p2PMessageBaseModel);
    }

    public void insert(List<P2PMessageBaseModel> p2PMessageBaseModels){
        p2PMessageRepository.insert(p2PMessageBaseModels);
    }

    public void update(P2PMessageBaseModel p2PMessageBaseModel){
        p2PMessageRepository.update(p2PMessageBaseModel);
    }

    public void updateP2PMessageByClientId(ImMessageBaseModel imMessageBaseModel){
        p2PMessageRepository.updateP2PMessageByClientId(imMessageBaseModel);
    }

    public void deleteAll(){
        p2PMessageRepository.deleteAll();
    }

    public void deleteForMessageClientId(String messageClientId){
        p2PMessageRepository.deleteForMessageClientId(messageClientId);
    }
    public void deleteMessageForFriendId(String friendId){
        p2PMessageRepository.deleteMessageForFriendId(friendId);
    }
}