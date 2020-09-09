package com.wd.daquan.model.bean;

import java.io.Serializable;
import java.text.NumberFormat;

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
    private int vip_amount;// 免手续费额度
    private int vip_total_amount;//免手续费总额度
    private double withdrawalRate;//提现手续费率

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

    public int getVip_amount() {
        return vip_amount;
    }

    public void setVip_amount(int vip_amount) {
        this.vip_amount = vip_amount;
    }

    public double getWithdrawalRate() {
        return withdrawalRate;
    }

    public void setWithdrawalRate(double withdrawalRate) {
        this.withdrawalRate = withdrawalRate;
    }

    public int getVip_total_amount() {
        return vip_total_amount;
    }

    public void setVip_total_amount(int vip_total_amount) {
        this.vip_total_amount = vip_total_amount;
    }

    /**
     * 显示百分比
     * @return
     */
    public String rate(){
        NumberFormat percentInstance = NumberFormat.getPercentInstance();
        percentInstance.setMaximumFractionDigits(2); // 保留小数两位
        String format = percentInstance.format(withdrawalRate); // 结果是81.25% ，最后一们四舍五入了
        return format;
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