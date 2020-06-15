package com.wd.daquan.http;

import android.widget.Toast;

import com.dq.im.DqWebSocketClient;
import com.dq.im.constants.URLUtil;
import com.google.gson.Gson;
import com.wd.daquan.DqApp;
import com.wd.daquan.model.bean.UserBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketException;
import java.util.Map;

import http.OkHttpUtil;

/**
 * IM的SDK的请求处理
 */
public class ImSdkHttpUtils {


//    /**
//     * 公共请求参数处理
//     */
//    private static String createRequestJson(Map<String,Object> map){
//        String content;
////        HttpBaseRequestBean httpBaseRequestBean = new HttpBaseRequestBean();
//        RequestBean bean = RetrofitHelp.getRequestBean();
//        if(map != null) {
//            bean.setParams(map);
//        }
//        String json = GsonUtils.toJson(bean.getParams());
//        Gson gson = new Gson();
//        content = gson.toJson(bean);
//        try {
//            JSONObject jsonObject = new JSONObject(content);
//            if (null == map || map.size() == 0){
//                jsonObject.put("data", new JSONObject());
//            }else {
//                jsonObject.put("data", new JSONObject(map));
//            }
//
////            jsonObject.put("user",new UserBean());
//            jsonObject.put("user","");
//            content = jsonObject.toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return content;
//    }

    /**
     * 公共请求参数处理
     */
    private static String createRequestJson(Map<String,Object> hashMap){
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
    /**
     * 公共请求参数处理
     */
    private static String createRequestJson(Object object){
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
    public static void postJson(boolean isCheckSocket,String url,Map<String,Object> params , HttpResultResultCallBack callBack){
        if (isCheckSocket){
            boolean isSocketConnecting = checkSocketConnectStatus();
            if (!isSocketConnecting){
                Toast.makeText(DqApp.sContext,"Socket链接失败,请稍后重试~",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        OkHttpUtil.postJson().url(URLUtil.getURL(url)).content(createRequestJson(params)).build().execute(callBack);
    }
    /**
     * 获取数据的接口
     * 只能传递一层参数
     */
    public static void postJson(boolean isCheckSocket,String host,String url,Map<String,Object> params , HttpResultResultCallBack callBack){
        if (isCheckSocket){
            boolean isSocketConnecting = checkSocketConnectStatus();
            if (!isSocketConnecting){
                Toast.makeText(DqApp.sContext,"Socket链接失败,请稍后重试~",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        OkHttpUtil.postJson().url(URLUtil.getURL(host,url)).content(createRequestJson(params)).build().execute(callBack);
    }

    /**
     * 获取数据的接口
     * 只能传递一层参数
     */
    public static void postJson(String url,Map<String,Object> params , HttpResultResultCallBack callBack){
        postJson(true,url,params,callBack);
    }

    /**
     * 获取数据的接口
     * 只能传递一层参数
     */
    public static void postJson(boolean isCheckSocket,String url,Object params , HttpResultResultCallBack callBack){
        if (isCheckSocket){
            boolean isSocketConnecting = checkSocketConnectStatus();
            if (!isSocketConnecting){
                Toast.makeText(DqApp.sContext,"Socket链接失败,请稍后重试~",Toast.LENGTH_SHORT).show();
                SocketException socketException = new SocketException("");
                callBack.onError(null,socketException,-1);
                return;
            }
        }
        OkHttpUtil.postJson().url(URLUtil.getURL(url)).content(createRequestJson(params)).build().execute(callBack);
    }

    /**
     * 获取数据的接口
     * 只能传递一层参数
     */
    public static void postJson(String url,Object params , HttpResultResultCallBack callBack){
        postJson(true,url,params,callBack);
    }

    /**
     * 检查Socket链接状态
     */
    private static boolean checkSocketConnectStatus(){
        boolean isSocketConnecting = DqWebSocketClient.getInstance().isConnectIng();
        if (!isSocketConnecting){
            DqWebSocketClient.getInstance().build();
        }
        return isSocketConnecting;
    }

}