package com.wd.daquan.model.bean;

/**
 * @Author: 方志
 * @Time: 2018/9/10 15:56
 * @Description:
 */
public class LoginBean {

    /**
     * phone : 13111111111
     * token : bf80017105d9ed37395ac923c6ad1c44
     * uid : 9999
     * nickName : u720973
     * headpic : http://t.file.sy.qingic.com/upload/head_pic_default.png
     * sex : 1
     * regTime : 2018-05-30
     * imToken :
     * allowSearch : 0
     * msgNotify : 0
     * newsDetail : 0
     * setPwd : 0
     * addedFriendWhetherV : 0
     * allowDouquanSearch : 0
     * dq_num :
     *"userType": 1,
     */

    public String phone;// 手机号
    public String token;// 斗圈token
    public String uid;//用户id
    public String nickName;//用户名
    public String headpic;//用户头像
    public String sex;//用户性别
    public String regTime;
    public String imToken;//云信token
    public String userType;
    public String allowSearch;
    public String msgNotify;
    public String newsDetail;
    public String setPwd;
    public String password = "";//用户密码
    public String addedFriendWhetherV;//是否开始好友认证0开1关
    public String allowDouquanSearch;
    public String douquanNum;
    public String jrmfToken;//金融魔方token

    // 接受戳一下消息
    public String poke;
    // 是否允许被添加群聊
    public String groupValidate;
    public String isVip;//是否是vip 0：不是vip 1：是vip
    public String vipStartTime;//vip开始时间
    public String vipEndTime;//vip结束时间


    @Override
    public String toString() {
        return "LoginBean{" +
                "phone='" + phone + '\'' +
                ", token='" + token + '\'' +
                ", uid='" + uid + '\'' +
                ", nickName='" + nickName + '\'' +
                ", headpic='" + headpic + '\'' +
                ", sex='" + sex + '\'' +
                ", regTime='" + regTime + '\'' +
                ", imToken ='" + imToken + '\'' +
                ", allowSearch='" + allowSearch + '\'' +
                ", msgNotify='" + msgNotify + '\'' +
                ", newsDetail='" + newsDetail + '\'' +
                ", setPwd='" + setPwd + '\'' +
                ", addedFriendWhetherV='" + addedFriendWhetherV + '\'' +
                ", allow_chuiniu_search='" + allowDouquanSearch + '\'' +
                ", chuiniu_num='" + douquanNum + '\'' +
                '}';
    }
}
