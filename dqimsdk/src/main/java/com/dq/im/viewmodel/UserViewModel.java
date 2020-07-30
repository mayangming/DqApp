package com.dq.im.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dq.im.model.GroupTeamUserModel;
import com.dq.im.model.UserModel;
import com.dq.im.repository.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private UserRepository mRepository;

    private LiveData<List<UserModel>> mAllUser;

    public UserViewModel(Application application) {
        super(application);
        mRepository = new UserRepository(application);
        mAllUser = mRepository.getAllUser();
    }

    public boolean hasFriend(String friendId){
        return mRepository.hasFriend(friendId) == 1;
    }

    public LiveData<List<UserModel>> getAllUser() { return mAllUser; }

    public LiveData<List<GroupTeamUserModel>> getAllUserAboutCreateTeamForUser() { return mRepository.getAllUserAboutCreateTeamForUser(); }

    public LiveData<UserModel> getUserByUserId(String userId) { return mRepository.getUserByUserId(userId); }

    public void insert(UserModel user) { mRepository.insert(user); }

    public void insert(List<UserModel> user) { mRepository.insert(user); }

    public void update(UserModel user) { mRepository.update(user); }

    public void updateNameForId(UserModel user) { mRepository.updateNameById(user); }
}