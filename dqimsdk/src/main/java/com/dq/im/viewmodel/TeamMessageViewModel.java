package com.dq.im.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dq.im.model.ChatTeamModel;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.model.TeamMessageBaseModel;
import com.dq.im.repository.TeamMessageRepository;

import java.util.List;

/**
 * 群聊数据模型
 * 数据保存类
 * LiveData中在数据更改时候，原先的数据会同步修改
 */
public class TeamMessageViewModel extends AndroidViewModel {
    private TeamMessageRepository teamMessageRepository;
    private LiveData<List<TeamMessageBaseModel>> imBaseModels;
    public TeamMessageViewModel(@NonNull Application application) {
        super(application);
        teamMessageRepository = new TeamMessageRepository(application);
        imBaseModels = teamMessageRepository.getAllMessage();
    }

    private LiveData<List<TeamMessageBaseModel>> getAllMessage(){
        return imBaseModels;
    }

//    /**
//     * 获取群聊消息
//     * 根据发信人ID和收信人ID进行查询聊天历史记录
//     * @return 查询的历史记录
//     */
//    private LiveData<List<TeamMessageBaseModel>> getTeamMessageByFromIdAndToId(String teamId){
//        return teamMessageRepository.getMessageByTeamId(teamId);
//    }
    /**
     * 获取群聊消息
     * 根据群ID查询聊天记录
     * @return 查询的历史记录
     */
    public List<TeamMessageBaseModel> getTeamMessageByFromGroupID(String teamId, int limitCount, long createTime){
        return teamMessageRepository.getMessageByTeamId(teamId,createTime,limitCount);
    }

    /**
     * 获取群聊消息
     * 根据发信人ID和收信人ID进行查询聊天历史记录
     * @param teamId 群组ID
     * @return 查询的历史记录
     */
    public LiveData<List<ChatTeamModel>> getMessageAndUserByTeamId(String teamId){
        return teamMessageRepository.getMessageAndUserByTeamId(teamId);
    }

    public LiveData<TeamMessageBaseModel> getTeamMessageByMessageId(String msgServerId){
        return teamMessageRepository.getTeamMessageByMessageId(msgServerId);
    }

    public void insert(TeamMessageBaseModel teamMessageBaseModel){
        teamMessageRepository.insert(teamMessageBaseModel);
    }

    public void insert(List<TeamMessageBaseModel> teamMessageBaseModel){
        teamMessageRepository.insert(teamMessageBaseModel);
    }

    public void updateMessageSendStatus(TeamMessageBaseModel teamMessageBaseModel){
        teamMessageRepository.updateMessageSendStatus(teamMessageBaseModel);
    }

    public void updateTeamPMessageByClientId(ImMessageBaseModel imMessageBaseModel){
        teamMessageRepository.updateTeamPMessageByClientId(imMessageBaseModel);
    }

    public void update(TeamMessageBaseModel teamMessageBaseModel){
        teamMessageRepository.update(teamMessageBaseModel);
    }

    public void deleteAll(){
        teamMessageRepository.deleteAll();
    }
    public void deleteMessageForGroupId(String groupId){
        teamMessageRepository.deleteMessageForGroupId(groupId);
    }

    public void deleteMessageForClientId(String clientId){
        teamMessageRepository.deleteMessageForClientId(clientId);
    }
}