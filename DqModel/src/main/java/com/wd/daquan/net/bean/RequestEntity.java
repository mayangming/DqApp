package com.wd.daquan.net.bean;

import android.os.Build;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author: dukangkang
 * @date: 2018/4/24 19:05.
 * @description: todo ...
 */
public class RequestEntity {

    private String url;

    private String tag;

    private String uid;

    private String token;

    private String version = "";

    private boolean isCache = false;

    // 请求参数
    private Map<String, String> mParams = new HashMap<>();
    // 请求参数
    private Map<String, Object> mParamObj = new HashMap<>();

    // 请求Header参数
    private Map<String, String> mHeaders = new HashMap<>();

    // 针对数据特殊处理
    private Map<String, String> mDatas = new HashMap<>();

    public Map<String, Object> getParams() {
        if (mParams == null) {
            mParams = new LinkedHashMap<>();
        }
        if (mParamObj == null) {
            mParamObj = new LinkedHashMap<>();
        }

        String data = getData();
        if (!TextUtils.isEmpty(data)) {
            mParams.put("data", data);
        }

        if (!TextUtils.isEmpty(uid)) {
            mParams.put("uid", uid);
        }

        if (!TextUtils.isEmpty(token)) {
            mParams.put("token", token);
        }

//        String appInfo = getAppInfo();
//        if (!TextUtils.isEmpty(appInfo)) {
//            mParams.put("appinfo", appInfo);
//        }
        mParamObj.putAll(mParams);
        JSONObject appInfo = getAppInfo();
        mParamObj.put("appinfo", appInfo);
        //Log.e("dkk", "mParams: " + mParams.toString());
        return mParamObj;
    }

    /**
     * 获取应用程序信息
     * @return
     */
    private JSONObject getAppInfo() {
        JSONObject json = new JSONObject();
        try {
            json.put("app_version", version);
            json.put("device_type", "android");
            json.put("device_brand", Build.BRAND + Build.MODEL);
            json.put("os_version", Build.VERSION.RELEASE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    /**
     * 针对特殊接口，参数特殊处理
     * @return
     *  发单人红包／发起转账接口
     */
    public String getData() {
        if (null == mDatas || 0 >= mDatas.size()) {
            return null;
        }
        JSONObject jsonObj = new JSONObject();
        Set<String> set = mDatas.keySet();
        try {
            for (String key : set) {
                jsonObj.put(key, mDatas.get(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObj.toString();
    }

    /**
     * 添加参数
     * @param key
     * @param value
     */
    public void putParam(String key, String value) {
        if (mParams == null) {
            mParams = new HashMap<>();
        }
        mParams.put(key, value);
    }

    /**
     * 添加data数据
     * @param key
     * @param value
     */
    public void putData(String key, String value) {
        if (mDatas == null) {
            mDatas = new HashMap<>();
        }
        mDatas.put(key, value);
    }

    /**
     * 添加Header参数
     * @param key
     * @param value
     */
    public void putHeader(String key, String value) {
        if (mHeaders == null) {
            mHeaders = new HashMap<>();
        }
        mHeaders.put(key, value);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setData(Map<String, String> data) {
        mDatas = data;
    }

    public boolean isCache() {
        return isCache;
    }

    public void setCache(boolean cache) {
        isCache = cache;
    }

    public void setParams(Map<String, String> params) {
        mParams = params;
    }

    public void setHeaders(Map<String, String> headers) {
        mHeaders = headers;
    }
}
