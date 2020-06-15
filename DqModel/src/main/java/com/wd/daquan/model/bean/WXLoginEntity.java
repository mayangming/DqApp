package com.wd.daquan.model.bean;

import java.io.Serializable;

public class WXLoginEntity implements Serializable {
    private static final long serialVersionUID = 3330369656982792155L;

    public String phone;
    public String openid;
    public String wx_nickname;
    public String wx_head_icon;
    public int status;
}
