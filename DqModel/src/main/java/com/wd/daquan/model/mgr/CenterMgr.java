package com.wd.daquan.model.mgr;

import android.text.TextUtils;
import android.util.Log;

import com.wd.daquan.model.bean.UnreadNotifyEntity;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.rxbus.ModuleBase;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.model.utils.GsonUtils;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * 个人中心
 * Created by Kind on 2018/10/11.
 */

public class CenterMgr implements ModuleBase, QCObserver{

    private QCSharedPrefManager qcSharedPrefManager;

    public static final String CITY="city";
    public static final String PROVINCE ="province";
    //已经安装过
    public static final String EXIT_INSTALL ="exit_install";

    //真实姓名
    public static final String REAL_NAME = "real_name";
    //身份证号
    public static final String ID_NUMBER ="id_number";
    //身份证号
    public static final String WALLET_BIND_PHONE ="wallet_bind_phone";

    public static final String JRMF_TOKEN ="jrmf_token";


    @Override
    public void init() {
        qcSharedPrefManager = QCSharedPrefManager.getInstance();
        MsgMgr.getInstance().attach(this);
    }

    @Override
    public void release() {
        MsgMgr.getInstance().detach(this);
    }

    public EBSharedPrefUser getKDPreferenceUserInfo() {
        return qcSharedPrefManager.getKDPreferenceUserInfo();
    }

    private String uid = "";
    private String dq_num = "";
    private String im_token = "";
    private String token = "";

    private String lastDynamicUid = "";//上次动态的朋友圈用户Id
    private long lastDynamicTime = 0;//上次朋友圈动态的时间

    /**
     * 获取UID
     * @return
     */
    public String getUID() {
        if (TextUtils.isEmpty(uid)) {
            uid = getKDPreferenceUserInfo().getString(EBSharedPrefUser.uid, "");
        }
        return uid;
    }
    /**
     * 获取上次动态的朋友圈Id
     * @return
     */
    public String getLastDynamicUid() {
        if (TextUtils.isEmpty(lastDynamicUid)) {
            lastDynamicUid = getKDPreferenceUserInfo().getString(EBSharedPrefUser.lastDynamicUid, "");
        }
        return lastDynamicUid;
    }

    /**
     * 获取上次动态的朋友圈动态阅读状态
     * @return
     */
    public int getLastDynamicReadStatus() {
        return getKDPreferenceUserInfo().getInt(EBSharedPrefUser.lastDynamicReadStatus, 0);
    }

    /**
     * 获取上次朋友圈动态的时间
     * @return
     */
    public long getLastDynamicTime() {
        lastDynamicTime = getKDPreferenceUserInfo().getLong(EBSharedPrefUser.lastDynamicTime, 0);
        return lastDynamicTime;
    }

    /**
     * 获取云信运行token
     * @return
     */
    public String getImToken() {
        if (TextUtils.isEmpty(im_token)) {
            im_token = getKDPreferenceUserInfo().getString(EBSharedPrefUser.im_token, "");
        }
        return im_token;
    }

    /**
     * 获取金融魔方token
     * @return
     */
    public String getJrmfToken() {
        return getKDPreferenceUserInfo().getString(EBSharedPrefUser.JRMF_TOKEN, "");
    }

    /**
     * 获取token
     */
    public String getToken() {
        if (TextUtils.isEmpty(token)) {
            token = getKDPreferenceUserInfo().getString(EBSharedPrefUser.token, "");
        }
        return token;
    }
    /**
     * 设置引导页权限同意的状态
     */
    public void putAgreeProtocolStatus(boolean isAgree) {
        getKDPreferenceUserInfo().saveBoolean(EBSharedPrefUser.IS_AGREE_PROTOCOL, isAgree);
    }
    /**
     * 获取引导页权限同意的状态
     */
    public boolean getAgreeProtocolStatus() {
        return getKDPreferenceUserInfo().getBoolean(EBSharedPrefUser.IS_AGREE_PROTOCOL, false);
    }
    /**
     * 获取红包雨内容
     */
    public void putRedRainContent(String redRainContent) {
        getKDPreferenceUserInfo().saveString(EBSharedPrefUser.RED_RAIN_CONTENT, redRainContent);
    }
    /**
     * 获取红包雨内容
     */
    public String getRedRainContent() {
        return getKDPreferenceUserInfo().getString(EBSharedPrefUser.RED_RAIN_CONTENT,"");
    }

