package com.wd.daquan.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务实体类
 */
public class SendTaskBean implements Serializable {
    private String id = "";//任务Id
    private String classification = "";//任务分类
    private String type = "";//任务类型
    private int priority = 0;//任务优先级
    private long createtime = 1L;//任务创建时间
    private String createuserid = "";//创建人Id
    private long extime = 1L;//任务到期时间
    private int expires = 0;//是否过期0:未过期1:已过期
    private String taskexplain = "";//任务说明
    private String reviewtime = "";//审核时间
    private String taskname = "支付宝下载任务";
    private String taskpic = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1596195164793&di=e8024d595a842c080cf5a0a07c1c0e14&imgtype=0&src=http%3A%2F%2Fpic.feizl.com%2Fupload%2Fallimg%2F170615%2F1TH010Z-7.jpg";
    private String typePic = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1596195164793&di=e8024d595a842c080cf5a0a07c1c0e14&imgtype=0&src=http%3A%2F%2Fpic.feizl.com%2Fupload%2Fallimg%2F170615%2F1TH010Z-7.jpg";
    private String typeName = "";//平台名字
    private String className = "";//任务类型名字
    private long taskmoney = 100;//赏金
    private int classnum =100;//任务总数
    private int lastnum = 3;
    private int isPass = 0;//审核状态 0:待提交审核 1:审核中 2:通过 3:拒绝
    private int isPay = 0;//是否付款  0:未付款 1:付款 2:申请退款 3:退款成功 4:退款拒绝 5:退款失败
    private String reason = "审核结果";//审核结果
    private List<String> taskLabelList = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getTaskpic() {
        return taskpic;
    }

    public void setTaskpic(String taskpic) {
        this.taskpic = taskpic;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
    }

    public long getExtime() {
        return extime;
    }

    public void setExtime(long extime) {
        this.extime = extime;
    }

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }

    public String getTaskexplain() {
        return taskexplain;
    }

    public void setTaskexplain(String taskexplain) {
        this.taskexplain = taskexplain;
    }

    public String getReviewtime() {
        return reviewtime;
    }

    public void setReviewtime(String reviewtime) {
        this.reviewtime = reviewtime;
    }

    public int getIsPass() {
        return isPass;
    }

    public void setIsPass(int isPass) {
        this.isPass = isPass;
    }

    public int getIsPay() {
        return isPay;
    }

    public void setIsPay(int isPay) {
        this.isPay = isPay;
    }

    public String getTypePic() {
        return typePic;
    }

    public void setTypePic(String typePic) {
        this.typePic = typePic;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}