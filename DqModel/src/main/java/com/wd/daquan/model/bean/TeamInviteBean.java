package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * @author: dukangkang
 * @date: 2019/2/22 18:05.
 * @description: todo ...
 */
public class TeamInviteBean implements Serializable {
    /**
     * requestId : fef66f8aafad4a3eb6095c4cf53ca63e
     * inviterUid : 462032037558091776
     * responseStatus : 0
     * gid : 2706320250
     * groupName : DQ_3203079168,DQ...
     * icon : http://39.106.158.176/headpic/group/469985933203079168/478698336220610560.jpg
     */

    public String requestId;
    public String nickName;
    public String inviterUid;
    public String inviterName;
    public String responseStatus;
    public String gid;
    public String groupName;
    public String icon;


//    public String request_id;
//    public String invite_uid;
//    public String receive_uid;
//    public String create_time;
//    public String update_time;
//    public String status;//用户响应状态 （0:等待处理 1:同意 2:忽略 3:删除）
//    public String invite_nickname;//邀请人昵称
//    public String group_name;//群组名称
//    public String group_pic;//群头像


}
