package com.wd.daquan.mine.collection;

import com.wd.daquan.common.bean.AliOssResp;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.GroupInfoBean;
import com.wd.daquan.model.bean.UserBean;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.retrofit.RetrofitHelp;
import com.wd.daquan.net.RequestHelper;
import com.wd.daquan.common.presenter.BasePresenter;
import com.wd.daquan.net.callback.ObjectCallback;
import com.da.library.tools.MD5;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by DELL on 2019/2/25.
 */

public class QCCollectionPresenter extends BasePresenter {

    //添加收藏
    public void requestAddCollection(String url, Map<String, String> hashMap) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean result, Exception e) {
                failed(url, code, result);
            }

            @Override
            public void onFinish() {
                hideLoading();
            }
        });
    }

    /***
     * 阿里云临时授权token
     */
    public void getAliOssToken(String url, Map<String, String> hashMap, String tag){
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<AliOssResp>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<AliOssResp> result) {
                result.tag = tag;
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<AliOssResp> result, Exception e) {
                failed(url, code, result);
            }

            @Override
            public void onFinish() {
                hideLoading();
            }
        });
    }

    /**
     * 我的收藏
     * @param url
     * @param hashMap
     */
    public void getCNCollection(String url, LinkedHashMap<String, String> hashMap) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<CollectionListBean>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<CollectionListBean> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<CollectionListBean> result, Exception e) {
                super.onFailed(call, url, code, result, e);
                failed(url, code, result);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoading();
            }
        });
    }

    /**
     * 我的收藏
     * @param url
     * @param hashMap
     */
    public void deleteCNCollection(String url, LinkedHashMap<String, String> hashMap, int position) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<List<CollectionUploadMsgBean>>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<List<CollectionUploadMsgBean>> result) {
                super.onSuccess(call, url, code, result);
                result.tag = String.valueOf(position);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<List<CollectionUploadMsgBean>> result, Exception e) {
                super.onFailed(call, url, code, result, e);
                failed(url, code, result);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoading();
            }
        });
    }

    public void getTeamInfo(String url, Map<String, String> hashMap) {

        RetrofitHelp.getGroupApi().getGroupInfo(url, getRequestBody(hashMap)).enqueue(
                new DqCallBack<DataBean<GroupInfoBean>>() {
                    @Override
                    public void onSuccess(String url, int code, DataBean<GroupInfoBean> entity) {
                        success(url, code, entity);
                    }

                    @Override
                    public void onFailed(String url, int code, DataBean<GroupInfoBean> entity) {
                        failed(url, code, entity);
                    }
                });
    }

    /**
     * 获取单人用户信息
     */
    public void getUserInfo(String url, LinkedHashMap<String, String> hashMap) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<UserBean>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<UserBean> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<UserBean> result, Exception e) {
                super.onFailed(call, url, code, result, e);
                failed(url, code, result);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoading();
            }
        });
    }

    /**
     * 图片收藏
     *
     * @param from_uid
     * @param fileWebUrl
     */
    public void addCollectionImg(String from_uid, String fileWebUrl) {
        addCollection(from_uid, "3", fileWebUrl);
    }

    /**
     * 收藏消息
     *
     * @param from_uid   对方ID
     * @param type       收藏消息类型 1：文本 2：语音 3：图片 4：视频 5：文件
     * @param fileWebUrl
     */
    public void addCollection(String from_uid, String type, String fileWebUrl) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(KeyValue.FROM_UID, from_uid);
        hashMap.put(KeyValue.MESSAGE_ID, MD5.encrypt(fileWebUrl));
        hashMap.put(KeyValue.TYPE, type);
        hashMap.put(KeyValue.CONTENT, fileWebUrl);
//        hashMap.put(KeyValue.EXTRA, json);//扩展字段不用传
        showLoading();
        RequestHelper.request(DqUrl.url_collection_add, hashMap, new ObjectCallback<DataBean>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean result) {
                DqToast.showShort("收藏成功");
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean result, Exception e) {
                DqToast.showShort(result == null ? "收藏失败" : result.msg);
            }

            @Override
            public void onFinish() {
                hideLoading();
            }
        });
    }
}