    public void clear() {
        uid = null;
        token = null;
        im_token = null;
        dq_num = null;
        lastDynamicUid = null;
    }


    /**
     * 斗圈号
     * @return
     */
    public String getDqNum(){
        if (TextUtils.isEmpty(dq_num)) {
            dq_num = getKDPreferenceUserInfo().getString(EBSharedPrefUser.QINGTALK_NUM, "");
        }
        return dq_num;
    }

    public void putDqNum(String qingChatNum){
        getKDPreferenceUserInfo().saveString(EBSharedPrefUser.QINGTALK_NUM, qingChatNum);
    }

    /**
     * 昵称
     * @return
     */
    public String getNickName(){
        return getKDPreferenceUserInfo().getString(EBSharedPrefUser.nickname, "");
    }

    public void putNickName(String nickname){
        getKDPreferenceUserInfo().saveString(EBSharedPrefUser.nickname, nickname);
    }

    /**
     * 头像
     * @return
     */
    public String getAvatar(){
        return getKDPreferenceUserInfo().getString(EBSharedPrefUser.headpic, "");
    }
    public void saveAvatar(String path){
        getKDPreferenceUserInfo().saveString(EBSharedPrefUser.headpic, path);
    }

    public void putAvatar(String avatar){
        getKDPreferenceUserInfo().saveString(EBSharedPrefUser.headpic, avatar);
    }

    public boolean enablePoke() {
        String target = getKDPreferenceUserInfo().getString(EBSharedPrefUser.POKE_MESSAGE, "0");
        return "0".equals(target);
    }

    /**
     * 保存戳一下状态
     * @param value
     */
    public void savePoke(String value) {
        getKDPreferenceUserInfo().saveString(EBSharedPrefUser.POKE_MESSAGE, value);
    }

    public void saveTeamInvite(String value) {
        getKDPreferenceUserInfo().saveString(getUID() + EBSharedPrefUser.TEAM_INVITE, value);
    }

    /**
     * 上次动态的朋友圈用户Id
     * @param value
     */
    public void saveLastDynamicUid(String value) {
        getKDPreferenceUserInfo().saveString(EBSharedPrefUser.lastDynamicUid, value);
    }

    /**
     * 保存上一次朋友圈动态的时间
     * @param value
     */
    public void saveLastDynamicTime(Long value) {
        getKDPreferenceUserInfo().saveLong(EBSharedPrefUser.lastDynamicTime, value);
    }

    /**
     * 上次朋友圈的阅读状态
     * @param value
     */
    public void saveLastDynamicReadStatus(int value) {
        getKDPreferenceUserInfo().saveInt(EBSharedPrefUser.lastDynamicReadStatus, value);
    }

    public boolean enableTeamInvite() {
        String target = getKDPreferenceUserInfo().getString(getUID() + EBSharedPrefUser.TEAM_INVITE, "");
        return "0".equals(target);
    }

    public void saveNotifyList(LinkedHashSet<UnreadNotifyEntity> list) {
        String target = GsonUtils.toJson(list);
        getKDPreferenceUserInfo().saveString(EBSharedPrefUser.NOTIFY_LIST, target);
    }

    public List<UnreadNotifyEntity> getNotifyList() {
        String target = getKDPreferenceUserInfo().getString(EBSharedPrefUser.NOTIFY_LIST, null);
        if (TextUtils.isEmpty(target)) {
            return null;
        }
        List<UnreadNotifyEntity> list = GsonUtils.fromJsonList(target, UnreadNotifyEntity.class);
        return list;
    }

