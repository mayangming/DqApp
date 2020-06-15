package com.wd.daquan.common.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.da.library.dialog.LoadingDialog;
import com.da.library.tools.ActivitysManager;
import com.da.library.view.DqToolbar;
import com.da.library.widget.AnimUtils;
import com.da.library.widget.CommTitle;
import com.meetqs.qingchat.imagepicker.immersive.ImmersiveManage;
import com.netease.nim.uikit.common.fragment.TFragment;
import com.umeng.analytics.MobclickAgent;
import com.wd.daquan.R;
import com.wd.daquan.common.presenter.Presenter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: dukangkang
 * @date: 2018/4/24 15:44.
 * @description: todo ...
 */
public abstract class DqBaseActivity<P extends Presenter.IPresenter, T> extends AppCompatActivity implements Presenter.IView<T>, View.OnClickListener{

    public P mPresenter = null;
    protected boolean enableAnim = true;
    private WeakReference<Activity> mWeakReference = null;
    protected CommTitle mTitleLayout;
    protected DqToolbar mTitleDqLayout;
    /**
     * 状态栏文字颜色是否显示黑色
     */
    protected int mStatusBarColor = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();

        mPresenter = createPresenter();
        if (null != mPresenter) {
            mPresenter.attachView(this);
        }
        setContentView();
        mWeakReference = new WeakReference<>(this);
        ActivitysManager.getInstance().add(this);
        init();
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    protected void initStatusBar() {

        if(mStatusBarColor == 0) {
            mStatusBarColor = Color.WHITE;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(mStatusBarColor);
        }
        // 设置沉浸式字体颜色
        ImmersiveManage.setStatusFontColor(getWindow(), mStatusBarColor);
    }

    public void setStatusBarColor(int statusBarColor) {
        this.mStatusBarColor = statusBarColor;
    }

    protected abstract P createPresenter();

    protected abstract void setContentView();

    protected void init(){}

    protected abstract void initView();

    protected abstract void initData();

    protected void initListener(){
        if(mTitleLayout != null) {
            mTitleLayout.getLeftIv().setOnClickListener(this);
        }
        if(mTitleDqLayout != null) {
            mTitleDqLayout.getBackIv().setOnClickListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
//        Utils.cancelNotify();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);

    }

    @Override
    protected void onDestroy() {
        dismissLoading();
        super.onDestroy();
        if (null != mPresenter) {
            mPresenter.detachView();
        }
        ActivitysManager.getInstance().finish(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                if (null != v.getWindowToken()) {
                    InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void finish() {
        super.finish();
        Activity activity = getActivity();
        if (null != activity && enableAnim) {
            AnimUtils.exitAnimForActivity(activity);
        }
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    public boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0];
            int top = l[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    @Override
    public void showLoading() {
        LoadingDialog.show(this, "正在加载...");
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

    /**
     * 限制sp字体不随系统改变
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    /**
     * fragment management
     */
    public TFragment addFragment(TFragment fragment) {
        List<TFragment> fragments = new ArrayList<TFragment>(1);
        fragments.add(fragment);

        List<TFragment> fragments2 = addFragments(fragments);
        return fragments2.get(0);
    }

    public List<TFragment> addFragments(List<TFragment> fragments) {
        List<TFragment> fragments2 = new ArrayList<TFragment>(fragments.size());

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        boolean commit = false;
        for (int i = 0; i < fragments.size(); i++) {
            // install
            TFragment fragment = fragments.get(i);
            int id = fragment.getContainerId();

            // exists
            TFragment fragment2 = (TFragment) fm.findFragmentById(id);

            if (fragment2 == null) {
                fragment2 = fragment;
                transaction.add(id, fragment);
                commit = true;
            }

            fragments2.add(i, fragment2);
        }

        if (commit) {
            try {
                transaction.commitAllowingStateLoss();
            } catch (Exception e) {

            }
        }

        return fragments2;
    }

    public TFragment switchContent(TFragment fragment) {
        return switchContent(fragment, false);
    }

    protected TFragment switchContent(TFragment fragment, boolean needAddToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(fragment.getContainerId(), fragment);
        if (needAddToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        try {
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {

        }

        return fragment;
    }

    protected void switchFragmentContent(TFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(fragment.getContainerId(), fragment);
        try {
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CommTitle getCommTitle(){
        return this.findViewById(R.id.base_title);
    }

    /**
     * toolbar返回鍵
     */
    public void toolbarBack(){
        findViewById(R.id.toolbar_back_iv).setOnClickListener(view -> finish());
    }

    /**
     * toolbar右边文字点击事件
     */
    public void toolbarRightTvOnClick(View.OnClickListener onClick){
        findViewById(R.id.toolbar_right_tv).setOnClickListener(onClick);
    }

    /**
     * toolbar右边图片点击事件
     */
    public void toolbarRightIvOnClick(View.OnClickListener onClick){
        findViewById(R.id.toolbar_right_iv).setOnClickListener(onClick);
    }

    public void setTitle(String txt) {
        getCommTitle().setTitle(txt);
    }

    public void setBackView() {
        CommTitle commTitle = getCommTitle();
        commTitle.setLeftVisible(View.VISIBLE);
        commTitle.getLeftIv().setOnClickListener(v -> finish());
    }

    public Activity getActivity() {
        Activity activity = mWeakReference.get();
        return activity;
    }

    public <V extends View> V getView(int viewId) {
        View view = super.findViewById(viewId);
        return (V) view;
    }

    @Override
    public void onClick(View v) {
        if(mTitleLayout != null && v.getId() == mTitleLayout.getLeftIv().getId()) {
            finish();
        }
        if(mTitleDqLayout != null && v.getId() == mTitleDqLayout.getBackIv().getId()) {
            finish();
        }
    }
}
