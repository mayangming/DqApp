package com.wd.daquan.login.presenter;

import com.wd.daquan.common.presenter.BasePresenter;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.LoginBean;
import com.wd.daquan.model.bean.WxBindBean;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.retrofit.RetrofitHelp;
import com.wd.daquan.net.RequestHelper;
import com.wd.daquan.net.callback.ObjectCallback;

import java.io.File;
import java.util.Map;

/**
 * @Author: 方志
 * @Time: 2018/9/10 14:26
 * @Description:
 */
public class LoginPresenter extends BasePresenter< Presenter.IView<DataBean>> {

    /**
     * 获取短信验证码
     */
    public void getVerificationCode(String url, Map<String, String> hashMap) {
        request(url, hashMap);
    }

    /**
     * 无返回bean对象请求
     */
    private void request(String url, Map<String, String> hashMap) {
//        showLoading();
        RetrofitHelp.request(url, hashMap, new DqCallBack<DataBean>() {

            @Override
            public void onSuccess(String url, int code, DataBean entity) {
//                hideLoading();
                success(url, code, entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean entity) {
//                hideLoading();
                failed(url, code, entity);
            }
        });
    }


    /***
     * 注册
     */
    public void register(String url, Map<String, String> hashMap) {
        requestLoginBean(url, hashMap);
    }

    /**
     * 设置用户信息, 上传头像文件
     */
    public void setUserInfo(String url, Map<String, String> hashMap, String fileKey, File file) {
        showLoading();
        RequestHelper.request(url, hashMap, file, fileKey, new ObjectCallback<DataBean<LoginBean>>() {

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

    /**
     * 设置用户信息, 只上传昵称
     */
    public void setUserInfo(String url, Map<String, String> hashMap) {
        requestLoginBean(url, hashMap);
    }

    /**
     * 设置登录密码
     */
    public void setPassword(String url, Map<String, String> hashMap) {
        request(url, hashMap);
    }

    /**
     * 登录,密码,验证码,微信
     */
    public void loginPassword(String url, Map<String, String> hashMap) {
        requestLoginBean(url, hashMap);
    }

    private void requestLoginBean(String url, Map<String, String> hashMap) {

        showLoading();
        RetrofitHelp.getUserApi().login(url, getRequestBody(hashMap)).enqueue(
            new DqCallBack<DataBean<LoginBean>>() {

                @Override
                public void onSuccess(String url, int code, DataBean<LoginBean> entity) {
                    hideLoading();
                    success(url, code, entity);
                }

                @Override
                public void onFailed(String url, int code, DataBean<LoginBean> entity) {
                    hideLoading();
                    failed(url, code, entity);
                }
            });

    }

    /**
     * 获取微信绑定验证码
     */
    public void getWXVerificationCode(String url, Map<String, String> hashMap) {
        requestWxBindBean(url, hashMap);
    }

    private void requestWxBindBean(String url, Map<String, String> hashMap) {

        RetrofitHelp.getUserApi().wxBindPhone(url,getRequestBody(hashMap)).enqueue(new DqCallBack<DataBean<WxBindBean>>() {
            @Override
            public void onSuccess(String url, int code, DataBean<WxBindBean> entity) {
                success(url, code, entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean<WxBindBean> entity) {
                failed(url, code, entity);
            }
        });
//        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<WxBindBean>>() {
//
//            @Override
//            public void onSuccess(okhttp3.Call call, String url, int code, DataBean<WxBindBean> result) {
//                success(url, code, result);
//            }
//
//            @Override
//            public void onFailed(okhttp3.Call call, String url, int code, DataBean<WxBindBean> result, Exception e) {
//                failed(url, code, result);
//            }
//
//            @Override
//            public void onFinish() {
//                hideLoading();
//            }
//        });
    }

    /**
     * 绑定微信数据
     */
    public void getUseWeixinInfo(String url, Map<String, String> hashMap) {
        requestWxBindBean(url, hashMap);
    }
}
