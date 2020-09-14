package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 签到规则
 */
public class SignRuleEntity implements Serializable {
    /**
     * id
     */
    private long id;

    /**
     * 规则
     */
    private String rule;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}