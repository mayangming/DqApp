package com.wd.daquan.third.friend;

import com.wd.daquan.common.presenter.BasePresenter;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.retrofit.RetrofitHelp;

import java.util.Map;

/**
 * @author: dukangkang
 * @date: 2018/9/27 16:04.
 * @description: todo ...
 */
public class FriendPresenter extends BasePresenter<Presenter.IView<DataBean>> {

    /**
     * 添加好友
     *
     */
    public void addFriend(String url, Map<String, String> hashMap) {
        showLoading();
        RetrofitHelp.getUserApi().getFriend(url, getRequestBody(hashMap)).enqueue(
                new DqCallBack<DataBean<Friend>>() {

                    @Override
                    public void onSuccess(String url, int code, DataBean<Friend> entity) {
                        hideLoading();
                        success(url, code, entity);
                    }

                    @Override
                    public void onFailed(String url, int code, DataBean<Friend> entity) {
                        hideLoading();
                        failed(url, code, entity);
                    }
                });
    }
    /**
     * 删除好友
     */
    public void delFriend(String url, Map<String, String> hashMap) {
        showLoading();
        RetrofitHelp.getUserApi().delFriend(url, getRequestBody(hashMap)).enqueue(
                new DqCallBack<DataBean>() {

                    @Override
                    public void onSuccess(String url, int code, DataBean entity) {
                        hideLoading();
                        success(url, code, entity);
                    }

                    @Override
                    public void onFailed(String url, int code, DataBean entity) {
                        hideLoading();
                        failed(url, code, entity);
                    }
                });
    }

    public void setFriendInfo(String url, Map<String, String> hashMap) {
        //网络层
        showLoading();
        RetrofitHelp.request(url, hashMap, new DqCallBack() {
            @Override
            public void onSuccess(String url, int code, DataBean entity) {
                success(url, code, entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean entity) {
                failed(url, code, entity);
            }
        });
    }

    public void getUserInfo(String url, Map<String, String> hashMap) {
        showLoading();
        RetrofitHelp.getUserApi().getFriend(url, getRequestBody(hashMap)).enqueue(
                new DqCallBack<DataBean<Friend>>() {

                    @Override
                    public void onSuccess(String url, int code, DataBean<Friend> entity) {
                        hideLoading();
                        success(url, code, entity);
                    }

                    @Override
                    public void onFailed(String url, int code, DataBean<Friend> entity) {
                        hideLoading();
                        failed(url, code, entity);
                    }
                });
    }
}
