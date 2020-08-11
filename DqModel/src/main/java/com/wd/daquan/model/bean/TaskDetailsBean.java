package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 任务详情实体类
 */
public class TaskDetailsBean implements Serializable {
    private int id = 0;
    private String userId;
    private String getTime;//接任务时间
    private String taskpic = "";
    private String taskId = "";
    private int expires = 0;//0: 未过期 1::已过期
    private long taskmoney = 0;//任务金额
    private int classnum = 0;//任务总数
    private int lastnum = 0;//剩余数量
    private long payTime = 0;//付钱时间
    private long extime = 0;//任务结束时间
    private String reviewtime = "";//审核时间
    private String taskexplain = "";//任务说明
    private String teachingVideoUrl = "https://dq-oss.oss-cn-beijing.aliyuncs.com/jxsp/1596695847655706.mp4";//演示视频
    private int taskStatus = 0;//任务状态 0:未接取 1:已接取 2:待审核 3:审核通过结算成功 4:审核通过结算失败 5:审核失败重新提交
    private String taskPicNew;//任务凭证
    private String reviewUser;//审核人
    private int payStatus;//结算状态 0:结算成功 1:结算失败

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskpic() {
        return taskpic;
    }

    public void setTaskpic(String taskpic) {
        this.taskpic = taskpic;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public long getTaskmoney() {
        return taskmoney;
    }

    public void setTaskmoney(long taskmoney) {
        this.taskmoney = taskmoney;
    }

    public int getClassnum() {
        return classnum;
    }

    public void setClassnum(int classnum) {
        this.classnum = classnum;
    }

    public int getLastnum() {
        return lastnum;
    }

    public void setLastnum(int lastnum) {
        this.lastnum = lastnum;
    }

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }

    public String getReviewtime() {
        return reviewtime;
    }

    public void setReviewtime(String reviewtime) {
        this.reviewtime = reviewtime;
    }

    public String getTaskexplain() {
        return taskexplain;
    }

    public void setTaskexplain(String taskexplain) {
        this.taskexplain = taskexplain;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTeachingVideoUrl() {
        return teachingVideoUrl;
    }

    public void setTeachingVideoUrl(String teachingVideoUrl) {
        this.teachingVideoUrl = teachingVideoUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGetTime() {
        return getTime;
    }

    public void setGetTime(String getTime) {
        this.getTime = getTime;
    }

    public String getTaskPicNew() {
        return taskPicNew;
    }

    public void setTaskPicNew(String taskPicNew) {
        this.taskPicNew = taskPicNew;
    }

    public String getReviewUser() {
        return reviewUser;
    }

    public void setReviewUser(String reviewUser) {
        this.reviewUser = reviewUser;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }

    public long getExtime() {
        return extime;
    }

    public void setExtime(long extime) {
        this.extime = extime;
    }
}