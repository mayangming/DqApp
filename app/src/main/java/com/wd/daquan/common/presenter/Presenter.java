package com.wd.daquan.common.presenter;

/**
 * @author: dukangkang
 * @date: 2018/6/8 13:41.
 * @description: todo ...
 */
public interface Presenter {

    interface IView<T> {
        void showLoading();

        void showLoading(String tipMessage);

        void dismissLoading();

        void onFailed(String url, int code, T entity);

        void onSuccess(String url, int code, T entity);
    }

    interface IPresenter<V extends IView> {
        void attachView(V view);

        void detachView();

        V getView();
    }
}
