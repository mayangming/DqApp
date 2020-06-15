package com.wd.daquan.login.presenter;

import com.wd.daquan.common.presenter.BasePresenter;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.retrofit.RetrofitHelp;

import java.util.Map;

/**
 * @Author: 方志
 * @Time: 2018/9/18 9:37
 * @Description:
 */
public class SplashPresenter extends BasePresenter< Presenter.IView<DataBean>> {

    /**
     * 升级下载统计
     */
    public void recordUpgradeData(String url, Map<String, String> hashMap) {
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

}
