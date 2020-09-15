package com.wd.daquan.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 签到实体类
 */
public class SignUpEntity implements Serializable {

    /**
     * 个人签到记录
     */
    private SignUserEntity dbUserSign = new SignUserEntity();
    /**
     * 每日金额
     */
   private List<SignAwardEntity> list = new ArrayList<>();
    /**
     * 签到规则
     */
//   private List<SignRuleEntity> dbRule = new ArrayList<>();
    private String dbRule;
    /**
     * 是不是第一次签到
     */
    private int isSign = 0;//0: 第一次 1:不是第一次
    public SignUserEntity getDbUserSign() {
        return dbUserSign;
    }

    public void setDbUserSign(SignUserEntity dbUserSign) {
        this.dbUserSign = dbUserSign;
    }

    public List<SignAwardEntity> getList() {
        return list;
    }

    public void setList(List<SignAwardEntity> list) {
        this.list = list;
    }

    public String getDbRule() {
        return dbRule;
    }

    public void setDbRule(String dbRule) {
        this.dbRule = dbRule;
    }

    public boolean getIsSign() {
        return isSign == 0;
    }

    public void setIsSign(int isSign) {
        this.isSign = isSign;
    }
}