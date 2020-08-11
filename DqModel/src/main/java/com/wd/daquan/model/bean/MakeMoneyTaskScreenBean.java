package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 赚钱功能筛选条目实体类
 */
public class MakeMoneyTaskScreenBean implements Serializable{
    private String taskId = "1";
    private String taskName = "集赞";
    private boolean isSelect = false;
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "MakeMoneyTaskScreenBean{" +
                "taskId='" + taskId + '\'' +
                ", taskName='" + taskName + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }
}
