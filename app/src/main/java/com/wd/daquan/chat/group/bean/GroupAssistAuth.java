package com.wd.daquan.chat.group.bean;

import java.io.Serializable;

/**
 * Created by DELL on 2019/1/7.
 */

public class GroupAssistAuth implements Serializable {

    private static final long serialVersionUID = 1361239579452869697L;
    public String auth;//true：不弹授权框 false：弹出授权框
    public GroupAssistAuthData auth_data;

    public class GroupAssistAuthData implements Serializable{

        private static final long serialVersionUID = -8372787161801843310L;
        public String plugin_id;
        public String group_id;
        public String unionid;
        public String owner_unionid;
        public String nonce;
        public String sign;
    }
}
