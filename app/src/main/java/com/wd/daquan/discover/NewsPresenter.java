package com.wd.daquan.discover;

import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.net.RequestHelper;
import com.wd.daquan.common.presenter.BasePresenter;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.net.callback.ObjectCallback;

import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by DELL on 2018/9/26.
 */

public class NewsPresenter extends BasePresenter<Presenter.IView<DataBean>> {
    /***
     *发现列表
     */
    public void getDiscoverList(String url, LinkedHashMap<String, String> linkedHashMap) {
        showLoading();
        RequestHelper.request(url, linkedHashMap, new ObjectCallback<DataBean<List<DiscoverMenuEntity>>>(){
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<List<DiscoverMenuEntity>> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<List<DiscoverMenuEntity>> result, Exception e) {
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
}
