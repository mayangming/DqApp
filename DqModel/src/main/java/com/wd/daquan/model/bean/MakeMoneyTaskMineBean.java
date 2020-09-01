package com.wd.daquan.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MakeMoneyTaskMineBean implements Serializable {
    private String id = "1";
    private String taskId = "1";
    private String typePic = "";//平台图标
    private String taskname = "下载应用";//任务名称
    private long taskmoney = 100;//任务价格
    private long getTime = 1L;//任务报名时间
    private int taskStatus = 1;//任务状态 0:未接取 1:已接取 2:待审核 3:审核通过结算成功 4:审核通过结算失败 5:审核失败重新提交
    private String classification = "";//任务分类
    private String type = "";//任务类型
    private List<String> taskLabelList = new ArrayList<>();//任务标签

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTypePic() {
        return typePic;
    }

    public void setTypePic(String typePic) {
        this.typePic = typePic;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public long getTaskmoney() {
        return taskmoney;
    }

    public void setTaskmoney(long taskmoney) {
        this.taskmoney = taskmoney;
    }

    public long getGetTime() {
        return getTime;
    }

    public void setGetTime(long getTime) {
        this.getTime = getTime;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public List<String> getTaskLabelList() {
        return taskLabelList;
    }

    public void setTaskLabelList(List<String> taskLabelList) {
        this.taskLabelList = taskLabelList;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}