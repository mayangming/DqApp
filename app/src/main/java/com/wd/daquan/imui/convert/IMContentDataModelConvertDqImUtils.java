package com.wd.daquan.imui.convert;

import android.util.Log;

import com.dq.im.bean.im.MessagePhotoBean;
import com.dq.im.bean.im.MessageTextBean;
import com.dq.im.model.IMContentDataModel;
import com.dq.im.type.MessageType;
import com.wd.daquan.imui.bean.im.DqMessageBaseContent;
import com.wd.daquan.imui.bean.im.DqMessagePhotoBean;
import com.wd.daquan.imui.bean.im.DqMessageTextBean;
import com.wd.daquan.model.utils.GsonUtils;
import com.wd.daquan.util.AESUtil;

/**
 * 斗圈消息类型与通用消息类型的转换工具类
 */
public class IMContentDataModelConvertDqImUtils {

    /**
     * 将斗圈文本消息改为通用消息
     * @param dqMessageTextBean
     * @return
     */
    public static IMContentDataModel convertCommonTextContent(DqMessageTextBean dqMessageTextBean){
        MessageTextBean messageTextBean = new MessageTextBean();
        messageTextBean.setDescription(dqMessageTextBean.getSearchableContent());
        return messageTextBean;
    }

    /**
     * 将通用文本消息转为斗圈消息
     * @param messageTextBean
     * @return
     */
    public static DqMessageBaseContent convertDqTextContent(MessageTextBean messageTextBean){
        DqMessageTextBean dqMessageTextBean = new DqMessageTextBean();
        dqMessageTextBean.setSearchableContent(messageTextBean.getDescription());
        return dqMessageTextBean;
    }

    /**
     * 将斗圈文本消息改为通用消息
     * @param dqMessagePhotoBean
     * @return
     */
    public static IMContentDataModel convertCommonPhotoContent(DqMessagePhotoBean dqMessagePhotoBean){
        MessagePhotoBean messagePhotoBean = new MessagePhotoBean();
        messagePhotoBean.setDescription(dqMessagePhotoBean.getRemoteMediaUrl());
        messagePhotoBean.setLocalUriString(dqMessagePhotoBean.getLocalUriString());
        messagePhotoBean.setPhotoUri(dqMessagePhotoBean.getPhotoUri());
        messagePhotoBean.setSearchableContent(dqMessagePhotoBean.getSearchableContent());
        return messagePhotoBean;
    }

    /**
     * 将通用消息转为斗圈消息
     * @param messagePhotoBean
     * @return
     */
    public static DqMessageBaseContent convertDqPhotoContent(MessagePhotoBean messagePhotoBean){
        DqMessagePhotoBean dqMessagePhotoBean = new DqMessagePhotoBean();
        dqMessagePhotoBean.setRemoteMediaUrl(messagePhotoBean.getDescription());
//        dqMessagePhotoBean.setSearchableContent(messagePhotoBean.getSearchableContent());
        dqMessagePhotoBean.setSearchableContent(AESUtil.decode("[图片]"));
        dqMessagePhotoBean.setLocalUriString(messagePhotoBean.getLocalUriString());
        dqMessagePhotoBean.setPhotoUri(messagePhotoBean.getPhotoUri());
        Log.e("YM","斗圈的图片内容:"+dqMessagePhotoBean.toString());
        return dqMessagePhotoBean;
    }

    /**
     * 将斗圈消息模型转换为通用聊天消息模型的json结构
     * @param msgType
     * @param json
     * @return
     */
    public static String convertCommonContentStr(String msgType, String json){
        MessageType messageType = MessageType.typeOfValue(msgType);
        String content = json;
        IMContentDataModel imContentDataModel;
        switch (messageType){
            case TEXT:
                DqMessageTextBean dqMessageTextBean = GsonUtils.fromJson(json,DqMessageTextBean.class);
                imContentDataModel = convertCommonTextContent(dqMessageTextBean);
                content = GsonUtils.toJson(imContentDataModel);
                break;
            case PICTURE:
                DqMessagePhotoBean dqMessagePhotoBean = GsonUtils.fromJson(json,DqMessagePhotoBean.class);
                imContentDataModel = convertCommonPhotoContent(dqMessagePhotoBean);
                content = GsonUtils.toJson(imContentDataModel);
                break;
        }
        return content;
    }
    /**
     * 将聊天消息模型转换为斗圈消息模型的json结构
     * @param msgType
     * @param json
     * @return
     */
    public static String convertDqCommonContentStr(String msgType, String json){
        MessageType messageType = MessageType.typeOfValue(msgType);
        String content = json;
        DqMessageBaseContent dqMessageBaseContent;
        switch (messageType){
            case TEXT:
                MessageTextBean imContentDataModel = GsonUtils.fromJson(json,MessageTextBean.class);
                dqMessageBaseContent = convertDqTextContent(imContentDataModel);
                content = GsonUtils.toJson(dqMessageBaseContent);
                break;
            case PICTURE:
                MessagePhotoBean messagePhotoBean = GsonUtils.fromJson(json,MessagePhotoBean.class);
                dqMessageBaseContent = convertDqPhotoContent(messagePhotoBean);
                content = GsonUtils.toJson(dqMessageBaseContent);
                break;
        }
        Log.e("YM","convertDqCommonContentStr----->"+content);
        return content;
    }

    /**
     * 将斗圈消息模型转换为聊天消息模型的json结构
     * @param msgType
     * @param dqMessageBaseContent
     * @return
     */
    public static IMContentDataModel convertCommonContent(String msgType, IMContentDataModel dqMessageBaseContent){
        MessageType messageType = MessageType.typeOfValue(msgType);
        IMContentDataModel imContentDataModel = dqMessageBaseContent;
        switch (messageType){
            case TEXT:
                DqMessageTextBean dqMessageTextBean = (DqMessageTextBean) dqMessageBaseContent;
                imContentDataModel = convertCommonTextContent(dqMessageTextBean);
                break;
            case PICTURE:
                DqMessagePhotoBean dqMessagePhotoBean = (DqMessagePhotoBean)dqMessageBaseContent;
                imContentDataModel = convertCommonPhotoContent(dqMessagePhotoBean);
                break;
        }
        return imContentDataModel;
    }

    /**
     * 将普通消息模型转换为斗圈聊天消息模型的json结构
     * @param msgType
     * @param imContentDataModel
     * @return
     */
    public static IMContentDataModel convertDqCommonContent(String msgType, IMContentDataModel imContentDataModel){
        MessageType messageType = MessageType.typeOfValue(msgType);
        IMContentDataModel model = imContentDataModel;
        switch (messageType){
            case TEXT:
                MessageTextBean messageTextBean = (MessageTextBean) imContentDataModel;
                model = convertDqTextContent(messageTextBean);
                break;
            case PICTURE:
                MessagePhotoBean messagePhotoBean = (MessagePhotoBean)imContentDataModel;
                model = convertDqPhotoContent(messagePhotoBean);
                break;
        }
        return model;
    }

}