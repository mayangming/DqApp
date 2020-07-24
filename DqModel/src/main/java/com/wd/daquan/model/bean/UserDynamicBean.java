package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 获取朋友圈配置的bean
 */
public class UserDynamicBean implements Serializable {

    /**
     * userId : 492484774594609152
     * desc : 快乐每一天
     * pic :
     * updateTime : 1595385881329
     * status : 1
     */

    private String userId;
    private String desc;
    private String pic;
    private long updateTime;
    private String status;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}