package com.dq.im.model;


import androidx.room.Embedded;

/**
 * 群组聊天数据模型
 * 该模型中包含聊天数据内容、用户信息内容
 * 用户信息是对方的，自己的信息不从数据库取
 */
public class ChatTeamModel {

    /**
     * Embedded注解用于将压缩的数据结构解析到数据表中
     */
    @Embedded
    private TeamMessageBaseModel teamMessageBaseModel;

    @Embedded
    private UserModel userModel;

    public TeamMessageBaseModel getTeamMessageBaseModel() {
        return teamMessageBaseModel;
    }

    public void setTeamMessageBaseModel(TeamMessageBaseModel teamMessageBaseModel) {
        this.teamMessageBaseModel = teamMessageBaseModel;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public String toString() {
        return "ChatModel{" +
                "p2PMessageBaseModel=" + teamMessageBaseModel +
                ", userModel=" + userModel +
                '}';
    }
}