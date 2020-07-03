package com.wd.daquan.login.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.da.library.constant.IConstant;
import com.da.library.tools.FileUtils;
import com.meetqs.qingchat.imagepicker.immersive.ImmersiveManage;
import com.wd.daquan.R;
import com.wd.daquan.common.bean.ShareBean;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.login.helper.LoginHelper;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.utils.GsonUtils;
import com.wd.daquan.sdk.bean.SdkShareBean;

public class StartActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private int mFlag = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarColor(Color.WHITE,this);
        // 设置沉浸式字体颜色
        ImmersiveManage.setStatusFontColor(getWindow(), Color.WHITE);
        super.onCreate(savedInstanceState);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initOtherAppData();
                finish();
            }
        }, 2000);

    }
    /**
     * 前往注册、登录主页
     */
    private void gotoLogin() {
        Intent intent = new Intent(StartActivity.this, SplashActivity.class);
        startActivity(intent);
        finish();
        //取消界面跳转时的动画，使启动页的logo图片与注册、登录主页的logo图片完美衔接
        overridePendingTransition(0, 0);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarColor(int statusColor, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            //取消设置Window半透明的Flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏为透明
            window.setStatusBarColor(statusColor);
        }
    }

    /**
     * 用来初始化其他APP传递过来的值
     */
    private void initOtherAppData(){
        if(ModuleMgr.getCenterMgr().isInstall()) {
            doOption();
        }else {
            gotoLogin();
        }
    }

    private void doOption() {
        mFlag = getIntent().getFlags();
        String action = getIntent().getAction();
        DqLog.e("YM------>action:"+action);
        DqLog.e("YM------>mFlag:"+mFlag);
        if (Intent.ACTION_SEND.equals(action)) {
            //系统分享 其它app直接分享图片 文字
            fromShareSystem();
        } else if (Intent.ACTION_VIEW.equals(action)) {
            fromShareApp();
        } else {
            startActivity(mFlag);
        }
    }
    /**
     * 来自系统分享
     */
    private void fromShareSystem() {
        if(isLogin()) {
            return;
        }
        int flag = getIntent().getFlags();
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        if (bundle.containsKey(Intent.EXTRA_STREAM)) { // 分享图片
            try {
                Uri uri = bundle.getParcelable(Intent.EXTRA_STREAM);
                // 图片路径
                String path = FileUtils.getRealFilePath(this, uri);
                ShareBean shareBean = new ShareBean();
                shareBean.enterType = IConstant.Share.SHARE;
                shareBean.textOrImage = IConstant.Share.IMAGE;
                shareBean.path = path;
                NavUtils.gotoShareActivity(this, shareBean);
                finish();
            } catch (Exception e) {
                DqToast.showShort(getResources().getString(R.string.nonsupport_format));
                startActivity(flag);
            }
        } else if (bundle.containsKey(Intent.EXTRA_TEXT)) { // 分享文字
            String text = getIntent().getStringExtra(Intent.EXTRA_TEXT);
            ShareBean shareBean = new ShareBean();
            shareBean.enterType = IConstant.Share.SHARE;
            shareBean.textOrImage = IConstant.Share.TEXT;
            shareBean.path = text;
            NavUtils.gotoShareActivity(this, shareBean);
            finish();
        } else {
            DqToast.showShort(getResources().getString(R.string.nonsupport_format));
            startActivity(flag);
        }
    }

    /**
     * 是否需要登录
     */
    private boolean isLogin(){

        String token = ModuleMgr.getCenterMgr().getToken();
        if (TextUtils.isEmpty(token)) {
            NavUtils.gotoLoginCodeActivity(this,null);
            finish();
            return true;
        }
        return false;
    }

    /**
     * 来自APP分享
     */
    private void fromShareApp() {
        Intent intent = getIntent();
        int flag = intent.getFlags();
        Uri uri = intent.getData();
        if (null == uri) {
            DqToast.showShort(getResources().getString(R.string.nonsupport_format));
            startActivity(flag);
            return;
        }

        String url = uri.toString();
        String host = uri.getHost();
        String content = uri.getQuery();

        if (TextUtils.isEmpty(url)) {
            startActivity(flag);
            return;
        }

        if (TextUtils.isEmpty(host)) {
            startActivity(flag);
            return;
        }

        DqLog.i("dqshare : " + host);
        if ("dqshare".equals(host)) {
            if (TextUtils.isEmpty(content)) {
                DqToast.showShort(getResources().getString(R.string.nonsupport_format));
                startActivity(flag);
                return;
            }

            String params =  new String(Base64.decode(content, Base64.DEFAULT));
//
            DqLog.i("share params : " + params);
            if (TextUtils.isEmpty(params)) {
                startActivity(flag);
                return;
            }

            SdkShareBean shareBean = GsonUtils.fromJson(params, SdkShareBean.class);

            if(shareBean != null) {
                NavUtils.gotoRecentlyContactsListActivity(this, shareBean);
                finish();
            }else {
                DqToast.showShort(getResources().getString(R.string.nonsupport_format));
                startActivity(flag);
            }
        } else if ("dqlogin".equals(host)){
            String params =  new String(Base64.decode(content, Base64.DEFAULT));
//
            DqLog.i("share params : " + params);
            if (TextUtils.isEmpty(params)) {
                startActivity(flag);
                return;
            }

            SdkShareBean shareBean = GsonUtils.fromJson(params, SdkShareBean.class);

            if(shareBean != null) {
                Log.e("YM","不为空");
                NavUtils.gotoQCOpenLoginSdkActivity(this, shareBean);
                finish();
            }else {
                Log.e("YM","为空");
                DqToast.showShort(getResources().getString(R.string.nonsupport_format));
                startActivity(flag);
            }

        }else {
            startActivity(flag);
        }
    }

    private void startActivity(int flag) {
        if(flag == -1) {
            return;
        }
        if(!isLogin()) {
            String uid = ModuleMgr.getCenterMgr().getUID();
            String im_token = ModuleMgr.getCenterMgr().getImToken();
            if (!TextUtils.isEmpty(uid)) {
//                ViewModelProviders.of(this).get(ApplicationViewModel.class).initRoomDataBase(uid);
                LoginHelper.login(this, uid, im_token);
            }
        }
    }

}