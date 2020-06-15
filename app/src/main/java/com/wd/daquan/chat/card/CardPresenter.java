package com.wd.daquan.chat.card;

import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.retrofit.RetrofitHelp;
import com.wd.daquan.common.presenter.BasePresenter;
import com.wd.daquan.model.bean.Friend;

import java.util.List;
import java.util.Map;

/**
 * @author: dukangkang
 * @date: 2018/9/25 13:11.
 * @descriptio: todo ...
 */
public class CardPresenter extends BasePresenter {

    /**
     * 获取好友列表
     * @param url
     * @param hashMap
     */
    public void getFriend(String url, Map<String, String> hashMap) {
        showLoading();
        RetrofitHelp.getUserApi().getFriendList(url, getRequestBody(hashMap)).enqueue(
                new DqCallBack<DataBean<List<Friend>>>() {
                    @Override
                    public void onSuccess(String url, int code, DataBean<List<Friend>> entity) {
                        hideLoading();
                        success(url, code, entity);
                    }

                    @Override
                    public void onFailed(String url, int code, DataBean<List<Friend>> entity) {
                        hideLoading();
                        failed(url, code, entity);
                    }
                });
    }


}
