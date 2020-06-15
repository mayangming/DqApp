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
    UNKNOWN("-1",null),
    SYSTEM("01",null),
    TEXT("02", MessageTextBean.class),
    TEXT_LINK("03",null),
    VOICE("04", MessageVoiceBean.class),
    PICTURE("05", MessagePhotoBean.class),
    FILE("06",null),
    VIDEO("07", MessageVideoBean.class),
    RED_PACKAGE("08", MessageRedPackageBean.class),
    LOCATION("09",null),
    PERSON_CARD("10", MessageCardBean.class),
    VOICE_PHONE("11",null),
    VIDEO_PHONE("12",null),
    EMOJI("13",null),//表情
    ANONYMOUS("14",null),//匿名消息
    FRIEND_ADD("15",null),//添加好友消息
    GROUP_INVITE("16",null),//群组邀请消息
    GROUP_APPLY_FOR("17",null),//加群申请消息
    FRIEND_DELETE("18",null),//好友删除消息
    GROUP_EXIT("19", null),//退群消息
    GROUP_KICK_OUT("20", null),//你被踢出了群组
    SIGN_LOGIN("21", null),//单点登陆登出
    FRIEND_RECEIVED("22", null),//好友申请已经通过了
    GROUP_REMOVE("25", null),//群解散消息
    RED_PACKAGE_SEND("28", null),//发送红包消息
    UPDATE_GROUP_NAME("29", null),//修改群名称
    EXPAND_MSG("999", null);//拓展消息
//    FRIEND_ADD_AGREE("22", null);//对方已同意好友请求
//    ANONYMOUS("99",null);//匿名

    private String value;
    private Class<? extends IMContentDataModel> classType;//具体消息类型

    private MessageType(String var3,Class<? extends IMContentDataModel> classType) {
        this.classType = classType;
        this.value = var3;
    }

    public final String getValue() {
        return this.value;
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
            if (messageType.value.equals(typeValue)){
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
        String typeValue = null;
        for (MessageType messageType : messageTypes){
            if (classType == messageType.classType){
                typeValue = messageType.value;
                break;
            }
        }
        return typeValue;
    }

}