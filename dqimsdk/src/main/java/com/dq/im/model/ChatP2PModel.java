package com.dq.im.model;


import androidx.room.Embedded;

/**
 * 个人聊天数据模型
 * 该模型中包含聊天数据内容、用户信息内容
 * 用户信息是对方的，自己的信息不从数据库取
 */
public class ChatP2PModel {

    /**
     * Embedded注解用于将压缩的数据结构解析到数据表中
     */
    @Embedded
    private P2PMessageBaseModel p2PMessageBaseModel;

//    @ColumnInfo(name = "self_user")

    @Embedded
    private UserModel userModel;

    public P2PMessageBaseModel getP2PMessageBaseModel() {
        return p2PMessageBaseModel;
    }

    public void setP2PMessageBaseModel(P2PMessageBaseModel p2PMessageBaseModel) {
        this.p2PMessageBaseModel = p2PMessageBaseModel;
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
                "p2PMessageBaseModel=" + p2PMessageBaseModel +
                ", userModel=" + userModel +
                '}';
    }
}