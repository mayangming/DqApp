package com.wd.daquan.model.retrofit;

import com.wd.daquan.model.api.DqBaseApi;
import com.wd.daquan.model.api.GroupApi;
import com.wd.daquan.model.api.SdkApi;
import com.wd.daquan.model.api.UserApi;
import com.wd.daquan.model.bean.RequestBean;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.utils.GsonUtils;

import java.io.File;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RetrofitHelp {
    //请求json数据
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    // 二进制流数据,常见文件下载／上传
    private static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");

    /**
     * UserApi
     */
    public static UserApi getUserApi() {
        return RetrofitClient.getInstance().getRetrofit().create(UserApi.class);
    }
    /**
     * GroupApi
     */
    public static GroupApi getGroupApi() {
        return RetrofitClient.getInstance().getRetrofit().create(GroupApi.class);
    }

    /**
     * SdkApi
     */
    public static SdkApi getSdkApi() {
        return RetrofitClient.getInstance().getRetrofit().create(SdkApi.class);
    }

    /**
     * 请求数据， 无bean结构
     */
    public static void request(String url, Map<String, String> hashMap, DqCallBack callback) {

        RetrofitClient.getInstance().getRetrofit()
                .create(DqBaseApi.class)
                .request(url, getRequestBody(hashMap))
                .enqueue(callback);
    }


    /**
     * 普通纯json参数请求
     * @param hashMap 请求参数
     * @return RequestBody
     */
    public static RequestBody getRequestBody(Map<String, String> hashMap) {
        RequestBean bean = getRequestBean();
        if(hashMap != null) {
            bean.setParams(hashMap);
        }
        String json = GsonUtils.toJson(bean.getParams());
        //Log.e("dq", "json : " + json);
        return RequestBody.create(JSON, json);
    }

    /**
     * 普通纯json参数请求
     * @param hashMap 请求参数
     * @return RequestBody
     */
    public static RequestBody getRequestBodyByFromData(Map<String, String> hashMap) {
        RequestBean bean = getRequestBean();
        FormBody.Builder builder = new FormBody.Builder();
        Set<String> ketSet = hashMap.keySet();
        for (String key : ketSet){
            String value = hashMap.get(key);
            builder.add(key,value);
            DqLog.e("YM--->key:"+key+"--->value:"+value);
        }
        builder.add("uid",bean.uid);
        builder.add("token",bean.token);
//        RequestBean bean = getRequestBean();
//        if(hashMap != null) {
//            bean.setParams(hashMap);
//        }
//        String json = GsonUtils.toJson(bean.getParams());
        //Log.e("dq", "json : " + json);
        return builder.build();
    }

    /**
     * 文件
     * @param fileMap 文件参数
     * @return MultipartBody
     */
    public static MultipartBody getFileBody(Map<String, File> fileMap) {

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (Map.Entry<String, File> entry: fileMap.entrySet()) {
            RequestBody fileBody = RequestBody.create(MEDIA_TYPE_STREAM, entry.getValue());
            builder.addFormDataPart(entry.getKey(), entry.getValue().getName(), fileBody);
        }
        return builder.build();
    }

    /**
     * 获取请求实体对象
     */
    public static RequestBean getRequestBean() {
        RequestBean bean = new RequestBean();
        bean.uid = ModuleMgr.getCenterMgr().getUID();
        bean.token = ModuleMgr.getCenterMgr().getToken();
        return bean;
    }

}
