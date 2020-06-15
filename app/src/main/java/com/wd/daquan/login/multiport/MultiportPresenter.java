package com.wd.daquan.login.multiport;


import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.net.RequestHelper;
import com.wd.daquan.common.presenter.BasePresenter;
import com.wd.daquan.net.callback.ObjectCallback;

import java.util.Map;

import okhttp3.Call;

/**
 * @Author: 方志
 * @Time: 2018/7/12 18:38
 * @Description: 网页扫码登录退出
 */
public class MultiportPresenter extends BasePresenter {

    /**
     * 网页登录确认
     *
     * @param url     网址
     * @param hashMap 参数
     */
    public void webConfirmLogin(String url, Map<String, String> hashMap) {
        request(url, hashMap);
    }


    /**
     * 网页退出登录
     *
     * @param url     网址
     * @param hashMap 参数
     */
    public void getWebLogout(String url, Map<String, String> hashMap) {
        request(url, hashMap);
    }

    /**
     * 数据请求
     *
     * @param url     网址
     * @param hashMap 参数
     */
    private void request(String url, Map<String, String> hashMap) {
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
                super.onFinish();
                hideLoading();
            }
        });
    }
}
