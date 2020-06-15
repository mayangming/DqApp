package com.wd.daquan.model.db.note;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MemberNote {

    @Id(autoincrement = true)
    public Long id;//主键

    /**
     * unique唯一标识
     */
    @Index(unique = true)
    public String key;//

    public String group_id;//群id
    public String uid;//群成员id
    public String phone;//群成员手机号
    public String nickname;//群成员手机号
    public String headpic;//群成员头像
    public String role;//群成角色 0成员1管理员2群主
    public String sex;
    public String dq_num;
    public String remarks;//群昵称备注
    public String friend_remarks;//当前用户对群成员(好友)的备注
    public String whether_friend; // 是否是好友关系 0 非1 是
    public String whether_black; // 黑名单 0 非1 是
    public String source; // 进群方式
    public String is_join; // 	该用户在禁止收发红包人员列表状态，0在，1不在
    @Generated(hash = 482524955)
    public MemberNote(Long id, String key, String group_id, String uid,
            String phone, String nickname, String headpic, String role, String sex,
            String dq_num, String remarks, String friend_remarks,
            String whether_friend, String whether_black, String source,
            String is_join) {
        this.id = id;
        this.key = key;
        this.group_id = group_id;
        this.uid = uid;
        this.phone = phone;
        this.nickname = nickname;
        this.headpic = headpic;
        this.role = role;
        this.sex = sex;
        this.dq_num = dq_num;
        this.remarks = remarks;
        this.friend_remarks = friend_remarks;
        this.whether_friend = whether_friend;
        this.whether_black = whether_black;
        this.source = source;
        this.is_join = is_join;
    }
    @Generated(hash = 2067383432)
    public MemberNote() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getKey() {
        return this.key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getGroup_id() {
        return this.group_id;
    }
    public void setGroup_id(String group_id) {
        this.group_id = group_id;
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
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getHeadpic() {
        return this.headpic;
    }
    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }
    public String getRole() {
        return this.role;
    }
    public void setRole(String role) {
        this.role = role;
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
    public String getRemarks() {
        return this.remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public String getFriend_remarks() {
        return this.friend_remarks;
    }
    public void setFriend_remarks(String friend_remarks) {
        this.friend_remarks = friend_remarks;
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
    public String getSource() {
        return this.source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getIs_join() {
        return this.is_join;
    }
    public void setIs_join(String is_join) {
        this.is_join = is_join;
    }
}
