package com.dq.im.parser;

import com.dq.im.model.HomeImBaseMode;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.model.TeamMessageBaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 共同解析类
 */
public class ImParserUtils{

    /**
     * 解析基础的IM结构
     * @param json
     */
    public static ImMessageBaseModel getBaseMessageModel(String json){
        ImMessageBaseModel imMessageBaseModel = new ImMessageBaseModel();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            String type = jsonObject.optString("type");
            String msgType = jsonObject.optString("msgType","");
            String contentData = jsonObject.optString("contentData");
            String msgIdClient = jsonObject.optString("msgIdClient");
            String msgIdServer = jsonObject.optString("msgIdServer");
            String msgSecondType = jsonObject.optString("msgSecondType");
            String fromUserId = jsonObject.optString("fromUserId");
            String toUserId = jsonObject.optString("toUserId");
            String groupId = jsonObject.optString("groupId");
            long createTime = jsonObject.optLong("createTime");
            imMessageBaseModel.setType(type);
            imMessageBaseModel.setMsgType(msgType);
            imMessageBaseModel.setSourceContent(contentData);
            imMessageBaseModel.setMsgIdClient(msgIdClient);
            imMessageBaseModel.setMsgIdServer(msgIdServer);
            imMessageBaseModel.setCreateTime(createTime);
            imMessageBaseModel.setMsgSecondType(msgSecondType);
            imMessageBaseModel.setFromUserId(fromUserId);
            imMessageBaseModel.setToUserId(toUserId);
//            imMessageBaseModel.setToUserId(groupId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imMessageBaseModel;
    }

    /**
     * 解析单条个人消息
     */
    public static P2PMessageBaseModel getP2PMessageBaseModel(String json){
        P2PMessageBaseModel messageBaseModel = new P2PMessageBaseModel();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            String type = jsonObject.optString("type");
            String msgType = jsonObject.optString("msgType","");
            String fromUserId = jsonObject.optString("fromUserId");
            String toUserId = jsonObject.optString("toUserId");
            String contentData = jsonObject.optString("contentData");
//            String contentData = jsonObject.optString("sourceContent");
            String msgIdClient = jsonObject.optString("msgIdClient");
            String msgIdServer = jsonObject.optString("msgIdServer");
            String msgSecondType = jsonObject.optString("msgSecondType");
            long createTime = jsonObject.optLong("createTime");
            messageBaseModel.setType(type);
            messageBaseModel.setMsgType(msgType);
            messageBaseModel.setFromUserId(fromUserId);
            messageBaseModel.setToUserId(toUserId);
            messageBaseModel.setSourceContent(contentData);
            messageBaseModel.setMsgIdClient(msgIdClient);
            messageBaseModel.setMsgIdServer(msgIdServer);
            messageBaseModel.setCreateTime(createTime);
            messageBaseModel.setMsgSecondType(msgSecondType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messageBaseModel;
    }
    /**
     * 批量解析个人消息
     */
    public static List<P2PMessageBaseModel> getP2PMessageBaseModelList(String json){
        List<P2PMessageBaseModel> p2PMessageBaseModels = new ArrayList<>();
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(json);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++){
                String childJson = jsonArray.getString(i);
                P2PMessageBaseModel model = getP2PMessageBaseModel(childJson);

                p2PMessageBaseModels.add(model);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return p2PMessageBaseModels;
    }

    /**
     * 解析单条群组消息
     */
    public static TeamMessageBaseModel getTeamMessageBaseModel(String json){
        TeamMessageBaseModel messageBaseModel = new TeamMessageBaseModel();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            String type = jsonObject.optString("type");
            String msgType = jsonObject.optString("msgType","");
            String fromUserId = jsonObject.optString("fromUserId");
            String toUserId = jsonObject.optString("toUserId");
            String groupId = jsonObject.optString("groupId");
            String contentData = jsonObject.optString("contentData");
//            String contentData = jsonObject.optString("sourceContent");
            String msgIdClient = jsonObject.optString("msgIdClient");
            String msgIdServer = jsonObject.optString("msgIdServer");
            String msgSecondType = jsonObject.optString("msgSecondType");
            long createTime = jsonObject.optLong("createTime");
            messageBaseModel.setType(type);
            messageBaseModel.setGroupId(groupId);
            messageBaseModel.setMsgType(msgType);
            messageBaseModel.setFromUserId(fromUserId);
            messageBaseModel.setToUserId(toUserId);
            messageBaseModel.setSourceContent(contentData);
            messageBaseModel.setMsgIdClient(msgIdClient);
            messageBaseModel.setMsgIdServer(msgIdServer);
            messageBaseModel.setCreateTime(createTime);
            messageBaseModel.setMsgSecondType(msgSecondType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messageBaseModel;
    }
    /**
     * 批量解析群组消息
     */
    public static List<TeamMessageBaseModel> getTeamMessageBaseModelList(String json){
        List<TeamMessageBaseModel> teamMessageBaseModels = new ArrayList<>();
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(json);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++){
                String childJson = jsonArray.getString(i);
                TeamMessageBaseModel model = getTeamMessageBaseModel(childJson);

                teamMessageBaseModels.add(model);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return teamMessageBaseModels;
    }

    /**
     * 解析单条首页消息
     */
    public static HomeImBaseMode getHomeImBaseMode(String json){
        HomeImBaseMode messageBaseModel = new HomeImBaseMode();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            String type = jsonObject.optString("type");
            String msgType = jsonObject.optString("msgType","");
            String fromUserId = jsonObject.optString("fromUserId");
            String toUserId = jsonObject.optString("toUserId");
            String contentData = jsonObject.optString("contentData");
//            String contentData = jsonObject.optString("sourceContent");
            String msgIdClient = jsonObject.optString("msgIdClient");
            String msgIdServer = jsonObject.optString("msgIdServer");
            long createTime = jsonObject.optLong("createTime");
            String groupId = jsonObject.optString("groupId");
            messageBaseModel.setType(type);
            messageBaseModel.setMsgType(msgType);
            messageBaseModel.setFromUserId(fromUserId);
            messageBaseModel.setToUserId(toUserId);
            messageBaseModel.setSourceContent(contentData);
            messageBaseModel.setGroupId(groupId);
            messageBaseModel.setMsgIdClient(msgIdClient);
            messageBaseModel.setMsgIdServer(msgIdServer);
            messageBaseModel.setCreateTime(createTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messageBaseModel;
    }

    /**
     * 批量解析首页消息
     */
    public static List<HomeImBaseMode> getHomeImBaseModeList(String json){
        List<HomeImBaseMode> homeImBaseModes = new ArrayList<>();
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(json);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++){
                String childJson = jsonArray.getString(i);
                HomeImBaseMode model = getHomeImBaseMode(childJson);

                homeImBaseModes.add(model);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return homeImBaseModes;
    }

}