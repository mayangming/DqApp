package com.wd.daquan.sdk;

import com.wd.daquan.common.presenter.BasePresenter;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.model.bean.CreateTeamEntity;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.bean.LoginBean;
import com.wd.daquan.model.bean.OpenSdkLoginBean;
import com.wd.daquan.model.bean.TeamBean;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.retrofit.RetrofitHelp;
import com.wd.daquan.net.RequestHelper;
import com.wd.daquan.net.callback.ObjectCallback;

import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2018/11/15.
 */

public class SdkPresenter extends BasePresenter<Presenter.IView<DataBean>> {

    public void loginPassword(String url, Map<String, String> hashMap) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<LoginBean>>() {

            @Override
            public void onSuccess(okhttp3.Call call, String url, int code, DataBean<LoginBean> result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(okhttp3.Call call, String url, int code, DataBean<LoginBean> result, Exception e) {
                failed(url, code, result);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoading();
            }
        });
    }

    /***
     * 获取code
     */
    public void getAppCode(String url, Map<String, String> hashMap) {
        DqLog.e("YM,开始请求code:");
        showLoading();
        RetrofitHelp.getSdkApi().getAppCode(url,getRequestBody(hashMap)).enqueue(new DqCallBack<DataBean<OpenSdkLoginBean>>() {
            @Override
            public void onSuccess(String url, int code, DataBean<OpenSdkLoginBean> entity) {
                success(url, code, entity);
                hideLoading();
            }

            @Override
            public void onFailed(String url, int code, DataBean<OpenSdkLoginBean> entity) {
                failed(url, code, entity);
                hideLoading();
            }
        });
    }

    /***
     * 获取应用信息
     */
    public void verifyAppid(String url, Map<String, String> hashMap) {
        showLoading();
        RetrofitHelp.getSdkApi().getAppInfo(url,getRequestBody(hashMap)).enqueue(new DqCallBack<DataBean<OpenSdkLoginBean>>() {
            @Override
            public void onSuccess(String url, int code, DataBean<OpenSdkLoginBean> entity) {
                success(url, code, entity);
                hideLoading();
            }

            @Override
            public void onFailed(String url, int code, DataBean<OpenSdkLoginBean> entity) {
                failed(url, code, entity);
                hideLoading();
            }
        });
    }

    /***
     * 获取accessToken
     */
    public void getAccessToken(String url, Map<String, String> hashMap) {
        showLoading();
        RetrofitHelp.getSdkApi().getAccessToken(url,getRequestBody(hashMap)).enqueue(new DqCallBack<DataBean<OpenSdkLoginBean>>() {
            @Override
            public void onSuccess(String url, int code, DataBean<OpenSdkLoginBean> entity) {
                success(url, code, entity);
                hideLoading();
            }

            @Override
            public void onFailed(String url, int code, DataBean<OpenSdkLoginBean> entity) {
                failed(url, code, entity);
                hideLoading();
            }
        });
    }

    /**
     * 上传分享记录
     */
    public void sdkShareRecord(String url, Map<String, String> hashMap) {
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<OpenSdkLoginBean>>() {

            @Override
            public void onSuccess(okhttp3.Call call, String url, int code, DataBean<OpenSdkLoginBean> result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(okhttp3.Call call, String url, int code, DataBean<OpenSdkLoginBean> result, Exception e) {
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
     * 获取好友列表
     */
    public void getContacts(String url, Map<String, String> hashMap) {

        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<List<Friend>>>() {

            @Override
            public void onSuccess(okhttp3.Call call, String url, int code, DataBean<List<Friend>> result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(okhttp3.Call call, String url, int code, DataBean<List<Friend>> result, Exception e) {
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

}
