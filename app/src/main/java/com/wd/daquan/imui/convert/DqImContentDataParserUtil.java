package com.wd.daquan.imui.convert;

import com.dq.im.bean.im.MessageCardBean;
import com.dq.im.bean.im.MessageRedPackageBean;
import com.dq.im.bean.im.MessageVideoBean;
import com.dq.im.bean.im.MessageVoiceBean;
import com.dq.im.model.IMContentDataModel;
import com.dq.im.type.MessageType;
import com.wd.daquan.imui.bean.ChatOfSystemMessageBean;
import com.wd.daquan.imui.bean.MessageSystemBean;
import com.wd.daquan.imui.bean.im.DqMessagePhotoBean;
import com.wd.daquan.imui.bean.im.DqMessageTextBean;
import com.wd.daquan.imui.type.MsgSecondType;
import com.wd.daquan.model.utils.GsonUtils;

/**
 * 通用数据模型解析
 */
public class DqImContentDataParserUtil {
    public static IMContentDataModel parserImContentDataModel(String msgType,String msgSecondType,String json){
        IMContentDataModel imContentDataModel = new IMContentDataModel();
        MessageType messageType = MessageType.typeOfValue(msgType);
        switch (messageType){
            case TEXT:
                imContentDataModel = parserText(json);
                break;
            case PICTURE:
                imContentDataModel = parserPhoto(json);
                break;
            case VOICE:
                imContentDataModel = parserVoice(json);
                break;
            case VIDEO:
                imContentDataModel = parserVideo(json);
                break;
            case PERSON_CARD:
                imContentDataModel = parserPersonCard(json);
                break;
            case RED_PACKAGE:
                imContentDataModel = parserReadPackage(json);
                break;
            case GROUP_KICK_OUT://被踢出了群组
                imContentDataModel = parserGroupKickOut(json);
                break;
            case SYSTEM://系统消息
                imContentDataModel = parserSystem(msgSecondType,json);
                break;
        }
        return imContentDataModel;
    }
    public static IMContentDataModel parserImContentDataModel(String type,String json){
        return parserImContentDataModel(type,"",json);
    }

    private static IMContentDataModel parserText(String json){
        return GsonUtils.fromJson(json, DqMessageTextBean.class);
    }
    private static IMContentDataModel parserPhoto(String json){
        return GsonUtils.fromJson(json, DqMessagePhotoBean.class);
    }
    private static IMContentDataModel parserVoice(String json){
        return GsonUtils.fromJson(json, MessageVoiceBean.class);
    }
    private static IMContentDataModel parserVideo(String json){
        return GsonUtils.fromJson(json, MessageVideoBean.class);
    }
    private static IMContentDataModel parserPersonCard(String json){
        return GsonUtils.fromJson(json, MessageCardBean.class);
    }
    private static IMContentDataModel parserReadPackage(String json){
        return GsonUtils.fromJson(json, MessageRedPackageBean.class);
    }

    /**
     * 被踢出了群组
     * @param json
     * @return
     */
    private static IMContentDataModel parserGroupKickOut(String json){
        return GsonUtils.fromJson(json, MessageSystemBean.class);
    }
    private static IMContentDataModel parserSystem(String secondType,String json){
        IMContentDataModel imContentDataModel = new IMContentDataModel();
        MsgSecondType msgSecondType = MsgSecondType.getMsgSecondTypeByValue(secondType);
        switch (msgSecondType){
            case MSG_SECOND_TYPE_RED_COMPLETE:
                imContentDataModel = GsonUtils.fromJson(json, ChatOfSystemMessageBean.class);
                break;
        }
        return imContentDataModel;
    }



}