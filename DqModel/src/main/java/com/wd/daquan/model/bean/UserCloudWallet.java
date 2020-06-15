package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 用户零钱信息
 */
public class UserCloudWallet implements Serializable {

    /**
     * uid : 492655210338975744
     * balance : 400 余额
     * frozenBalance : 0 冻结余额
     * accumulatedIncome : 0 收入总额
     * pwdIsSet : true 是否设置提现密码
     */

    private String uid;
    private int balance;
    private int frozenBalance;
    private int accumulatedIncome;
    private boolean pwdIsSet;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getFrozenBalance() {
        return frozenBalance;
    }

    public void setFrozenBalance(int frozenBalance) {
        this.frozenBalance = frozenBalance;
    }

    public int getAccumulatedIncome() {
        return accumulatedIncome;
    }

    public void setAccumulatedIncome(int accumulatedIncome) {
        this.accumulatedIncome = accumulatedIncome;
    }

    public boolean isPwdIsSet() {
        return pwdIsSet;
    }

    public void setPwdIsSet(boolean pwdIsSet) {
        this.pwdIsSet = pwdIsSet;
    }

    @Override
    public String toString() {
        return "UserCloudWallet{" +
                "uid='" + uid + '\'' +
                ", balance=" + balance +
                ", frozenBalance=" + frozenBalance +
                ", accumulatedIncome=" + accumulatedIncome +
                ", pwdIsSet=" + pwdIsSet +
                '}';
    }
}