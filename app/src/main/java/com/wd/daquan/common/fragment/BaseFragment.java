package com.wd.daquan.common.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.da.library.dialog.LoadingDialog;
import com.wd.daquan.common.presenter.Presenter;


/**
 * @author: dukangkang
 * @date: 2018/4/24 15:44.
 * @description: todo ...
 */
public abstract class BaseFragment<P extends Presenter.IPresenter, T> extends Fragment implements Presenter.IView<T> {

    public P mPresenter = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        if (null != mPresenter) {
            mPresenter.attachView(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    protected abstract int getLayoutId();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
        initData();
        initListener();
    }

    public abstract P createPresenter();

    public P getPresenter() {
        return mPresenter;
    }

    public abstract void initView(View view, Bundle savedInstanceState);

    public abstract void initListener();

    public abstract void initData();

    @Override
    public void onDestroyView() {
        dismissLoading();
        super.onDestroyView();
        if (null != mPresenter) {
            mPresenter.detachView();
        }
    }

    @Override
    public void showLoading() {
        LoadingDialog.show(getActivity(), "正在加载...");
    }

    @Override
    public void showLoading(String tipMessage) {
        LoadingDialog.show(getActivity(), tipMessage);
    }

    @Override
    public void dismissLoading() {
        LoadingDialog.closeLoadingDialog();
    }

    @Override
    public void onFailed(String url, int code, T entity) {

    }

    @Override
    public void onSuccess(String url, int code, T entity) {

    }

    public View getSharedElement() {
        return null;
    }

    public View getSharedElementForPlay() {
        return null;
    }
    public void updateShareTransition() {

    }
}
