package com.wd.daquan.chat.ait;

import com.wd.daquan.common.presenter.BasePresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.retrofit.RetrofitHelp;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author: dukangkang
 * @date: 2018/9/25 13:11.
 * @descriptio: todo ...
 */
public class AitPresenter extends BasePresenter {

    /**
     * 获取群组成员
     */
    public void getTeamMember(String url, LinkedHashMap<String, String> hashMap) {
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
