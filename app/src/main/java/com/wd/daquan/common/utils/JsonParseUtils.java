package com.wd.daquan.common.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.uikit.common.util.string.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 捕捉Json解析异常,避免出现由此引起 的crash
 * */
public class JsonParseUtils
{
	private static final String TAG_JSON = "JsonParseUtils";
	private static Gson gson;
	static {
		gson = new Gson();
	}

	public static Gson getGson() 
	{
		return gson;
	}

	/**
	 * 对象转成json
	 * @param <T>
	 */
	public static <T> String obj2Json(T t) 
	{
		return gson.toJson(t);
	}

	/**
	 * json转成对象
	 * 
	 * @param <T>
	 */
	public static <T> T json2Obj(String json, Class<T> clazz) 
	{
		return gson.fromJson(json, clazz);
	}
	
	/**
	 * json转成List
	 * 
	 * ex: new TypeToken<List<ChatMessage>>
	 * 
	 * @param typeToken 
	 */
	public static <T> List<T> json2List(String json, TypeToken<?> typeToken)
	{
		try {
			List<T> list = gson.fromJson(json, typeToken.getType());
			return list;
		} catch (Exception e) {
		}
		return new ArrayList<T>();
	}
	
	public static String getString(JSONObject json, String key)
	{
		return getString(json, key, "");
	}

	public static String getString(JSONObject json, String key, String def) 
	{
		String value = def;
		if (canParse(json, key)) {
			try {
				value = json.getString(key);
			} catch (JSONException e) {
				CNLog.e(TAG_JSON, "getString=" + e.getMessage());
			}
		}

		return value;
	}	
	
	public static int getInt(JSONObject json, String key)
	{
		return getInt(json, key, -1);
	}

	public static int getInt(JSONObject json, String key, int def) 
	{
		int value = def;
		if (canParse(json, key)) {
			try {
				value = json.getInt(key);
			} catch (JSONException e) {
				CNLog.e(TAG_JSON, "getInt=" + e.getMessage());
			}
		}

		return value;
	}
	
	public static long getLong(JSONObject json, String key)
	{
		return getLong(json, key, -1);
	}
	
	public static long getLong(JSONObject json, String key, long def)
	{
		long value = def;
		if (canParse(json, key)) {
			try {
				value = json.getLong(key);
			} catch (JSONException e) {
				CNLog.e(TAG_JSON, "getInt=" + e.getMessage());
			}
		}

		return value;
	}
	
	public static JSONObject getJSONObject(JSONArray array, int index)
	{
		return getJSONObject(array, index, new JSONObject());
	}
	
	public static JSONObject getJSONObject(JSONArray array, int index,JSONObject def) 
	{
		JSONObject returnObj = def;
		if (array != null && array.length() > 0) {
			try {
				returnObj = array.getJSONObject(index);
			} catch (JSONException e) {
				CNLog.e(TAG_JSON, "getJSONObject=" + e.getMessage());
			}
		}
		return returnObj;
	}
	
	public static JSONObject getJSONObject(JSONObject json, String key)
	{
		return getJSONObject(json, key, new JSONObject());
	}

	public static JSONObject getJSONObject(JSONObject json, String key, JSONObject def) 
	{
		JSONObject returnObj = def;
		if (canParse(json, key)) {
			try {
				returnObj = json.getJSONObject(key);
			} catch (JSONException e) {
				CNLog.e(TAG_JSON, "getJSONObject=" + e.getMessage());
			}
		}
		return returnObj;
	}
	
	public static JSONArray getJSONArray(JSONObject json, String key)
	{
		return getJSONArray(json, key, new JSONArray());
	}

	public static JSONArray getJSONArray(JSONObject json, String key, JSONArray def) 
	{
		JSONArray array = def;
		if (canParse(json, key)) {
			try {
				array = json.getJSONArray(key);
			} catch (JSONException e) {
				CNLog.e(TAG_JSON, "getJSONArray=" + e.getMessage());
			}
		}
		return array;
	}
	
	public static JSONObject getJSONObject(byte[] json)
	{
		return getJSONObject(StringUtil.bytesToString(json,"UTF-8"));
	}

	public static JSONObject getJSONObject(String json)
	{
		return getJSONObject(json, new JSONObject());
	}
	
	public static JSONObject getJSONObject(String json, JSONObject def)
	{
		JSONObject jsonObject = def;
		try {
			jsonObject = new JSONObject(json);
		} catch (Exception e) {  //不只是JsonException，还可能是别的异常
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	public static JSONArray getJSONArray(String json)
	{
		return getJSONArray(json, new JSONArray());
	}
	
	public static JSONArray getJSONArray(String json, JSONArray def)
	{
		JSONArray jsonArray = def;
		try {
			jsonArray = new JSONArray(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonArray;
	}

	/**
	 * 判断数据是否可以解析，可以则return true;
	 * */
	private static boolean canParse(JSONObject json, String key) 
	{
		if (json == null || TextUtils.isEmpty(key)) {
			return false;
		}
		return true;
	}
	
	
	public static <T> List<T> parseJsonArray2List(JSONArray jsonArray, Class<T> clazz) 
	{
		List<T> list = new ArrayList<T>();
		if(jsonArray==null) return list;
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = JsonParseUtils.getJSONObject(jsonArray, i, null);
			if(jsonObject==null) continue;
			T t = (T) JsonParseUtils.json2Obj(jsonObject.toString(),clazz);
			list.add(t);
		}
		return list;
	}
	
	public static void putValue(JSONObject json,String key,Object value)
	{
		try {
			json.put(key,value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
