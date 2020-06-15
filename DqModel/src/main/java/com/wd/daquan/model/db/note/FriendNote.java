package com.wd.daquan.model.db.note;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

@Entity
public class FriendNote {

    @Id(autoincrement = true)
    public Long id;//主键

    /**
     * unique唯一标识
     */
    @Index(unique = true)
    public String uid; //id

    public String phone; //电话
    public String headpic; //头像
    public String nickname; //用户名
    public String sex; // 1:男 2:女
    public String dq_num; // 斗圈号
    public String whether_friend; // 是否是好友关系 0 非1 是
    public String whether_black; // 黑名单 0 非1 是
    public String screenshot_notify;
    public String isVip;//是否是vip 0：不是vip 1：是vip
    public String vipStartTime;//vip开始时间
    public String vipEndTime;//vip结束时间
    public String burn; // 阅后即焚
    public String top; // 置顶
    public String message_mute; // 消息免打擾
    public String strjson; // 好友信息描述
    public String time; //
    public String pinyin; //好友拼音
    public String friend_remark; //好友昵称
    @Generated(hash = 1523256790)
    public FriendNote(Long id, String uid, String phone, String headpic,
            String nickname, String sex, String dq_num, String whether_friend,
            String whether_black, String screenshot_notify, String isVip,
            String vipStartTime, String vipEndTime, String burn, String top,
            String message_mute, String strjson, String time, String pinyin,
            String friend_remark) {
        this.id = id;
        this.uid = uid;
        this.phone = phone;
        this.headpic = headpic;
        this.nickname = nickname;
        this.sex = sex;
        this.dq_num = dq_num;
        this.whether_friend = whether_friend;
        this.whether_black = whether_black;
        this.screenshot_notify = screenshot_notify;
        this.isVip = isVip;
        this.vipStartTime = vipStartTime;
        this.vipEndTime = vipEndTime;
        this.burn = burn;
        this.top = top;
        this.message_mute = message_mute;
        this.strjson = strjson;
        this.time = time;
        this.pinyin = pinyin;
        this.friend_remark = friend_remark;
    }
    @Generated(hash = 1335243118)
    public FriendNote() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUid() {
        return this.uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getHeadpic() {
        return this.headpic;
    }
    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getDq_num() {
        return this.dq_num;
    }
    public void setDq_num(String dq_num) {
        this.dq_num = dq_num;
    }
    public String getWhether_friend() {
        return this.whether_friend;
    }
    public void setWhether_friend(String whether_friend) {
        this.whether_friend = whether_friend;
    }
    public String getWhether_black() {
        return this.whether_black;
    }
    public void setWhether_black(String whether_black) {
        this.whether_black = whether_black;
    }
    public String getScreenshot_notify() {
        return this.screenshot_notify;
    }
    public void setScreenshot_notify(String screenshot_notify) {
        this.screenshot_notify = screenshot_notify;
    }
    public String getBurn() {
        return this.burn;
    }
    public void setBurn(String burn) {
        this.burn = burn;
    }
    public String getTop() {
        return this.top;
    }
    public void setTop(String top) {
        this.top = top;
    }
    public String getMessage_mute() {
        return this.message_mute;
    }
    public void setMessage_mute(String message_mute) {
        this.message_mute = message_mute;
    }
    public String getStrjson() {
        return this.strjson;
    }
    public void setStrjson(String strjson) {
        this.strjson = strjson;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getPinyin() {
        return this.pinyin;
    }
    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
    public String getFriend_remark() {
        return this.friend_remark;
    }
    public void setFriend_remark(String friend_remark) {
        this.friend_remark = friend_remark;
    }
    public String getIsVip() {
        return this.isVip;
    }
    public void setIsVip(String isVip) {
        this.isVip = isVip;
    }
    public String getVipStartTime() {
        return this.vipStartTime;
    }
    public void setVipStartTime(String vipStartTime) {
        this.vipStartTime = vipStartTime;
    }
    public String getVipEndTime() {
        return this.vipEndTime;
    }
    public void setVipEndTime(String vipEndTime) {
        this.vipEndTime = vipEndTime;
    }

}
