package com.wd.daquan.chat.friend;

import com.wd.daquan.common.presenter.BasePresenter;
import com.wd.daquan.model.bean.CreateTeamEntity;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.retrofit.RetrofitHelp;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: dukangkang
 * @date: 2018/9/18 20:39.
 * @description: todo ...
 */
public class SelectFriendPresenter extends BasePresenter {

    /**
     * 返回好友列表
     * 群成员列表
     */
    public void getFriendList(String url, Map<String, String> hashMap)  {
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

    /**
     * 创建群组
     * @param url
     * @param hashMap
     */
    public void createTeam(String url, LinkedHashMap<String, String> hashMap)  {
        showLoading();
        RetrofitHelp.getGroupApi().createTeam(url, getRequestBody(hashMap)).enqueue(
            new DqCallBack<DataBean<CreateTeamEntity>>() {
                @Override
                public void onSuccess(String url, int code, DataBean<CreateTeamEntity> entity) {
                    hideLoading();
                    success(url, code, entity);
                }

                @Override
                public void onFailed(String url, int code, DataBean<CreateTeamEntity> entity) {
                    hideLoading();
                    failed(url, code, entity);
                }
            }
        );
    }

    /**
     * 获取群组成员
     *
     * @param url
     * @param hashMap
     */
    public void getTeamMember(String url, Map<String, String> hashMap) {
        getFriendList(url, hashMap);
    }

    /**
     * 群组加人
     */
    public void jsonGroup(String url, Map<String, String> hashMap) {
        showLoading();
        RetrofitHelp.request(url, hashMap, new DqCallBack<DataBean>() {

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
}
