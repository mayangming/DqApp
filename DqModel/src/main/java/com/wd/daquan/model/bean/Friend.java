package com.wd.daquan.model.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.wd.daquan.model.interfaces.ISelect;

import java.util.ArrayList;

/**
 * @Author: 方志
 * @Time: 2018/9/6 11:39
 * @Description: 联系人数据
 * "data": {
 * "uid": "100013",
 * "nickName": "方志",
 * "sex": 0,
 * "dq_num": "",
 * "whether_friend": 1,
 * "whether_black": 0,
 * "screenshot_notify": 0,
 * "burn": 0,
 * "remarks": "",  区别
 * "friend_remarks": "", 区别
 * "headpic": "http:\/\/oss.meetsn.com\/upload\/628f8d8c174aad1b21b72448ed30eb72.jpeg"
 * }
 *
 */
public class Friend implements ISelect, Parcelable {

    public String uid; //id
    public String phone; //电话
    public String headpic; //头像
    public String nickname; //用户名
    public String sex; // 1:男 2:女
    public String dq_num; // 项目号
    public String whether_friend; // 是否是好友关系 0 不是 1 是
    public String whether_black; // 黑名单 0 是1 否
    public String screenshot_notify;
    public String burn; // 阅后即焚,时间为0关闭
    public String top; // 置顶
    public String isVip;//是否是vip 0：不是vip 1：是vip
    public String vipStartTime;//vip开始时间
    public String vipEndTime;//vip结束时间
    public String descriptions;
    public String card;//备注图片
    public ArrayList<String> phones;


    /**
     * user/get_userinfo  获取其他用户信息  remarks,friend_remarks二个字段是一样的
     * group/get_userinfo 群组内指定用户信息查询 remarks群内的备注，friend_remarks好友备注
     */
    public String remarks;//群组中自己的备注
    public String friend_remarks;//好友备注

    public String pinYin; // 拼音
    public String source; // 进群方式
    public String is_protect_groupuser; // 是否是群保护 0 非1 是
    public String message_mute;//静音
    public long time;

    public String role;//2：群主 1：管理员 0：普通成员
    private boolean isSelected = false;

    // 选择好友的时候使用
    public String letters;

    public String target_master;//目标用户在群里的身份，2群主；1管理员；0普通成员）
    public String own_master;//自己用户在群里的身份，2群主；1管理员；0普通成员）
    public String is_join;//1 表示该用户已经在禁止收发红包人员列表； 0 表示该用户不在禁止收发红包人员列表
//    public String disPlayName = "";//用户名字显示的规则，优先级： 自己对好友的备注 > 好友自己的备注 > 好友的昵称
    public String getUid() {
        return uid == null ? "" : uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getPinYin() {
        return pinYin == null ? "" : pinYin;
    }
    public boolean isTop() {
        return "0".equals(top);
    }

    public boolean isBurn() {
        return burn != null && !"0".equals(burn);
    }

    public boolean isWhether_friend() {

        return "1".equals(whether_friend);
    }
    public boolean isWhether_black() {
        return "0".equals(whether_black);
    }
    public boolean isMute() {
        return "0".equals(message_mute);
    }

    public long getBurn() {
        return burn == null  ? 0 : Long.parseLong(burn);
    }

    public boolean isVip(){
       return  (!TextUtils.isEmpty(isVip) && "1".equals(isVip));
    }

    /**
     * 获取名称
     * @return
     */
    public String getName(){
        if(!TextUtils.isEmpty(remarks)){
            return remarks;
        }

        if(!TextUtils.isEmpty(friend_remarks)){
            return friend_remarks;
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

    /**
     * 获取@成员名称
     * @return
     */
    public String getAitName() {
        if(!TextUtils.isEmpty(nickname)){
            return nickname;
        }
        return uid;
    }

    public String getTarget_master() {
        return target_master == null ? "" : target_master;
    }

    public String getOwn_master() {
        return own_master == null ? "" : own_master;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getFriend_remarks() {
        return friend_remarks == null ? "" : friend_remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setWhether_friend(String whether_friend) {
        this.whether_friend = whether_friend;
    }

    public void setWhether_black(String whether_black) {
        this.whether_black = whether_black;
    }

    public void setBurn(String burn) {
        this.burn = burn;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isScreenshot_notify() {
        return "1".equals(screenshot_notify);
    }

    public void setScreenshot_notify(String screenshot_notify) {
        this.screenshot_notify = screenshot_notify;
    }

    public void setMessage_mute(String message_mute) {
        this.message_mute = message_mute;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public boolean isContain(String target) {
        String name = getName();
        if (!TextUtils.isEmpty(name)) {
            if (name.contains(target)) {
                return true;
            }
        }

        if (!TextUtils.isEmpty(phone)) {
            if (phone.contains(target)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.role);
        dest.writeString(this.phone);
        dest.writeString(this.headpic);
        dest.writeString(this.nickname);
        dest.writeString(this.remarks);
        dest.writeString(this.friend_remarks);
        dest.writeString(this.pinYin);
        dest.writeString(this.sex);
        dest.writeString(this.dq_num);
        dest.writeString(this.whether_friend);
        dest.writeString(this.whether_black);
        dest.writeString(this.burn);
        dest.writeString(this.source);
        dest.writeString(this.is_protect_groupuser);
        dest.writeString(this.screenshot_notify);
        dest.writeString(this.message_mute);
        dest.writeLong(this.time);
        dest.writeString(this.letters);
        dest.writeString(this.isVip);
        dest.writeString(this.vipStartTime);
        dest.writeString(this.vipEndTime);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public Friend() {
    }

    public Friend(String uid) {
        this.uid = uid;
    }

    public Friend(String uid, String nickname, String headpic) {
        this.uid = uid;
        this.nickname = nickname;
        this.headpic = headpic;
    }

    public Friend(String uid, String nickname, String headpic, String remarks, String phone, boolean isSelected) {
        this.uid = uid;
        this.nickname = nickname;
        this.headpic = headpic;
        this.remarks = remarks;
        this.phone = phone;
        this.isSelected = isSelected;
    }

    protected Friend(Parcel in) {
        this.uid = in.readString();
        this.role = in.readString();
        this.phone = in.readString();
        this.headpic = in.readString();
        this.nickname = in.readString();
        this.remarks = in.readString();
        this.friend_remarks = in.readString();
        this.pinYin = in.readString();
        this.sex = in.readString();
        this.dq_num = in.readString();
        this.whether_friend = in.readString();
        this.whether_black = in.readString();
        this.burn = in.readString();
        this.source = in.readString();
        this.is_protect_groupuser = in.readString();
        this.screenshot_notify = in.readString();
        this.message_mute = in.readString();
        this.time = in.readLong();
        this.letters = in.readString();
        this.isVip = in.readString();
        this.vipStartTime = in.readString();
        this.vipEndTime = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<Friend> CREATOR = new Creator<Friend>() {
        @Override
        public Friend createFromParcel(Parcel source) {
            return new Friend(source);
        }

        @Override
        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };

    @Override
    public String toString() {
        return "Friend{" +
                "uid='" + uid + '\'' +
                ", phone='" + phone + '\'' +
                ", headpic='" + headpic + '\'' +
                ", nickName='" + nickname + '\'' +
                ", isVip='" + isVip + '\'' +
                ", startTime='" + vipStartTime + '\'' +
                '}';
    }

    public boolean isFriend() {
        return "1".equals(whether_friend);
    }
}
