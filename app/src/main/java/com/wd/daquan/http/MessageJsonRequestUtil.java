package com.wd.daquan.http;

import com.google.gson.Gson;
import com.wd.daquan.model.bean.UserBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public
class MessageJsonRequestUtil {
    /**
     * 公共请求参数处理
     */
    public static String createRequestJson(Object object){
        String content;
        HttpBaseRequestBean httpBaseRequestBean = new HttpBaseRequestBean();
        httpBaseRequestBean.setData(object);
        Gson gson = new Gson();
        content = gson.toJson(httpBaseRequestBean);
        return content;
    }

    /**
     * 公共请求参数处理
     */
    public static String createRequestJson(Map<String,Object> hashMap){
        String content;
        HttpBaseRequestBean httpBaseRequestBean = new HttpBaseRequestBean();
        Gson gson = new Gson();
        content = gson.toJson(httpBaseRequestBean);
        try {
            JSONObject jsonObject = new JSONObject(content);
            if (null == hashMap || hashMap.size() == 0){
                jsonObject.put("data", new JSONObject());
            }else {
                jsonObject.put("data", new JSONObject(hashMap));
            }

            jsonObject.put("user",new UserBean());
            content = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return content;
    }
}
