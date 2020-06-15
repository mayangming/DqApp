package com.wd.daquan.model.utils;

import android.content.Context;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 方志
 * @Time: 2018/5/29 15:57
 * @Description: Gson解析工具类
 */
public class GsonUtils {

    private static Gson gson = null;

    public static Gson getGson() {
        return gson;
    }

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    /**
     * 解析成对象
     *
     * @param data  数据
     * @param clazz 类
     * @param <T>   数据类型
     * @return json对象
     */
    public static <T> T fromJson(String data, Class<T> clazz) {
        try {
            return gson.fromJson(data, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析成集合对象
     *
     * @param data  数据
     * @param clazz 类
     * @param <T>   数据类型
     * @return json集合
     */
    public static <T> List<T> fromJsonList(String data, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            JsonArray array = new JsonParser().parse(data).getAsJsonArray();
            for (JsonElement elem : array) {
                list.add(gson.fromJson(elem, clazz));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 转json
     *
     * @param data 数据
     * @return json字符串
     */
    public static String toJson(Object data) {
        try {
            return gson.toJson(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *  获取raw目录下文件
     * @param context 引用对象
     * @param id 资源文件id
     * @return json字符串
     */
    public static String getJson(Context context, int id) {
        StringBuilder jsonStr = new StringBuilder();
        InputStream stream = null;
        BufferedReader reader = null;
        try {
            stream = context.getResources().openRawResource(id);
            reader = new BufferedReader(new InputStreamReader(stream));
            String line;

            while ((line = reader.readLine()) != null) {
                jsonStr.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != stream) {
                try {
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(null != reader) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonStr.toString();
    }
}
