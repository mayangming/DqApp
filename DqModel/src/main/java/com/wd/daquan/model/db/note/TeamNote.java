package com.wd.daquan.model.db.note;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class TeamNote {

    @Id(autoincrement = true)
    public Long id;//主键

    /**
     * unique唯一标识
     */
    @Index(unique = true)
    public String group_id;//群id

    public String group_name;//群昵称
    public String group_pic;//群头像
    public String announcement;//群公告
    public String create_uid;//创建人ID
    public String group_member_num;//群成员数量
    public String role;//群成角色 0成员1管理员2群主
    public String status;//群状态：0群正常，1群解散，2群封禁
    public String within_group; //当前用户是否在群组，1不在，0在
    public String examine; //是否开启群认证，1没开，0开启
    public String group_type; //群类型
    public String message_mute; //消息免打扰0开 1关
    public String remarks; //群备注
    public String screenshot_notify; //截屏通知 0开 1关
    public String burn; // 阅后即焚 0开 1关
    public String saved_team; // 保存到通讯录 0开 1关
    public String top; // 置顶 0开 1关
    public String str_json; // 群个人信息json
    @Generated(hash = 1972444342)
    public TeamNote(Long id, String group_id, String group_name, String group_pic,
            String announcement, String create_uid, String group_member_num,
            String role, String status, String within_group, String examine,
            String group_type, String message_mute, String remarks,
            String screenshot_notify, String burn, String saved_team, String top,
            String str_json) {
        this.id = id;
        this.group_id = group_id;
        this.group_name = group_name;
        this.group_pic = group_pic;
        this.announcement = announcement;
        this.create_uid = create_uid;
        this.group_member_num = group_member_num;
        this.role = role;
        this.status = status;
        this.within_group = within_group;
        this.examine = examine;
        this.group_type = group_type;
        this.message_mute = message_mute;
        this.remarks = remarks;
        this.screenshot_notify = screenshot_notify;
        this.burn = burn;
        this.saved_team = saved_team;
        this.top = top;
        this.str_json = str_json;
    }
    @Generated(hash = 394312114)
    public TeamNote() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getGroup_id() {
        return this.group_id;
    }
    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }
    public String getGroup_name() {
        return this.group_name;
    }
    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }
    public String getGroup_pic() {
        return this.group_pic;
    }
    public void setGroup_pic(String group_pic) {
        this.group_pic = group_pic;
    }
    public String getAnnouncement() {
        return this.announcement;
    }
    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }
    public String getCreate_uid() {
        return this.create_uid;
    }
    public void setCreate_uid(String create_uid) {
        this.create_uid = create_uid;
    }
    public String getGroup_member_num() {
        return this.group_member_num;
    }
    public void setGroup_member_num(String group_member_num) {
        this.group_member_num = group_member_num;
    }
    public String getRole() {
        return this.role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getWithin_group() {
        return this.within_group;
    }
    public void setWithin_group(String within_group) {
        this.within_group = within_group;
    }
    public String getExamine() {
        return this.examine;
    }
    public void setExamine(String examine) {
        this.examine = examine;
    }
    public String getGroup_type() {
        return this.group_type;
    }
    public void setGroup_type(String group_type) {
        this.group_type = group_type;
    }
    public String getMessage_mute() {
        return this.message_mute;
    }
    public void setMessage_mute(String message_mute) {
        this.message_mute = message_mute;
    }
    public String getRemarks() {
        return this.remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
    public String getSaved_team() {
        return this.saved_team;
    }
    public void setSaved_team(String saved_team) {
        this.saved_team = saved_team;
    }
    public String getTop() {
        return this.top;
    }
    public void setTop(String top) {
        this.top = top;
    }
    public String getStr_json() {
        return this.str_json;
    }
    public void setStr_json(String str_json) {
        this.str_json = str_json;
    }

}
