package com.dq.im.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dq.im.model.TeamModel;
import com.dq.im.model.TeamUserJoinModel;
import com.dq.im.model.UserModel;
import com.dq.im.repository.TeamUserRepository;

import java.util.List;

/**
 * 群组关系关联模型
 */
public class TeamUserJoinViewModel extends AndroidViewModel {
    private TeamUserRepository teamUserRepository;
    public TeamUserJoinViewModel(@NonNull Application application) {
        super(application);
        teamUserRepository = new TeamUserRepository(application);
    }

    /**
     * 添加新的群组信息
     */
    public void insert(TeamUserJoinModel teamUserJoinModel){
       teamUserRepository.insert(teamUserJoinModel);
    }

    /**
     * 批量添加新的群组信息
     */
    public void insert(List<TeamUserJoinModel> teamUserJoinModelList){
       teamUserRepository.insert(teamUserJoinModelList);
    }

    /**
     * 根据用户Id查询其所拥有的群组
     */
    public LiveData<List<TeamModel>> getTeamModelsForUserId(String userId){
        return teamUserRepository.getTeamForUserId(userId);
    }

    /**
     * 根据群组Id查询其拥有的成员
     */
    public LiveData<List<UserModel>> getUserModelsForTeamId(String teamId){
        return teamUserRepository.getUserForTeamId(teamId);
    }

}