package com.wd.daquan.model.bean;

import android.text.TextUtils;


public class WxBindBean {
    /**
     * openid : oPTSP1jmW4EW5FGfKkYVNwFfYPVo
     * phone : 15549433151
     * weixin_nickname : 方志
     * status : 1
     * is_registered_phone : 1
     */

//    public String is_registered_phone;
    public String nickName;//用户名
    public String headpic;//用户头像

    /**
     * id : 55
     * uid : 490532115830538240
     * imToken : 4cd6680d115ea3e8d115de3ff7dc8950
     * phone : 18310400074
     * token : 85962e996410cf954f8800602e8081a2
     * password :
     * sex : 1
     * descriptions :
     * userType :
     * userStatus : 0 //用户状态：0正常 1错误密码冻结 2封禁
     * isAdmin : 0
     * allowSearch : 0
     * msgNotify : 0
     * newsDetail : 0
     * addedFriendWhetherV : 1
     * allowDouquanSearch : 0
     * douquanNum :
     * poke : 0
     * groupValidate : 0
     * regTime : 2019-11-08T11:19:58.000+0000
     * lastLoginTime : 2019-11-08T11:19:58.000+0000
     * updateTime : 2019-11-08T11:19:58.000+0000
     * city :
     * wechatOpenid : o8NDewDVGWwoW9deHecnu1e756fs
     * province :
     * country : AD
     */

    private int id;
    private String uid;
    private String imToken;
    private String phone;
    private String token;
    private String password;
    private String sex;
    private String descriptions;
    private String userType;
    private String userStatus;
    private String isAdmin;
    private String allowSearch;
    private String msgNotify;
    private String newsDetail;
    private String addedFriendWhetherV;
    private String allowDouquanSearch;
    private String douquanNum;
    private String poke;
    private String groupValidate;
    private String regTime;
    private String lastLoginTime;
    private String updateTime;
    private String city;
    private String wechatOpenid;
    private String province;
    private String country;
    private String isVip;//是否是vip 0：不是vip 1：是vip
    private String vipStartTime;//vip开始时间
    private String vipEndTime;//vip结束时间
    private int shareTotalNum;//分享总人数
    private String redEnvelopedRainRemind;//红包雨活动介绍
    private String dbMoney;//斗币数量
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImToken() {
        return imToken;
    }

    public void setImToken(String imToken) {
        this.imToken = imToken;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getAllowSearch() {
        return allowSearch;
    }

    public void setAllowSearch(String allowSearch) {
        this.allowSearch = allowSearch;
    }

    public String getMsgNotify() {
        return msgNotify;
    }

    public void setMsgNotify(String msgNotify) {
        this.msgNotify = msgNotify;
    }

    public String getNewsDetail() {
        return newsDetail;
    }

    public void setNewsDetail(String newsDetail) {
        this.newsDetail = newsDetail;
    }

    public String getAddedFriendWhetherV() {
        return addedFriendWhetherV;
    }

    public void setAddedFriendWhetherV(String addedFriendWhetherV) {
        this.addedFriendWhetherV = addedFriendWhetherV;
    }

    public String getAllowDouquanSearch() {
        return allowDouquanSearch;
    }

    public void setAllowDouquanSearch(String allowDouquanSearch) {
        this.allowDouquanSearch = allowDouquanSearch;
    }

    public String getDouquanNum() {
        return douquanNum;
    }

    public void setDouquanNum(String douquanNum) {
        this.douquanNum = douquanNum;
    }

    public String getPoke() {
        return poke;
    }

    public void setPoke(String poke) {
        this.poke = poke;
    }

    public String getGroupValidate() {
        return groupValidate;
    }

    public void setGroupValidate(String groupValidate) {
        this.groupValidate = groupValidate;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWechatOpenid() {
        return wechatOpenid;
    }

    public void setWechatOpenid(String wechatOpenid) {
        this.wechatOpenid = wechatOpenid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isVip() {
        return (!TextUtils.isEmpty(isVip) && "1".equals(isVip));
    }

    public String getIsVip() {
        return isVip;
    }

    public void setIsVip(String isVip) {
        this.isVip = isVip;
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

    public int getShareTotalNum() {
        return shareTotalNum;
    }

    public void setShareTotalNum(int shareTotalNum) {
        this.shareTotalNum = shareTotalNum;
    }

    public String getRedEnvelopedRainRemind() {
        return redEnvelopedRainRemind;
    }

    public void setRedEnvelopedRainRemind(String redEnvelopedRainRemind) {
        this.redEnvelopedRainRemind = redEnvelopedRainRemind;
    }

    public String getDbMoney() {
        return dbMoney;
    }

    public void setDbMoney(String dbMoney) {
        this.dbMoney = dbMoney;
    }
}