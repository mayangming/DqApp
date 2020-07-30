package com.dq.im.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dq.im.model.TeamModel;
import com.dq.im.repository.TeamRepository;

import java.util.List;

public class TeamViewModel extends AndroidViewModel {
    private TeamRepository mRepository;

    private LiveData<List<TeamModel>> mAllTeam;

    public TeamViewModel(Application application) {
        super(application);
        mRepository = new TeamRepository(application);
        mAllTeam = mRepository.getAllTeam();
    }

    public LiveData<List<TeamModel>> getAllTeam() { return mAllTeam; }

    public LiveData<TeamModel> getTeamByTeamId(String teamId) { return mRepository.getTeamByTeamId(teamId); }

    public void insert(TeamModel teamModel) { mRepository.insert(teamModel); }

    public void insert(List<TeamModel> teamModels) { mRepository.insert(teamModels); }

    public void update(TeamModel teamModel) { mRepository.update(teamModel); }

    public boolean hasTeam(String groupId){
        return mRepository.hasTeam(groupId) == 1;
    }

    public void updateNameForId(TeamModel word) { mRepository.updateNameById(word); }
}