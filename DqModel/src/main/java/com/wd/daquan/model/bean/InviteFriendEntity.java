package com.wd.daquan.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 邀请好友的结果
 */
public class InviteFriendEntity implements Serializable {
    /**
     * 消息ID
     */
    private long id;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 被邀请人获得的奖励列表
     */
    private List<InvitePerActEntity> list = new ArrayList<>();
    /**
     * 被邀请人总数
     */
    private int invitedPersonNum;
    /**
     * 邀请人获得奖励总额
     */
    private int invitedTotalAward;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<InvitePerActEntity> getList() {
        return list;
    }

    public void setList(List<InvitePerActEntity> list) {
        this.list = list;
    }

    public int getInvitedPersonNum() {
        return invitedPersonNum;
    }

    public void setInvitedPersonNum(int invitedPersonNum) {
        this.invitedPersonNum = invitedPersonNum;
    }

    public int getInvitedTotalAward() {
        return invitedTotalAward;
    }

    public void setInvitedTotalAward(int invitedTotalAward) {
        this.invitedTotalAward = invitedTotalAward;
    }
}