package com.wd.daquan.common.bean;

import java.io.Serializable;

/**
 * Created by DELL on 2018/7/3.
 */

public class CollectionUploadMsgBean implements Serializable {
    private static final long serialVersionUID = -1L;
    //上传
    public String fromId;
    public String messageId;
    public String fileName;
    //公共
    public String type;
    public String content;
    public String extra;
    //获取列表
    public String collection_id;
    public String from_uid;
    public String from_nickname;
    public String create_time;


}
