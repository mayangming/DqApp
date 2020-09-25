package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 积分兑换页面商城的内容
 */
public class IntegralMallEntity implements Serializable {
    private String id = "";
    private String pic = "";
    private String name = "";
    private long integral = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getIntegral() {
        return integral;
    }

    public void setIntegral(long integral) {
        this.integral = integral;
    }
}