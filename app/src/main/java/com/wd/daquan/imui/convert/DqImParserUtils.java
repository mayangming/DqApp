package com.wd.daquan.imui.convert;

import android.text.TextUtils;
import android.util.Log;

import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.model.TeamMessageBaseModel;
import com.wd.daquan.imui.bean.im.DqImBaseBean;
import com.wd.daquan.imui.bean.im.FirstContentBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 斗圈基础解析类
 */
public class DqImParserUtils {

    /**
     * 解析斗圈通用消息格式
     * @param json
     * @return
     */
    public static DqImBaseBean getDqBaseMessageModel(String json){
        DqImBaseBean dqImBaseBean = new DqImBaseBean();
        JSONObject jsonObject;
        String msgIdServer = "-1";
        try {
            jsonObject = new JSONObject(json);
            msgIdServer = jsonObject.optString("msgIdServer","");
            JSONObject firstContent = jsonObject.getJSONObject("content");
            FirstContentBean firstContentBean = new FirstContentBean();
            firstContentBean.setMsgIdClient(firstContent.optString("msgIdClient",""));
            firstContentBean.setMsgType(firstContent.optInt("msgType",0));
            firstContentBean.setConversationType(firstContent.getString("conversationType"));
//            firstContentBean.setMsgIdServer(firstContent.optString("msgIdServer",""));
            firstContentBean.setSourceContent(firstContent.getString("content"));
            firstContentBean.setFrom(firstContent.optString("from",""));
            firstContentBean.setTarget(firstContent.optString("target",""));
            firstContentBean.setTimestamp(firstContent.optLong("timestamp",0));
            firstContentBean.setGroupId(firstContent.optString("groupId",""));
            firstContentBean.setStatus(Integer.parseInt(firstContent.optString("status","0")));
            firstContentBean.setMsgSecondType(firstContent.optString("msgSecondType",""));
            String msgContent = firstContent.optString("content","");
            firstContentBean.setSourceContent(msgContent);
            firstContentBean.setContent(DqImContentDataParserUtil.parserImContentDataModel(firstContentBean.getMsgType()+"",firstContentBean.getMsgSecondType(),firstContent.getString("content")));
            Log.e("YM","获取firstContent数据:"+firstContentBean.toString());
//            if (!TextUtils.isEmpty(msgIdServer)){//假如外层的msgServer不为null的时候，进行赋值
            firstContentBean.setMsgIdServer(msgIdServer);
//            }

            dqImBaseBean.setContent(firstContentBean);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return dqImBaseBean;
    }

    /**
     * 解析斗圈通用消息格式
     * @param json
     * @return
     */
    public static ImMessageBaseModel getBaseMessageModel(String json){
        DqImBaseBean dqImBaseBean = getDqBaseMessageModel(json);
        return CommonImConvertDqIm.imConvertCommonImDq(dqImBaseBean);
    }


    /**
     * 获取批量的群组消息
     * @param json
     * @return
     */
    public static List<TeamMessageBaseModel> getTeamMessageModels(String json){
        List<TeamMessageBaseModel> teamMessageBaseModels = new ArrayList<>();
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(json);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++){
                String childJson = jsonArray.getString(i);
                TeamMessageBaseModel model = (TeamMessageBaseModel)getBaseMessageModel(childJson);
                teamMessageBaseModels.add(model);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return teamMessageBaseModels;
    }

    /**
     * 获取批量的个人消息
     * @param json
     * @return
     */
    public static List<P2PMessageBaseModel> getP2PMessageModels(String json){
        List<P2PMessageBaseModel> p2PMessageBaseModels = new ArrayList<>();
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(json);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++){
                String childJson = jsonArray.getString(i);
                P2PMessageBaseModel model = (P2PMessageBaseModel)getBaseMessageModel(childJson);
                p2PMessageBaseModels.add(model);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return p2PMessageBaseModels;
    }

}