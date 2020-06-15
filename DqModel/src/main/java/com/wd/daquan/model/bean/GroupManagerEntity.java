package com.wd.daquan.model.bean;

import android.text.TextUtils;

import java.util.List;

/**
 * @author: dukangkang
 * @date: 2018/5/16 10:14.
 * @description: todo ...
 */
public class GroupManagerEntity {
    public String admin_num;
    public String examine; // 是否开启群认证 1:开启 0:关闭
    public String is_protect_groupuser; // 是否开启群成员保护 1:开启 0:关闭
    public String group_number;
    public String create_uid;
    public List<InvitedEntity> apply_list;
    public String role;//2：群主 1：管理员
    public String is_allow_receive_redpacket;//是否开启群长时间未领红包 0表示禁用，1表示开启
//    public PluginBean plugin;

    public boolean isHasAdmin() {
        return !TextUtils.isEmpty(admin_num) && !"0".equals(admin_num);
    }

    public boolean isExamine() {
        return "0".equals(examine);
    }


    public static class InvitedEntity {
        public String inviter_nickname;
        public String the_inviter_nickname;
        public String status;
        public String headpic;
        public String request_id;
    }
}
