package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * Created by DELL on 2019/1/22.
 */

public class CommRespEntity implements Serializable {

    private static final long serialVersionUID = 1511301104047660728L;
    public String examine;//当前群开启了群审核
    public String requestId;//该请求的ID
    public String status;//响应状态

    public boolean isExamine() {
        return "0".equals(examine);
    }

    @Override
    public String toString() {
        return "CommRespEntity{" +
                "examine='" + examine + '\'' +
                ", requestId='" + requestId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
