package com.dq.im.util;

import com.dq.im.model.IMContentDataModel;
import com.dq.im.model.ImNotificationModel;
import com.dq.im.model.P2PMessageBaseModel;
import com.google.gson.Gson;

public class ImContentFactory{
    private static Gson gson = new Gson();
    public static IMContentDataModel getIMContentDataModel(P2PMessageBaseModel model, String json){
        String type =  model.getType();
        IMContentDataModel imContentDataModel = new IMContentDataModel();
        switch (type){
            case "1":
            case "2":
                imContentDataModel = getIMContentDataModel(model.getMsgType(),json);
                break;
            case "6":
                imContentDataModel = gson.fromJson(json, ImNotificationModel.class);
                break;
        }
        return imContentDataModel;
    }

    public static IMContentDataModel getIMContentDataModel(String msgType,String json){
        IMContentDataModel imContentDataModel = new IMContentDataModel();
        switch (msgType){
            case "2":
                imContentDataModel = gson.fromJson(json,ImNotificationModel.class);
                break;
        }
        return imContentDataModel;
    }

}