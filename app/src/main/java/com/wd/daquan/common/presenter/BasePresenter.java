package com.wd.daquan.common.presenter;


import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.retrofit.RetrofitHelp;

import java.io.File;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 */
public class BasePresenter<V extends Presenter.IView<DataBean>> implements Presenter.IPresenter<V> {

    // 可尝试修改成弱引用
    protected V mView = null;

    @Override
    public void attachView(V view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        if(null != mView) {
            this.mView = null;
        }
    }

    public boolean isAttached() {
        return mView != null;
    }


    @Override
    public V getView() {
        return mView;
    }

    /**
     * 请求成功
     * @param url
     * @param code
     * @param result
     */
    public void success(String url, int code, DataBean result) {
        if (isAttached()) {
            mView.onSuccess(url, code, result);
        }
    }

    /**
     * 请求失败
     * @param url
     * @param code
     * @param result
     */
    public void failed(String url, int code, DataBean result) {
        if (isAttached()) {
            mView.onFailed(url, code, result);
        }
    }

    public void showLoading() {
        if (isAttached()) {
            mView.showLoading();
        }
    }

    public void hideLoading() {
        if (isAttached()) {
            mView.dismissLoading();
        }
    }

    protected RequestBody getRequestBody(Map<String, String> hashMap) {
        return RetrofitHelp.getRequestBody(hashMap);
    }

    protected RequestBody getRequestBodyByObject(Map<String, Object> hashMap) {
        return RetrofitHelp.getRequestBodyByObject(hashMap);
    }

    protected RequestBody getRequestBodyByFromData(Map<String, String> hashMap) {
        return RetrofitHelp.getRequestBodyByFromData(hashMap);
    }

    protected MultipartBody getFileBody(Map<String, File> fileMap) {
        return RetrofitHelp.getFileBody(fileMap);
    }

    public Map<String, Object> getParams() {
        return RetrofitHelp.getRequestBean().getParams();
    }

    /**
     * 通用请求
     */
    public void commonRequest(String url, Map<String, String> hashMap) {
        showLoading();
        RetrofitHelp.getUserApi().getCommonRequest(url, getRequestBody(hashMap)).enqueue(
                new DqCallBack<DataBean<String>>() {

                    @Override
                    public void onSuccess(String url, int code, DataBean<String> entity) {
                        hideLoading();
                        success(url, code, entity);
                    }

                    @Override
                    public void onFailed(String url, int code, DataBean<String> entity) {
                        hideLoading();
                        failed(url, code, entity);
                    }
                });
    }


}
