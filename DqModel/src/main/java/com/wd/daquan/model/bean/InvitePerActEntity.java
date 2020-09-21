package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 邀请好友的好友信息
 */
public class InvitePerActEntity implements Serializable{
    /**
     * 用户名称
     */
    private String nickName;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户头像
     */
    private String userPic;
    /**
     * 用户距下次签到奖励天数
     */
    private int nextAwardDay;
    /**
     * 用户下次签到奖励金额
     */
    private int nextSignAward;
    /**
     * 用户是否完成签到奖励 0:未完成 1:已完成
     */
    private int isSignFinish;
    /**
     * 用户距下次提现奖励金额
     */
    private int nexWithdrawMoney;
    /**
     * 用户下次提现奖励金额
     */
    private int nextWithdrawAward;
    /**
     * 用户是否完成提现奖励 0:未完成 1:已完成
     */
    private int isWithdrawFinish;

    /**
     * 用户获得奖励总金额
     */
    private int totalAward;

    private String returnWord;//描述的文档

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public int getNextAwardDay() {
        return nextAwardDay;
    }

    public void setNextAwardDay(int nextAwardDay) {
        this.nextAwardDay = nextAwardDay;
    }

    public int getNextSignAward() {
        return nextSignAward;
    }

    public void setNextSignAward(int nextSignAward) {
        this.nextSignAward = nextSignAward;
    }

    public int getIsSignFinish() {
        return isSignFinish;
    }

    public void setIsSignFinish(int isSignFinish) {
        this.isSignFinish = isSignFinish;
    }

    public int getNexWithdrawMoney() {
        return nexWithdrawMoney;
    }

    public void setNexWithdrawMoney(int nexWithdrawMoney) {
        this.nexWithdrawMoney = nexWithdrawMoney;
    }

    public int getNextWithdrawAward() {
        return nextWithdrawAward;
    }

    public void setNextWithdrawAward(int nextWithdrawAward) {
        this.nextWithdrawAward = nextWithdrawAward;
    }

    public int getIsWithdrawFinish() {
        return isWithdrawFinish;
    }

    public void setIsWithdrawFinish(int isWithdrawFinish) {
        this.isWithdrawFinish = isWithdrawFinish;
    }

    public int getTotalAward() {
        return totalAward;
    }

    public void setTotalAward(int totalAward) {
        this.totalAward = totalAward;
    }

    public String getReturnWord() {
        return returnWord;
    }

    public void setReturnWord(String returnWord) {
        this.returnWord = returnWord;
    }
}