package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 未读消息内容
 */
public class AreaUnReadBean implements Serializable{

    /**
     * msgId : 687731871273975809
     * userId : 685959671499980800
     * createTime : 1596720027776
     * sendUserId : 583760536945033216
     * dynamicId : 678183517321953280
     * imgUrl : https://dq-oss.oss-cn-beijing.aliyuncs.com/dq_img_1595581775788.jpg
     * toUserId : 685959671499980800
     * dynamicCommendId : 687731871072649216
     * dynamicCommendMsg :
     * msgType : 1
     * status : 1
     * pageNum : 0
     * pageSize : 0
     * sendUserHeadPic : null
     * sendUserNickName : 0
     * toUserNickName : 0
     * msgIds : null
     * sendUser : {"id":81,"uid":"583760536945033216","imToken":"265eab88f30e3f629e2c9566cda70e3d","phone":"17600600113","token":"15e631715cebda021efd4167b1bb5cc3","nickName":"强强强","wechatNickName":"郝亚强","password":"","sex":"1","headpic":"http://39.106.158.176/headpic/583760536945033216/5837605369450332161590558630097.jpg","wechatHeadpic":"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKyNlAdcg5Q8DM3XNnMlgibzYnoQypeTmTH6ndm7CRHGKkYicico3DjGtf49RfxuDu7sMztP2dvfW6aw/132","descriptions":"","userType":"","userStatus":"1","isAdmin":"0","allowSearch":"0","msgNotify":"0","newsDetail":"0","addedFriendWhetherV":"1","allowDouquanSearch":"0","douquanNum":"q13121325943","poke":"0","groupValidate":"1","regTime":"2020-03-16T02:27:59.000+0000","lastLoginTime":"2020-06-23T17:06:47.000+0000","updateTime":"2020-08-06T02:57:19.311+0000","city":null,"wechatOpenid":"o8NDewFFztZJJHhYt9iGedxp6JiE","province":null,"country":null,"jrmfToken":"62ecbe151da81be1802c828350399e58","userLv":null,"userIp":null,"userEdition":null,"isBindWechat":"1","isVip":"1","vipStartTime":"2020-08-05T09:04:45.208+0000","vipEndTime":"2020-09-11T09:04:45.208+0000","vip_amount":0}
     * toUser : {"id":101,"uid":"685959671499980800","imToken":"b2bc6f2492eb2933453c286ab789c436","phone":"13582009133","token":"d5815bff9fa9c03e2de13adb1c83ca72","nickName":"DQ_zq","wechatNickName":"竹淇","password":"0f5616782dc27bcd8c858c39a0656a22","sex":"2","headpic":"http://39.106.158.176/headpic/685959671499980800/6859596714999808001597045396794.jpg","wechatHeadpic":"http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epPhOIWSRTFZhmF20r2z32qN6L78zZZAq4P0WUB8hgQSdB70DqPVtRHiaTXok42TGyhZcYCn1NGLUg/132","descriptions":"","userType":"","userStatus":"1","isAdmin":"0","allowSearch":"0","msgNotify":"0","newsDetail":"0","addedFriendWhetherV":"1","allowDouquanSearch":"0","douquanNum":"a911035453_","poke":"0","groupValidate":"0","regTime":"2020-08-04T02:39:25.000+0000","lastLoginTime":"2020-08-06T03:01:02.000+0000","updateTime":"2020-08-10T07:43:16.794+0000","city":null,"wechatOpenid":"o8NDewHAh5qbF2iYoznk-AH-c3bM","province":null,"country":null,"jrmfToken":"1883e908c27bb09f8ecd8430ea645431","userLv":null,"userIp":null,"userEdition":null,"isBindWechat":"1","isVip":"1","vipStartTime":"2020-08-05T02:44:27.000+0000","vipEndTime":"2020-09-11T02:44:27.000+0000","vip_amount":0}
     */

