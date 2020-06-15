package com.wd.daquan.net;


import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.model.utils.ModelUtil;
import com.wd.daquan.net.bean.RequestEntity;
import com.wd.daquan.net.callback.ObjectCallback;
import com.wd.daquan.net.callback.StringCallback;

import java.io.File;
import java.util.Map;

/**
 * @author: dukangkang
 * @date: 2018/6/8 10:49.
 * @description: todo ...
 */
public class RequestHelper {
    /**
     * 获取请求实体对象
     * @return
     */
    public static RequestEntity getRequestEntity() {
        EBSharedPrefUser userInfoSp = QCSharedPrefManager.getInstance().getKDPreferenceUserInfo();
        String uid = userInfoSp.getString(EBSharedPrefUser.uid, "");
        String token = userInfoSp.getString(EBSharedPrefUser.token, "");

        //Log.e("TAG", "uid : " + uid + " , : token : " + token);
        RequestEntity entity = new RequestEntity();
        entity.setUid(uid);
        entity.setToken(token);
        entity.setVersion(ModelUtil.getVersion());
        return entity;
    }

    /**
     * 请求数据
     * @param url
     *  请求地址
     * @param hashMap
     *  请求参数
     * @param callback
     *  回调接口
     */
    public static void request(String url, Map<String, String> hashMap, ObjectCallback callback) {
        RequestEntity entity = RequestHelper.getRequestEntity();
        entity.setUrl(url);
        entity.setParams(hashMap);
        OkHttpHelper.postByObject(entity, callback);
    }

    public static void request(String url, Map<String, String> hashMap, File file, String fileKey, ObjectCallback callback) {
        RequestEntity entity = RequestHelper.getRequestEntity();
        entity.setParams(hashMap);
        entity.setUrl(url);
        OkHttpHelper.postByObject(entity, file, fileKey, callback);
    }

    /**
     * 请求数据
     * @param url
     * @param hashMap
     * @param callback
     */
    public static void requestByString(String url, Map<String, String> hashMap, StringCallback callback) {
        RequestEntity entity = RequestHelper.getRequestEntity();
        entity.setUrl(url);
        entity.setParams(hashMap);
        OkHttpHelper.postByString(entity, callback);
    }



}
