package com.wd.daquan.model.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 完成的任务详情
 */
public class ReleaseCompleteDetailsBean {
    private List<TaskCompleteBean> list = new ArrayList<>();

    private SendTaskBean task = new SendTaskBean();
    private int total = 0;

    public void setList(List<TaskCompleteBean> list) {
        this.list = list;
    }

    public List<TaskCompleteBean> getList() {
        return list;
    }

    public SendTaskBean getTask() {
        return task;
    }

    public void setTask(SendTaskBean task) {
        this.task = task;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * 单个任务情况
     */
    public static class TaskCompleteBean{
        private String userId = "";
        private String taskId = "";
        private String taskPic = "";
        private String headpic = "";
        private String nickName = "";
        private String douquanNum = "";
        private String reviewTime = "";

        public String getHeadpic() {
            return headpic;
        }

        public void setHeadpic(String headpic) {
            this.headpic = headpic;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getDouquanNum() {
            return douquanNum;
        }

        public void setDouquanNum(String douquanNum) {
            this.douquanNum = douquanNum;
        }

        public String getReviewTime() {
            return reviewTime;
        }

        public void setReviewTime(String reviewTime) {
            this.reviewTime = reviewTime;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getTaskPic() {
            return taskPic;
        }

        public void setTaskPic(String taskPic) {
            this.taskPic = taskPic;
        }
    }
}