package com.wd.daquan.model.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class SaveUserDynamicDescBean implements Serializable {

    /**
     * dynamicId : 675985807688859648
     * userId : 492467245323124736
     * desc : aade76cf070d3a53916120c9eee47289
     * pics : ["aade76cf070d3a53916120c9eee47289"]
     * createTime : 1595319787881
     * updateTime : 1595319787881
     * status : 1
     * type : 1
     * userDynamicCommentDataList : []
     * userDynamicLikeDataList : []
     */

    private long dynamicId;
    private String userId;
    private String desc;
    private long createTime;
    private long updateTime;
    private String status;
    private String type;
    private ArrayList<String> pics;

    public long getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(long dynamicId) {
        this.dynamicId = dynamicId;
    }

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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getPics() {
        return pics;
    }

    public void setPics(ArrayList<String> pics) {
        this.pics = pics;
    }
}