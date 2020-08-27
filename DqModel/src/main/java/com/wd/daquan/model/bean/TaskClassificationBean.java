package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 任务类型
 */
public class TaskClassificationBean implements Serializable{
    private int id = 1;//类型Id
    private String className = "";//类型名字
    private String classPic = "";//厂商名字
    private boolean isSelect = false;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getClassPic() {
        return classPic;
    }

    public void setClassPic(String classPic) {
        this.classPic = classPic;
    }
}