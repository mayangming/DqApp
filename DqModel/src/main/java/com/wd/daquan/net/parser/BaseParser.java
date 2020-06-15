package com.wd.daquan.net.parser;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * @author: dukangkang
 * @date: 2018/4/24 18:18.
 * @description: todo ...
 */
public abstract class BaseParser<T> {

    // 请求返回状态码
    public int respCode = 0;

    // 请求返回是否成功
    public boolean isSuccess = false;

    public String mParseType;

    public Type mType;

    public String mData = "";

    public BaseParser(String parseType) {
        mParseType = parseType;
    }

    public T parseResponse(Response response) {
        return parse(response);
    }

    public abstract T parse(Response response);

    /**
     * 解析data字段下数据
     * @param json
     * @return
     */
    protected String parseData(String json) {
        if (TextUtils.isEmpty(json)) {
            return "";
        }
        try {
            String status = "";
            JSONObject jsonObject = new JSONObject(json);
            // 注：参数变更 code->result state->status
            // 兼容旧参数
            if (jsonObject.has("code")) {
                respCode = jsonObject.getInt("code");
            }
            if (jsonObject.has("state")) {
                status = jsonObject.getString("state");
            }

            // 新参数
            if (jsonObject.has("result")) {
                respCode = jsonObject.getInt("result");
            }
            if (jsonObject.has("status")) {
                status = jsonObject.getString("status");
            }

            if ("ok".equals(status)) {
                isSuccess = true;
            } else {
                isSuccess = false;
            }

            if (jsonObject.has("data")) {
                String target = "";
                Object object = new JSONTokener(jsonObject.getString("data")).nextValue();
                if (object instanceof JSONArray) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    target = jsonArray.toString();
                } else if (object instanceof JSONObject) {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    target = dataObject.toString();
                }
                return target;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            isSuccess = false;
        }
        return null;
    }
}
