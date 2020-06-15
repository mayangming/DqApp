package com.dq.im.model;


import android.arch.persistence.room.Entity;

/**
 * 个人IM消息模型
 * 创建多个主键
 */

@Entity(tableName = "person_message",primaryKeys = {"msgIdClient","toUserId","fromUserId"})
public class P2PMessageBaseModel extends ImMessageBaseModel{
}