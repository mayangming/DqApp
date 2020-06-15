package com.wd.daquan.contacts.bean;

import java.io.Serializable;

/**
 * Created by DELL on 2019/1/22.
 */

public class CommRespEntity implements Serializable {

    private static final long serialVersionUID = 1511301104047660728L;
    public String examine;//当前群开启了群审核

    public boolean isExamine() {
        return "1".equals(examine);
    }
}
