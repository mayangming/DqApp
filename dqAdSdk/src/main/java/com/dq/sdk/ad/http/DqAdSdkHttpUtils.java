package com.dq.sdk.ad.http;

import android.util.Log;
import android.webkit.URLUtil;

import com.dq.sdk.ad.config.UrlConfig;
import com.dq.sdk.ad.http.bean.HttpBaseRequestBean;
import com.dq.sdk.ad.http.callback.HttpResultResultCallBack;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import http.OkHttpUtil;
import http.callback.Callback;
import http.callback.StringCallback;

/**
 * IM的SDK的请求处理
 */
public class DqAdSdkHttpUtils {

    /**
     * 公共请求参数处理
     */
    private static String createRequestJson(Map<String,Object> map){
        String content;
        HttpBaseRequestBean httpBaseRequestBean = new HttpBaseRequestBean();
        Gson gson = new Gson();
        content = gson.toJson(httpBaseRequestBean);
        try {
            JSONObject jsonObject = new JSONObject(content);
            if (null == map || map.size() == 0){
                jsonObject.put("data", new JSONObject());
            }else {
                jsonObject.put("data", new JSONObject(map));
            }

//            jsonObject.put("user",new UserBean());
            content = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return content;
    }
    /**
     * 公共请求参数处理
     */
    private static String createRequestJson(Object object){
        Log.e("YM","传入的参数:"+object.toString());
        String content;
        HttpBaseRequestBean httpBaseRequestBean = new HttpBaseRequestBean();
        httpBaseRequestBean.setData(object);
        Gson gson = new Gson();
        content = gson.toJson(httpBaseRequestBean);
        return content;
    }

    /**
     * 获取数据的接口
     * 只能传递一层参数
     */
    public static void postJson(String url,Map<String,Object> params , HttpResultResultCallBack callBack){
        OkHttpUtil.postJson().url(UrlConfig.Companion.getURL(url)).content(createRequestJson(params)).build().execute(callBack);
    }
    /**
     * 获取数据的接口
     * 只能传递一层参数
     */
    public static void postJson(String url,Map<String,Object> params , Callback callBack){
        OkHttpUtil.postJson().url(UrlConfig.Companion.getURL(url)).content(createRequestJson(params)).build().execute(callBack);
    }

    /**
     * 获取数据的接口
     * 只能传递一层参数
     */
    public static void postJson(String url,Object params , HttpResultResultCallBack callBack){
        OkHttpUtil.postJson().url(UrlConfig.Companion.getURL(url)).content(createRequestJson(params)).build().execute(callBack);
    }
}