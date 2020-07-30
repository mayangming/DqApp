package com.dq.im.model;


import androidx.room.Embedded;
import androidx.room.Ignore;

/**
 * 用来处理创建群组时候的用户数据
 */
public class GroupTeamUserModel {
    @Ignore
    private boolean isChecked;

    @Embedded
    private UserModel userModel;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public String toString() {
        return "GroupTeamUserModel{" +
                "isChecked=" + isChecked +
                ", userModel=" + userModel +
                '}';
    }
}