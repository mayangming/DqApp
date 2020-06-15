package com.wd.daquan.common.alioss;

import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.net.callback.ObjectCallback;
import com.wd.daquan.common.bean.AliOssResp;
import com.wd.daquan.common.presenter.BasePresenter;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.net.RequestHelper;

import java.util.Map;

import okhttp3.Call;

/**
 * @author: dukangkang
 * @date: 2018/9/11 19:12.
 * @description: todo ...
 */
public class AliOssPresenter extends BasePresenter<Presenter.IView<DataBean>> {

    /**
     * 获取阿里云OSStoken
     */
    public void getAliOssToken(String url, Map<String, String> hashMap) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<AliOssResp>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<AliOssResp> result) {
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
}
