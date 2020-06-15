package com.wd.daquan.model.bean;

import android.os.Build;
import android.text.TextUtils;

import com.wd.daquan.model.utils.ModelUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 请求基类
 */
public class RequestBean {


    public String tag = "";

    public String uid = "";

    public String token = "";

    // 请求参数
    private Map<String, Object> mParamObj = new HashMap<>();

    /**
     * 特殊数据，例如红包
     */
    private HashMap<String, Object> mData;

    public Map<String, Object> getParams() {
        if (mParamObj == null) {
            mParamObj = new LinkedHashMap<>();
        }

        String data = getData();
        if (!TextUtils.isEmpty(data)) {
            mParamObj.put("data", data);
        }

        if (!TextUtils.isEmpty(uid)) {
            mParamObj.put("uid", uid);
        }

        if (!TextUtils.isEmpty(token)) {
            mParamObj.put("token", token);
        }

        JSONObject appInfo = getAppInfo();
        mParamObj.put("appinfo", appInfo);
        return mParamObj;
    }

    /**
     * 获取应用程序信息
     */
    private JSONObject getAppInfo() {
        JSONObject json = new JSONObject();
        try {
            json.put("app_version", ModelUtil.getVersion());
            json.put("device_type", "android");
            json.put("device_brand", Build.BRAND + Build.MODEL);
            json.put("os_version", Build.VERSION.RELEASE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


    /**
     * 针对特殊接口，参数特殊处理
     * @return
     *  发单人红包／发起转账接口
     */
    public String getData() {
        if (null == mData || 0 >= mData.size()) {
            return null;
        }
        JSONObject jsonObj = new JSONObject();
        Set<String> set = mData.keySet();
        try {
            for (String key : set) {
                jsonObj.put(key, mData.get(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObj.toString();
    }


    /**
     * 添加data数据
     */
    public void putData(String key, String value) {
        if (mData == null) {
            mData = new HashMap<>();
        }
        mData.put(key, value);
    }


    public void setParams(Map<String, String> hashMap) {
        if(mParamObj == null) {
            mParamObj = new HashMap<>();
        }
        mParamObj.putAll(hashMap);
    }
    public void setParamsByObj(Map<String, Object> hashMap) {
        if(mParamObj == null) {
            mParamObj = new HashMap<>();
        }
        mParamObj.putAll(hashMap);
    }
}
