package com.wd.daquan.model.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 群成员信息
 * Created by Kind on 2018/9/14.
 */

public class GroupMemberBean implements Parcelable, Serializable {

    public String groupID;
    public String groupNickname;
    public String uid;
    public String nickname;
    public String headpic;
    public String sex;//
    /**
     * group/get_userinfo 返回的role 2：群主 1：管理员 0：普通成员
     *  group/get_userinfo 返回的 own_master，master等同
     */
    public String role;//2：群主 1：管理员 0：普通成员//
    public String phone;//
    public String remarks;//remarks和gremarks是一样的，//群备注
    public String friend_remarks;//好友备注
    //public String gremarks;
    public String dq_num;//
    public long time;
    public String source;//群主
    public int tmpMemberShowStatus = 1;//1.普通显示，2.加号3.减号 临时用的

    public String alipay_account;
    public String wechat_account;
    public List<String> description;//描述

    /**
     * 获取群成员信息返回的
     */
    public String is_protect_groupuser;//是否保护群
    public String whether_friend;//是否好友 1是好友，0不是
    public String whether_black;//是否拉黑 1 是黑名单，0不是
    public String own_master;//当前请求接口人的角色。
    public String target_master;
    public String master;
    public String type;
    public String source_uid;// 代表谁邀请他进群的。
    public String is_join;// 该用户在禁止收发红包人员列表状态，0在，1不在

    public String isVip;//是否是vip 0：不是vip 1：是vip
    public String vipStartTime;//vip开始时间
    public String vipEndTime;//vip结束时间

    public enum GrouppostType {
        LORD, //群主
        ADMIN, //群主
        MEMBER, //成员
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setGroupNickname(String groupNickname) {
        this.groupNickname = groupNickname;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public GrouppostType getRoleType() {//2：群主 1：管理员 0：普通成员
        GrouppostType grouppostType;
        switch (role) {
            case "2":
                grouppostType = GrouppostType.LORD;
                break;
            case "1":
                grouppostType = GrouppostType.ADMIN;
                break;
            default:
                grouppostType = GrouppostType.MEMBER;
                break;
        }
        return grouppostType;
    }

    /**
     * 群内显示规则
     * @return
     */
    public String getGroupDisplayName(){
        if(!TextUtils.isEmpty(remarks)){
            return remarks;
        }

        if(!TextUtils.isEmpty(nickname)){
            return nickname;
        }

        return uid;
    }

    /**
     * 管理员以上返回tue
     *
     * @return
     */
    public boolean isHighRole() {//2：群主 1：管理员 0：普通成员
        return ("2".equals(role) || "1".equals(role));
    }

    public boolean isGroupMaster() {//2：群主
        return "2".equals(role);
    }

    public boolean isAdmin() {//2：管理员
        return "1".equals(role);
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setDq_num(String dq_num) {
        this.dq_num = dq_num;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isVip() {
        return (!TextUtils.isEmpty(isVip) && "1".equals(isVip));
    }

    public String getVipStartTime() {
        return vipStartTime;
    }

    public void setVipStartTime(String vipStartTime) {
        this.vipStartTime = vipStartTime;
    }

    public String getVipEndTime() {
        return vipEndTime;
    }

    public void setVipEndTime(String vipEndTime) {
        this.vipEndTime = vipEndTime;
    }

    public GroupMemberBean(int tmpMemberShowStatus) {
        this.tmpMemberShowStatus = tmpMemberShowStatus;
    }

    public GroupMemberBean(String groupID, String uid, String nickName, String headPic) {
        this.groupID = groupID;
        this.uid = uid;
        this.nickname = nickName;
        this.headpic = headPic;
    }

    public GroupMemberBean(String groupID, String uid, String remarks) {
        this.groupID = groupID;
        this.uid = uid;
        this.remarks = remarks;
    }

    public void setGroupMemberInfo(String uid, String nickName, String headPic, String qingtalk_num) {
        this.uid = uid;
        this.nickname = nickName;
        this.headpic = headPic;
        this.dq_num = qingtalk_num;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.groupID);
        dest.writeString(this.groupNickname);
        dest.writeString(this.uid);
        dest.writeString(this.nickname);
        dest.writeString(this.headpic);
        dest.writeString(this.sex);
        dest.writeString(this.role);
        dest.writeString(this.phone);
        dest.writeString(this.remarks);
        dest.writeString(this.friend_remarks);
        dest.writeString(this.dq_num);
        dest.writeLong(this.time);
        dest.writeString(this.source);
        dest.writeInt(this.tmpMemberShowStatus);
        dest.writeString(this.alipay_account);
        dest.writeString(this.wechat_account);
        dest.writeStringList(this.description);
        dest.writeString(this.is_protect_groupuser);
        dest.writeString(this.whether_friend);
        dest.writeString(this.whether_black);
        dest.writeString(this.own_master);
        dest.writeString(this.target_master);
        dest.writeString(this.type);
        dest.writeString(this.source_uid);
        dest.writeString(this.master);
        dest.writeString(this.isVip);
        dest.writeString(this.vipStartTime);
        dest.writeString(this.vipEndTime);
    }

    public GroupMemberBean() {
    }

    protected GroupMemberBean(Parcel in) {
        this.groupID = in.readString();
        this.groupNickname = in.readString();
        this.uid = in.readString();
        this.nickname = in.readString();
        this.headpic = in.readString();
        this.sex = in.readString();
        this.role = in.readString();
        this.phone = in.readString();
        this.remarks = in.readString();
        this.friend_remarks = in.readString();
        this.dq_num = in.readString();
        this.time = in.readLong();
        this.source = in.readString();
        this.tmpMemberShowStatus = in.readInt();
        this.alipay_account = in.readString();
        this.wechat_account = in.readString();
        this.description = in.createStringArrayList();
        this.is_protect_groupuser = in.readString();
        this.whether_friend = in.readString();
        this.whether_black = in.readString();
        this.own_master = in.readString();
        this.target_master = in.readString();
        this.type = in.readString();
        this.source_uid = in.readString();
        this.master = in.readString();
        this.isVip = in.readString();
        this.vipStartTime = in.readString();
        this.vipEndTime = in.readString();
    }

    public static final Creator<GroupMemberBean> CREATOR = new Creator<GroupMemberBean>() {
        @Override
        public GroupMemberBean createFromParcel(Parcel source) {
            return new GroupMemberBean(source);
        }

        @Override
        public GroupMemberBean[] newArray(int size) {
            return new GroupMemberBean[size];
        }
    };
}
