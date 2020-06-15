package com.wd.daquan.common.presenter;

import com.wd.daquan.model.bean.CreateTeamEntity;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.bean.TeamBean;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.retrofit.RetrofitHelp;

import java.util.List;
import java.util.Map;

/**
 * @Author: 方志
 * @Time: 2018/9/21 9:55
 * @Description:
 */
public class SharePresenter extends BasePresenter<Presenter.IView<DataBean>> {

    /**
     * 获取好友列表
     */
    public void getContacts(String url, Map<String, String> hashMap) {

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
     * 选择一个我所属的群聊
     */
    public void getTeamList(String url, Map<String, String> hashMap)  {
        showLoading();
        RetrofitHelp.getGroupApi().getTeamList(url, getRequestBody(hashMap)).enqueue(
                new DqCallBack<DataBean<List<TeamBean>>>() {
                    @Override
                    public void onSuccess(String url, int code, DataBean<List<TeamBean>> entity) {
                        hideLoading();
                        success(url, code, entity);
                    }

                    @Override
                    public void onFailed(String url, int code, DataBean<List<TeamBean>> entity) {
                        hideLoading();
                        failed(url, code, entity);
                    }
                });
    }

    /**
     * 创建群组
     */
    public void createTeam(String url, Map<String, String> hashMap)  {
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
}
