package com.dq.im.model;


import androidx.room.Entity;

/**
 * 个人IM消息模型
 * 创建多个主键
 */

@Entity(tableName = "person_message",primaryKeys = {"msgIdClient","msgIdServer"})
public class P2PMessageBaseModel extends ImMessageBaseModel{
}