    @Override
    public void onMessage(String key, Object value) {
        if (key.equals(MsgType.MT_App_Login)) {
            if ((Boolean) value) {//登录成功
            } else {
                logout();
            }
        }
    }

    private void logout() {
        uid = "";
        dq_num = "";
    }

    /**
     * 获取手机号码
     */
    public String getPhoneNumber() {
        return getKDPreferenceUserInfo().getString(EBSharedPrefUser.phone, "");
    }

    /**
     * 存储手机号码
     */
    public void putPhoneNumber(String phoneNumber) {
        getKDPreferenceUserInfo().saveString(EBSharedPrefUser.phone, phoneNumber);
    }

    /**
     * 判断用户是否是Vip
     */
    public boolean isVip() {
        String vipStatus = getKDPreferenceUserInfo().getString(EBSharedPrefUser.isVip, "");
        DqLog.e("YM------->vip状态:"+vipStatus);
        return (!TextUtils.isEmpty(vipStatus) && "1".equals(vipStatus));

    }

    /**
     * 存储VIP状态
     */
    public void putVipStatus(String vipStatus) {
        getKDPreferenceUserInfo().saveString(EBSharedPrefUser.isVip, vipStatus);
    }

    /**
     * 获取手机号码
     */
    public String getVipStartTime() {
        return getKDPreferenceUserInfo().getString(EBSharedPrefUser.vipStartTime, "");
    }

    /**
     * 存储手机号码
     */
    public void putVipStartTime(String vipStartTime) {
        getKDPreferenceUserInfo().saveString(EBSharedPrefUser.vipStartTime, vipStartTime);
    }

    /**
     * 获取手机号码
     */
    public String getVipEndTime() {
        return getKDPreferenceUserInfo().getString(EBSharedPrefUser.vipEndTime, "");
    }

    /**
     * 存储手机号码
     */
    public void putVipEndTime(String vipEndTime) {
        getKDPreferenceUserInfo().saveString(EBSharedPrefUser.vipEndTime, vipEndTime);
    }

    /**
     *获取分享总人数
     */
    public int getShareTotalNum() {
        return getKDPreferenceUserInfo().getInt(EBSharedPrefUser.shareTotalNum, 0);
    }

    /**
     * 设置分享总人数
     */
    public void putShareTotalNum(int shareTotalNum) {
        getKDPreferenceUserInfo().saveInt(EBSharedPrefUser.shareTotalNum, shareTotalNum);
    }
    /**
     *获取用户微信头像
     */
    public String getWeChatIconUrl() {
        return getKDPreferenceUserInfo().getString(EBSharedPrefUser.weChatIconUrl,"");
    }

    /**
     * 设置用户微信头像
     */
    public void putWeChatIconUrl(String weChatIconUrl) {
        getKDPreferenceUserInfo().saveString(EBSharedPrefUser.weChatIconUrl, weChatIconUrl);
    }
    /**
     *获取用户微信昵称
     */
    public String getWeChatName() {
        return getKDPreferenceUserInfo().getString(EBSharedPrefUser.weChatName,"");
    }

    /**
     * 获取用户微信昵称
     */
    public void putWeChatName(String weChatName) {
        getKDPreferenceUserInfo().saveString(EBSharedPrefUser.weChatName, weChatName);
    }
    /**
     *获取红包雨规则
     */
    public String getRedEnvelopedRainRemind() {
        return getKDPreferenceUserInfo().getString(EBSharedPrefUser.redEnvelopedRainRemind, "");
    }

    /**
     * 设置红包雨规则
     */
    public void putRedEnvelopedRainRemind(String shareTotalNum) {
        getKDPreferenceUserInfo().saveString(EBSharedPrefUser.redEnvelopedRainRemind, shareTotalNum);
    }

