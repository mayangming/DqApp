package com.wd.daquan.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取朋友圈列表
 */
public class FindUserDynamicDescBean implements Serializable {

    /**
     * dynamicId : 675977653500510208
     * userId : 492484774594609152
     * desc : aade76cf070d3a53916120c9eee47289
     * pics : ["aade76cf070d3a53916120c9eee47289"]
     * createTime : 1595318815826
     * updateTime : 1595318815826
     * status : 1
     * type : 1
     * userDynamicCommentDataList : [{"commentId":676021531263893504,"dynamicId":675977653500510208,"userId":"492484774594609152","friendId":null,"desc":"675977653500510208","createTime":1595324046463,"type":"0"}]
     * userDynamicLikeDataList : [{"likeId":676016268611944448,"dynamicId":675977653500510208,"userId":"492484774594609152","createTime":1595323419106}]
     * userHeadPic : null
     * searchUserId : null
     * searchType : 0
     */

    private long dynamicId;
    private String userId;
    private String userNickName;
    private String desc;
    private long createTime;
    private long updateTime;
    private String status;
    private String type;
    private String userHeadPic;
    private String searchUserId;
    private int searchType;
    private ArrayList<String> pics;
    private ArrayList<UserDynamicCommentDataListBean> userDynamicCommentDataList;
    private ArrayList<UserDynamicLikeDataListBean> userDynamicLikeDataList;

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

    public String getUserHeadPic() {
        return userHeadPic;
    }

    public void setUserHeadPic(String userHeadPic) {
        this.userHeadPic = userHeadPic;
    }

    public String getSearchUserId() {
        return searchUserId;
    }

    public void setSearchUserId(String searchUserId) {
        this.searchUserId = searchUserId;
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public ArrayList<String> getPics() {
        return pics;
    }

    public void setPics(ArrayList<String> pics) {
        this.pics = pics;
    }

    public ArrayList<UserDynamicCommentDataListBean> getUserDynamicCommentDataList() {
        return userDynamicCommentDataList;
    }

    public void setUserDynamicCommentDataList(ArrayList<UserDynamicCommentDataListBean> userDynamicCommentDataList) {
        this.userDynamicCommentDataList = userDynamicCommentDataList;
    }

    public List<UserDynamicLikeDataListBean> getUserDynamicLikeDataList() {
        return userDynamicLikeDataList;
    }

    public void setUserDynamicLikeDataList(ArrayList<UserDynamicLikeDataListBean> userDynamicLikeDataList) {
        this.userDynamicLikeDataList = userDynamicLikeDataList;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }
}