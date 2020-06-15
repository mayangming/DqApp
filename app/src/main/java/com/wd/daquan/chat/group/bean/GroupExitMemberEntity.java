package com.wd.daquan.chat.group.bean;

/**
 * 退群成员详情
 * Created by Kind on 2018/9/10.
 */

public class GroupExitMemberEntity {

    /**
     nickName ：退群成员昵称
     operator_name：操作人昵称
     type 1：自己退出 2：被退出
     operator_role 操作人角色，2：群主 1：管理员
     quit_time 退群时间（秒级时间戳）
     source 进群方式
     2018/05/21 新增字段：uid，operator_uid
     */

    public String uid;
    public String nickname;
    public String headpic;
    public String operator_uid;
    public String operator_name;
    public String type;
    public String operator_role;
    public long quit_time;
    public String source;


    @Override
    public String toString() {
        return "GroupExitMemberEntity{" +
                "uid='" + uid + '\'' +
                ", nickName='" + nickname + '\'' +
                ", headpic='" + headpic + '\'' +
                ", operator_uid='" + operator_uid + '\'' +
                ", operator_name='" + operator_name + '\'' +
                ", type=" + type +
                ", operator_role=" + operator_role +
                ", quit_time=" + quit_time +
                ", source='" + source + '\'' +
                '}';
    }
}