    /**
     *  获取存储登录密码
     */
    public String getPwd() {
        return getKDPreferenceUserInfo().getString(EBSharedPrefUser.password, "");
    }
    public void putPwd(String pwd) {
        getKDPreferenceUserInfo().saveString(EBSharedPrefUser.password, pwd);
    }

    public void putString(String key, String value) {
        getKDPreferenceUserInfo().saveString(key, value);
    }
    public String getString(String key) {
        return getKDPreferenceUserInfo().getString(key, "");
    }

    /**
     * 获取存储城市
     */
    public String getCity() {
        return getKDPreferenceUserInfo().getString(CITY, "");
    }
    public void putCity(String city) {
        getKDPreferenceUserInfo().saveString(CITY, city);
    }


    /**
     *  获取存储加密真实姓名
     */
    public String getRealName() {
        return getKDPreferenceUserInfo().getString(getUID() + REAL_NAME, "");
    }
    public void putRealName(String name) {
        getKDPreferenceUserInfo().saveString(getUID() + REAL_NAME, name);
    }

    /**
     *  获取存储加密证件号码
     */
    public String getIDNumber() {
        return getKDPreferenceUserInfo().getString(getUID() + ID_NUMBER, "");
    }
    public void putIDNumber(String name) {
        getKDPreferenceUserInfo().saveString(getUID() + ID_NUMBER, name);
    }

    /**
     *  获取存储钱包绑定的手机号码
     */
    public String getWalletPhone() {
        return getKDPreferenceUserInfo().getString(getUID() + WALLET_BIND_PHONE, "");
    }
    public void putWalletPhone(String name) {
        getKDPreferenceUserInfo().saveString(getUID() + WALLET_BIND_PHONE, name);
    }

    public boolean isInstall() {
        return getKDPreferenceUserInfo().getBoolean(EXIT_INSTALL, false);
    }
    public void setInstall(boolean boo) {
        getKDPreferenceUserInfo().saveBoolean(EXIT_INSTALL, boo);
    }

    /**
     * 拉去好友列表的时间
     */
    private static final String GET_FRIEND_LIST_TIME = "get_friend_list_time";

    public long getFriendListTime() {
        //        if(aLong == 0) {
//            aLong = System.currentTimeMillis();
//        }
        return getKDPreferenceUserInfo().getLong(GET_FRIEND_LIST_TIME, 0);
    }
    public void setFriendListTime(long time) {
        getKDPreferenceUserInfo().saveLong(GET_FRIEND_LIST_TIME, time);
    }

    public void saveSex(String gender) {
        getKDPreferenceUserInfo().saveString(EBSharedPrefUser.sex, gender);
    }
    public String getSex() {
        return getKDPreferenceUserInfo().getString(EBSharedPrefUser.sex, "");
    }


    /**
     * 金融魔方用户是否是否存在
     */
    private static final String JRMF_USER_IS_EXIT = "jrmf_user_is_exit";
    public void saveJrmfState(boolean b) {
        getKDPreferenceUserInfo().saveBoolean(JRMF_USER_IS_EXIT, b);
    }
    public boolean getJrmfState() {
        return  getKDPreferenceUserInfo().getBoolean(JRMF_USER_IS_EXIT, false);
    }


    public void saveIsShowMakeMoneyTip(boolean isShowTip){
        getKDPreferenceUserInfo().saveBoolean(EBSharedPrefUser.isShowMakeMoneyTip, isShowTip);
    }

    public boolean isShowMakeMoneyTip(){
        return  getKDPreferenceUserInfo().getBoolean(EBSharedPrefUser.isShowMakeMoneyTip, true);
    }

    public void saveHttpProxy(String httpProxy){
        getKDPreferenceUserInfo().saveString(EBSharedPrefUser.httpProxy, httpProxy);
    }

    public String getHttpProxy(){
        return getKDPreferenceUserInfo().getString(EBSharedPrefUser.httpProxy,"");
    }

}
