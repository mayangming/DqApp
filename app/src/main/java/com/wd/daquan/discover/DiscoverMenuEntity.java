package com.wd.daquan.discover;


import java.io.Serializable;


public class DiscoverMenuEntity implements Serializable {

    private static final long serialVersionUID = -1L;
    public String id;
    public String title;
    public String url;
    public String status;//status=1表示正常显示；status=2表示维护中，此时message字段就是弹窗中显示的维护信息；
    public String message;
}
