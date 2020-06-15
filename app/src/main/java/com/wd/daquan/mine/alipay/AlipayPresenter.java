package com.wd.daquan.mine.alipay;

import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.net.RequestHelper;
import com.wd.daquan.common.presenter.BasePresenter;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.net.callback.ObjectCallback;

import java.util.HashMap;

import okhttp3.Call;

/**
 * @Author: 方志
 * @Time: 2019/5/6 11:26
 * @Description:
 */
public class AlipayPresenter extends BasePresenter<Presenter.IView<DataBean>> {
    /**
     * 设置收款码图片
     * @param url
     * @param hashMap
     */
    public void setPaymentCode(String url, HashMap<String, String> hashMap) {
//        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean result, Exception e) {
                failed(url, code, result);
            }

            @Override
            public void onFinish() {
//                hideLoading();
            }
        });
    }

}
