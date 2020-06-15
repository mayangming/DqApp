package com.wd.daquan.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 群信息
 * Created by Kind on 2018/9/13.
 * "create_uid": 101,
 * "group_pic": "http:\/\/toss.meetsn.com\/upload\/cf62bceedb02f0dbbcb4a415b749d23b.jpeg",
 * "group_name": "斗圈官方团队、dkk1、知足常乐...",
 * "announcement": null,
 * "screenshot_notify": 0,
 * "burn": 0,
 * "gossip": 0,
 * "group_number": null,
 * "create_time": "2018-09-18 19:26:21",
 * "am_update_time": "0000-00-00 00:00:00",
 * "examine": 0,
 * "status": 1,
 * "is_protect_groupuser": 0,
 * "group_id": "1397493832",
 * "group_type": 0,
 * "within_group": 1,
 * "group_member_num": 6,
 * "apply": 0,
 * "contacts": 0,
 * "remarks": "",
 */

public class GroupInfoBean extends TeamBean implements Parcelable {

    public String create_uid;
    public String create_time;
    public String remarks; //群备注
    public String gossip; //是否全员禁言（暂时没用）
    public String examine;//是否开启群认证消息免打扰0：开 1：关
    public String apply;
    public String group_type;
    public String announcement; //群公告
    public String am_update_time;
    public String screenshot_notify;
    public String burn;
    public String group_number;//群聊号目前只用于展示
    public String status;//群状态：0群正常，1群解散，2群封禁
    public String within_group; //当前用户是否在群组，1不在，0在
    public String strJson;
    public long time;

    public int banner_id;
    public String banner_title;
    public String banner_url;
    public int close_banner_id;
    public String is_allow_receive_redpacket;//1关，0开
    public String is_protect_groupuser;//是否开启群成员包保护 0：开 1：关
    public String message_mute;//消息免打扰0：开 1：关
    public String saved_team; //保存到通讯录0：开 1：关
    public String top; //会话置顶 0：开 1：关

    //新增字段
    public String plugin_id;//用于判断群是否安装插件 返回空则没有
    public String plugin_type;//插件类型 1：h5模式 2：暂无
    public String plugin_h5_url;//插件h5地址
    public String plugin_auth;//是否授权

    public String role;//2：群主 1：管理员 0：普通成员//

    public List<GroupMemberBean> memberList = new ArrayList<>();

    /**
     * 保存到通信录
     */
    public boolean isSaveTeam() {
        return "0".equals(saved_team);
    }


    /**
     * 1.开启截屏
     * @return
     */
    public boolean isScreenshot_notify() {
        return "0".equals(screenshot_notify);
    }

    public void setScreenshot_notify(String screenshot_notify) {
        this.screenshot_notify = screenshot_notify;
    }

    public boolean isBurn() {
        return "0".equals(burn);
    }
    public boolean isWithIn() {
        return "0".equals(within_group);
    }

    public String getStatus() {
        return status == null ? "" : status;
    }
    public boolean isExamine() {
        return "0".equals(examine);
    }

    public List<GroupMemberBean> getMemberList() {
        if(memberList == null){
            memberList = new ArrayList<>();
        }
        return memberList;
    }

    public GroupInfoBean(String id) {
        this.group_id = id;
    }
    public GroupInfoBean() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.create_uid);
        dest.writeString(this.create_time);
        dest.writeString(this.remarks);
        dest.writeString(this.gossip);
        dest.writeString(this.examine);
        dest.writeString(this.apply);
        dest.writeString(this.group_type);
        dest.writeString(this.announcement);
        dest.writeString(this.am_update_time);
        dest.writeString(this.screenshot_notify);
        dest.writeString(this.burn);
        dest.writeString(this.group_number);
        dest.writeString(this.status);
        dest.writeString(this.within_group);
        dest.writeString(this.strJson);
        dest.writeLong(this.time);
        dest.writeInt(this.banner_id);
        dest.writeString(this.banner_title);
        dest.writeString(this.banner_url);
        dest.writeInt(this.close_banner_id);
        dest.writeString(this.is_allow_receive_redpacket);
        dest.writeString(this.is_protect_groupuser);
        dest.writeString(this.message_mute);
        dest.writeString(this.saved_team);
        dest.writeString(this.top);
        dest.writeString(this.plugin_id);
        dest.writeString(this.plugin_type);
        dest.writeString(this.plugin_h5_url);
        dest.writeString(this.plugin_auth);
        dest.writeString(this.role);
        dest.writeTypedList(this.memberList);
    }

    protected GroupInfoBean(Parcel in) {
        this.create_uid = in.readString();
        this.create_time = in.readString();
        this.remarks = in.readString();
        this.gossip = in.readString();
        this.examine = in.readString();
        this.apply = in.readString();
        this.group_type = in.readString();
        this.announcement = in.readString();
        this.am_update_time = in.readString();
        this.screenshot_notify = in.readString();
        this.burn = in.readString();
        this.group_number = in.readString();
        this.status = in.readString();
        this.within_group = in.readString();
        this.strJson = in.readString();
        this.time = in.readLong();
        this.banner_id = in.readInt();
        this.banner_title = in.readString();
        this.banner_url = in.readString();
        this.close_banner_id = in.readInt();
        this.is_allow_receive_redpacket = in.readString();
        this.is_protect_groupuser = in.readString();
        this.message_mute = in.readString();
        this.saved_team = in.readString();
        this.top = in.readString();
        this.plugin_id = in.readString();
        this.plugin_type = in.readString();
        this.plugin_h5_url = in.readString();
        this.plugin_auth = in.readString();
        this.role = in.readString();
        this.memberList = in.createTypedArrayList(GroupMemberBean.CREATOR);
    }

    public static final Creator<GroupInfoBean> CREATOR = new Creator<GroupInfoBean>() {
        @Override
        public GroupInfoBean createFromParcel(Parcel source) {
            return new GroupInfoBean(source);
        }

        @Override
        public GroupInfoBean[] newArray(int size) {
            return new GroupInfoBean[size];
        }
    };

    @Override
    public String toString() {
        return "GroupInfoBean{" +
                "memberList=" + memberList +
                ", group_id='" + group_id + '\'' +
                ", group_name='" + group_name + '\'' +
                ", group_pic='" + group_pic + '\'' +
                ", group_member_num='" + group_member_num + '\'' +
                '}';
    }
}
