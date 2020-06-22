package com.dq.im.type;

import android.support.annotation.NonNull;

import com.dq.im.bean.im.MessageCardBean;
import com.dq.im.bean.im.MessagePhotoBean;
import com.dq.im.bean.im.MessageRedPackageBean;
import com.dq.im.bean.im.MessageTextBean;
import com.dq.im.bean.im.MessageVideoBean;
import com.dq.im.bean.im.MessageVoiceBean;
import com.dq.im.model.IMContentDataModel;

/**
 * 消息类型
 * 没有定义的类型统统用null来表示，用的时候注意处理下
 */
public enum MessageType {
    RECEIVER_MESSAGE_CALLBACK_STATUS(-2, null),//消息回传状态
    UNKNOWN(0,null),
    SYSTEM(10,null),
    TEXT(1, MessageTextBean.class),
//    TEXT_LINK("03",null),
    VOICE(2, MessageVoiceBean.class),
    PICTURE(3, MessagePhotoBean.class),
    LOCATION(4,null),
    FILE(5,null),
    VIDEO(6, MessageVideoBean.class),
    PERSON_CARD(11, MessageCardBean.class),
    RED_PACKAGE(12, MessageRedPackageBean.class),
//    VOICE_PHONE("11",null),
//    VIDEO_PHONE("12",null),
    EMOJI(13,null),//表情
    ANONYMOUS(14,null),//匿名消息
    GROUP_APPLY_FOR(17,null),//加群申请消息
    SIGN_LOGIN(21, null),//单点登陆登出
    FRIEND_RECEIVED(22, null),//好友申请已经通过了
    FRIEND_ADD(101,null),//添加好友消息
    FRIEND_DELETE(102,null),//好友删除消息
    GROUP_CREATE(104,null),//创建群组
    GROUP_INVITE(105,null),//群组邀请消息
    GROUP_KICK_OUT(106, null),//你被踢出了群组
    GROUP_EXIT(107, null),//退群消息
    GROUP_REMOVE(108, null),//群解散消息
    UPDATE_GROUP_NAME(110, null),//修改群名称
    RED_PACKAGE_SEND(111, null),//发送红包消息
    EXPAND_MSG(999, null);//拓展消息
//    FRIEND_ADD_AGREE("22", null);//对方已同意好友请求
//    ANONYMOUS("99",null);//匿名

    private int value;
    private Class<? extends IMContentDataModel> classType;//具体消息类型

    private MessageType(int var3,Class<? extends IMContentDataModel> classType) {
        this.classType = classType;
        this.value = var3;
    }

    public final String getValue() {
        return String.valueOf(this.value);
    }

    public final Class<? extends IMContentDataModel> getClassType() {
        return this.classType;
    }

    public static MessageType typeOfValue(String var0) {
        MessageType[] var1;
        int var2 = (var1 = values()).length;
        for(int var3 = 0; var3 < var2; ++var3) {
            MessageType var4;
            if ((var4 = var1[var3]).getValue().equals(var0)) {
                return var4;
            }
        }
        return UNKNOWN;
    }

    /**
     * 根据type类型获取具体的Class类型
     */
    public static Class<? extends IMContentDataModel> getClassByType(String typeValue){
        MessageType[] messageTypes = values();
        Class classType = null;
        for (MessageType messageType : messageTypes){
            if (messageType.getValue().equals(typeValue)){
                classType = messageType.classType;
                break;
            }
        }
        return classType;
    }

    /**
     * 根据type类型获取具体的Class类型
     */
    public static String getTypeByType(@NonNull Class<? extends IMContentDataModel> classType){
        if (null == classType){
            throw new NullPointerException("Class类型不能传递null");
        }
        MessageType[] messageTypes = values();
        int typeValue = 0;
        for (MessageType messageType : messageTypes){
            if (classType == messageType.classType){
                typeValue = messageType.value;
                break;
            }
        }
        return String.valueOf(typeValue);
    }

}