    private long msgId;
    private String userId;
    private long createTime;
    private String sendUserId;
    private long dynamicId;
    private String imgUrl;
    private String toUserId;
    private long dynamicCommendId;
    private String dynamicCommendMsg;
    private int msgType;
    private int status;
    private int pageNum;
    private int pageSize;
    private String sendUserHeadPic;
    private int sendUserNickName;
    private int toUserNickName;
    private String msgIds;
    private UserBean sendUser;
    private UserBean toUser;

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public long getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(long dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public long getDynamicCommendId() {
        return dynamicCommendId;
    }

    public void setDynamicCommendId(long dynamicCommendId) {
        this.dynamicCommendId = dynamicCommendId;
    }

    public String getDynamicCommendMsg() {
        return dynamicCommendMsg;
    }

    public void setDynamicCommendMsg(String dynamicCommendMsg) {
        this.dynamicCommendMsg = dynamicCommendMsg;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSendUserHeadPic() {
        return sendUserHeadPic;
    }

    public void setSendUserHeadPic(String sendUserHeadPic) {
        this.sendUserHeadPic = sendUserHeadPic;
    }

    public int getSendUserNickName() {
        return sendUserNickName;
    }

    public void setSendUserNickName(int sendUserNickName) {
        this.sendUserNickName = sendUserNickName;
    }

    public int getToUserNickName() {
        return toUserNickName;
    }

    public void setToUserNickName(int toUserNickName) {
        this.toUserNickName = toUserNickName;
    }

    public String getMsgIds() {
        return msgIds;
    }

    public void setMsgIds(String msgIds) {
        this.msgIds = msgIds;
    }

    public UserBean getSendUser() {
        return sendUser;
    }

    public void setSendUser(UserBean sendUser) {
        this.sendUser = sendUser;
    }

    public UserBean getToUser() {
        return toUser;
    }

    public void setToUser(UserBean toUser) {
        this.toUser = toUser;
    }

    public static class UserBean {
        /**
         * id : 81
         * uid : 583760536945033216
         * imToken : 265eab88f30e3f629e2c9566cda70e3d
         * phone : 17600600113
         * token : 15e631715cebda021efd4167b1bb5cc3
         * nickName : 强强强
         * wechatNickName : 郝亚强
         * password :
         * sex : 1
         * headpic : http://39.106.158.176/headpic/583760536945033216/5837605369450332161590558630097.jpg
         * wechatHeadpic : http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKyNlAdcg5Q8DM3XNnMlgibzYnoQypeTmTH6ndm7CRHGKkYicico3DjGtf49RfxuDu7sMztP2dvfW6aw/132
         * descriptions :
         * userType :
         * userStatus : 1
         * isAdmin : 0
         * allowSearch : 0
         * msgNotify : 0
         * newsDetail : 0
         * addedFriendWhetherV : 1
         * allowDouquanSearch : 0
         * douquanNum : q13121325943
         * poke : 0
         * groupValidate : 1
         * regTime : 2020-03-16T02:27:59.000+0000
         * lastLoginTime : 2020-06-23T17:06:47.000+0000
         * updateTime : 2020-08-06T02:57:19.311+0000
         * city : null
         * wechatOpenid : o8NDewFFztZJJHhYt9iGedxp6JiE
         * province : null
         * country : null
         * jrmfToken : 62ecbe151da81be1802c828350399e58
         * userLv : null
         * userIp : null
         * userEdition : null
         * isBindWechat : 1
         * isVip : 1
         * vipStartTime : 2020-08-05T09:04:45.208+0000
         * vipEndTime : 2020-09-11T09:04:45.208+0000
         * vip_amount : 0
         */

        private int id;
        private String uid;
        private String imToken;
        private String phone;
        private String token;
        private String nickName;
        private String wechatNickName;
        private String password;
        private String sex;
        private String headpic;
        private String wechatHeadpic;
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
        private String jrmfToken;
        private String userLv;
        private String userIp;
        private String userEdition;
        private String isBindWechat;
        private String isVip;
        private String vipStartTime;
        private String vipEndTime;
        private int vip_amount;

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

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getWechatNickName() {
            return wechatNickName;
        }

        public void setWechatNickName(String wechatNickName) {
            this.wechatNickName = wechatNickName;
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

        public String getHeadpic() {
            return headpic;
        }

        public void setHeadpic(String headpic) {
            this.headpic = headpic;
        }

        public String getWechatHeadpic() {
            return wechatHeadpic;
        }

        public void setWechatHeadpic(String wechatHeadpic) {
            this.wechatHeadpic = wechatHeadpic;
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

        public String getJrmfToken() {
            return jrmfToken;
        }

        public void setJrmfToken(String jrmfToken) {
            this.jrmfToken = jrmfToken;
        }

        public String getUserLv() {
            return userLv;
        }

        public void setUserLv(String userLv) {
            this.userLv = userLv;
        }

        public String getUserIp() {
            return userIp;
        }

        public void setUserIp(String userIp) {
            this.userIp = userIp;
        }

        public String getUserEdition() {
            return userEdition;
        }

        public void setUserEdition(String userEdition) {
            this.userEdition = userEdition;
        }

        public String getIsBindWechat() {
            return isBindWechat;
        }

        public void setIsBindWechat(String isBindWechat) {
            this.isBindWechat = isBindWechat;
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

        public int getVip_amount() {
            return vip_amount;
        }

        public void setVip_amount(int vip_amount) {
            this.vip_amount = vip_amount;
        }
    }

}