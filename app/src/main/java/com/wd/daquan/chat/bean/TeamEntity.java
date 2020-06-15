package com.wd.daquan.chat.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: dukangkang
 * @date: 2018/9/17 13:48.
 * @description: todo ...
 */
public class TeamEntity implements Serializable{
    public long timestamp;
    public String operatorNickname;
    public String targetGroupName;
    public List<String> targetUserDisplayNames = new ArrayList<>();
    public List<String> targetUserIds = new ArrayList<>();
    public String oldCreatorId;
    public String oldCreatorName;
    public String newCreatorId;
    public String newCreatorName;
    public String groupPic;
}
