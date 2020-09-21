package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 邀请会员时候获取的奖励内容
 */
public class DqIntviteRewardEntity implements Serializable {
    /**
     * result : 邀请好友规则
     * min : 100
     * max : 990
     */

    private String result;
    private String min;
    private String max;
    private InviteFriendEntity personInviteAward;
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public InviteFriendEntity getPersonInviteAward() {
        return personInviteAward;
    }

    public void setPersonInviteAward(InviteFriendEntity personInviteAward) {
        this.personInviteAward = personInviteAward;
    }
}