package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 厂商类型
 */
public class TaskTypeBean implements Serializable {
    private int id = 1;//类型Id
    private String typeName = "";//类型名字
    private String typePic = "";//厂商名字
    private boolean isSelect = false;//是否选择
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypePic() {
        return typePic;
    }

    public void setTypePic(String typePic) {
        this.typePic = typePic;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}