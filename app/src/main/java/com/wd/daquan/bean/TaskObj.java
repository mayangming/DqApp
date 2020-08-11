package com.wd.daquan.bean;

import com.wd.daquan.model.bean.TaskClassificationBean;
import com.wd.daquan.model.bean.TaskTypeBean;

import java.io.Serializable;
import java.util.List;

public class TaskObj implements Serializable{
    private List<TaskTypeBean> taskTypeBeans;
    private List<TaskClassificationBean> taskClassificationBeans;

    public List<TaskTypeBean> getTaskTypeBeans() {
        return taskTypeBeans;
    }

    public void setTaskTypeBeans(List<TaskTypeBean> taskTypeBeans) {
        this.taskTypeBeans = taskTypeBeans;
    }

    public List<TaskClassificationBean> getTaskClassificationBeans() {
        return taskClassificationBeans;
    }

    public void setTaskClassificationBeans(List<TaskClassificationBean> taskClassificationBeans) {
        this.taskClassificationBeans = taskClassificationBeans;
    }
}
