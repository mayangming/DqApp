package com.wd.daquan.mine.mode;

import android.text.TextUtils;

import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;

/**
 * @Author: 方志
 * @Time: 2018/8/23 15:47
 * @Description: 消息提醒，声音，振动辅助类
                新消息通知 0为开 1为关
                新消息声音提示 btnoise为开 nbt为关
                新消息震动 vibtrue为开 vibfalse为关
 */
public class MessageNotifyHelper {

    private static MessageNotifyHelper instance = null;
    private final EBSharedPrefUser prefUser;
    private final String userId;

    private MessageNotifyHelper(){
        QCSharedPrefManager manager = QCSharedPrefManager.getInstance();
        prefUser = manager.getKDPreferenceUserInfo();
        userId = prefUser.getString(EBSharedPrefUser.uid, "");
    }

    public static MessageNotifyHelper getInstance() {
        synchronized (MessageNotifyHelper.class) {
            if (instance == null) {
                instance = new MessageNotifyHelper();
            }
        }
        return instance;
    }

    /**
     * 新消息通知 赋值
     */
    public void setNewMessageNotify(String isNotify){
        if(TextUtils.isEmpty(getNewMessageNotify())) {
            //第一次登录，初始化开启通知和声音
            prefUser.saveString(EBSharedPrefUser.msg_notify + userId, KeyValue.ZERO_STRING);//通知
            prefUser.saveString(EBSharedPrefUser.noise + userId, KeyValue.NEW_MESSAGE_VOICE_ON);//声音
        }else {
            prefUser.saveString(EBSharedPrefUser.msg_notify + userId, isNotify);
        }
    }

    /**
     * 新消息通知 取值
     */
    public String getNewMessageNotify(){
        String notice = prefUser.getString(EBSharedPrefUser.msg_notify + userId, "");
        if(TextUtils.isEmpty(notice)) {
            prefUser.saveString(EBSharedPrefUser.msg_notify + userId, KeyValue.ZERO_STRING);//通知
            prefUser.saveString(EBSharedPrefUser.noise + userId, KeyValue.NEW_MESSAGE_VOICE_ON);//声音
        }
        return prefUser.getString(EBSharedPrefUser.msg_notify + userId, "");
    }

    /**
     * 新消息声音 赋值
     */
    public void setNewMessageVoice(String isVoice){
        prefUser.saveString(EBSharedPrefUser.noise + userId, isVoice);

    }

    /**
     * 新消息声音 取值
     */
    public String getNewMessageVoice(){
        return prefUser.getString(EBSharedPrefUser.noise + userId, KeyValue.NEW_MESSAGE_VOICE_ON);
    }

    /**
     * 新消息震动 赋值
     */
    public void setNewMessageShake(String isShake){
        QCSharedPrefManager manager = QCSharedPrefManager.getInstance();
        EBSharedPrefUser prefUser = manager.getKDPreferenceUserInfo();
        prefUser.saveString(EBSharedPrefUser.vibration + userId, isShake);
    }

    /**
     * 新消息震动 取值
     */
    public String getNewMessageShake(){
        return prefUser.getString(EBSharedPrefUser.vibration + userId, KeyValue.NEW_MESSAGE_SHAKE_ON);
    }


}
