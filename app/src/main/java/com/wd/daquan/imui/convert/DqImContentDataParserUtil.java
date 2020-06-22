package com.wd.daquan.imui.convert;

import android.util.Log;

import com.dq.im.bean.im.MessageCardBean;
import com.dq.im.bean.im.MessagePhotoBean;
import com.dq.im.bean.im.MessageRedPackageBean;
import com.dq.im.bean.im.MessageTextBean;
import com.dq.im.bean.im.MessageVideoBean;
import com.dq.im.bean.im.MessageVoiceBean;
import com.dq.im.model.IMContentDataModel;
import com.dq.im.type.MessageType;
import com.wd.daquan.imui.bean.im.DqMessagePhotoBean;
import com.wd.daquan.imui.bean.im.DqMessageTextBean;
import com.wd.daquan.model.utils.GsonUtils;

/**
 * 通用数据模型解析
 */
public class DqImContentDataParserUtil {

    public static IMContentDataModel parserImContentDataModel(String type,String json){
        Log.e("YM","消息类型"+type);
        Log.e("YM","消息内容"+json);
        IMContentDataModel imContentDataModel = new IMContentDataModel();
        MessageType messageType = MessageType.typeOfValue(type);
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
        }
        return imContentDataModel;
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
}