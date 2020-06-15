package com.wd.daquan.chat.group.activity;

import com.wd.daquan.chat.group.bean.TeamListEntity;
import com.wd.daquan.chat.group.bean.UnreceivedEntity;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.TeamBean;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.retrofit.RetrofitHelp;
import com.wd.daquan.net.RequestHelper;
import com.wd.daquan.common.presenter.BasePresenter;
import com.wd.daquan.net.callback.ObjectCallback;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * @author: dukangkang
 * @date: 2018/9/19 20:01.
 * @description: todo ...
 */
public class TeamPresenter extends BasePresenter {

    /**
     * 选择一个我所属的群聊
     * @param url
     * @param hashMap
     */
    public void getTeamList(String url, LinkedHashMap<String, String> hashMap)  {
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
    /***
     * 加入群组接口
     */
    public void addGroup(String url, Map<String, String> hashMap)  {
        showLoading();
        RetrofitHelp.request(url, hashMap, new DqCallBack() {
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

    /**
     * 获取群组内长时间未领取红包
     * @param url
     * @param linkedHashMap
     */
    public void getLongTimeRp(String url, LinkedHashMap<String, String> linkedHashMap) {
        showLoading();
        RequestHelper.request(url, linkedHashMap, new ObjectCallback<DataBean<UnreceivedEntity>>(){
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<UnreceivedEntity> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<UnreceivedEntity> result, Exception e) {
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
