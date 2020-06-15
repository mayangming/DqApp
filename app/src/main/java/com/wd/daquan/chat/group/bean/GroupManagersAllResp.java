package com.wd.daquan.chat.group.bean;


import java.io.Serializable;
import java.util.List;


public class GroupManagersAllResp implements Serializable {
    private static final long serialVersionUID = -1L;
    public GroupManagersItemResp master;
    public List<GroupManagersItemResp> admin;

}
