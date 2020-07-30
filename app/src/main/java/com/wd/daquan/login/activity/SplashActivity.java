package com.wd.daquan.login.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.da.library.constant.IConstant;
import com.da.library.tools.FileUtils;
import com.meetqs.qingchat.imagepicker.immersive.ImmersiveManage;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.bean.ShareBean;
import com.wd.daquan.common.helper.UpdateListener;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.login.helper.CommDialogHelper;
import com.wd.daquan.login.helper.LoginHelper;
import com.wd.daquan.login.presenter.SplashPresenter;
import com.wd.daquan.login.splash.SplashAdapter;
import com.wd.daquan.login.splash.SplashFragment;
import com.wd.daquan.mine.dialog.ProtocolDialog;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.UpdateEntity;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ConfigManager;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.utils.GsonUtils;
import com.wd.daquan.sdk.bean.SdkShareBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 方志
 * @Time: 2018/9/6 16:38
 * @Description: 欢迎界面，权限，系统分享，游戏分享，版本升级
 * @UPDATE: 2018/12/12 更换引导页使用方式
 */
public class SplashActivity extends DqBaseActivity<SplashPresenter, DataBean> implements UpdateListener, SplashFragment.SplashFragmentListener {

    /**
     * 基本权限管理
     */
    protected String[] needPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储卡写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE,//存储卡读取权限
            Manifest.permission.READ_PHONE_STATE,//手机状态
    };
    private List<Integer> mImages;
    private int mFlag = 0;
    //    private Banner mBanner;
//    private View mSkip;
    private ViewPager viewPager;//引导图轮播
    private SplashAdapter splashAdapter;//引导图轮播适配器
    private RadioGroup splashIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (!this.isTaskRoot()){
//            finish();
//            return;
//        }
//        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
//            finish();
//            return;
//        }
        DqApp.getInstance().setChatTextSize(0);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initStatusBar() {
        ImmersiveManage.setStatusTextColor(getWindow(), true);
    }

    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void init() {}

    @Override
    protected void initView() {
//        mBanner = findViewById(R.id.splash_banner);
//        mSkip = findViewById(R.id.splash_btn_skip);
        viewPager = findViewById(R.id.splash_vp);
        splashIndicator = findViewById(R.id.splash_indicator);
        if (ModuleMgr.getCenterMgr().getAgreeProtocolStatus()){
            initCheck();
        }else {
            showProtocolDialog();
        }
    }

    private void initCheck(){
        DqLog.e("YM","版本权限检测开始");
        if (DqUtils.checkPermissions(this, needPermissions)) {
            DqLog.e("YM","版本权限检测结束");
            initSplashData();
        }
    }

    private void initSplashData(){
        if(ModuleMgr.getCenterMgr().isInstall()) {
            doOption();
        }else {
            mImages = new ArrayList<>();
            mImages.add(R.mipmap.splash1);
            mImages.add(R.mipmap.splash2);
//            mImages.add(R.mipmap.splash3);

            //设置banner样式
//            mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
//                    //设置图片加载器
//                    .setImageLoader(new ImageLoader() {
//                        @Override
//                        public void displayImage(Context context, Object path, ImageView imageView) {
//                            GlideUtils.loadThumbnail(context, path, imageView);
//                        }
//                    })
//                    .setImages(mImages)
//                    .isAutoPlay(false)
//                    .setIndicatorGravity(BannerConfig.CENTER)
//                    .start();
            initSplash();
        }
    }

    @Override
    protected void initData() {

    }

    private void initSplash(){
        splashIndicator.setVisibility(View.VISIBLE);
        SplashFragment splashFragment1 = new SplashFragment();
        SplashFragment splashFragment2 = new SplashFragment();
        Bundle bundle1 = new Bundle();
        Bundle bundle2 = new Bundle();
        List<Fragment> fragments = new ArrayList<>();
        bundle1.putInt(SplashFragment.SPLASH_RES_KEY,R.mipmap.splash1);
        bundle1.putBoolean(SplashFragment.SPLASH_IS_SKIP_KEY,false);
        splashFragment1.setArguments(bundle1);
        fragments.add(splashFragment1);
        bundle2.putInt(SplashFragment.SPLASH_RES_KEY,R.mipmap.splash2);
        bundle2.putBoolean(SplashFragment.SPLASH_IS_SKIP_KEY,true);
        splashFragment2.setArguments(bundle2);
        fragments.add(splashFragment2);
        splashAdapter = new SplashAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(splashAdapter);
        ((RadioButton)splashIndicator.getChildAt(0)).setChecked(true);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                ((RadioButton)splashIndicator.getChildAt(position)).setChecked(true);
            }
        });
    }

    @Override
    protected void initListener() {
//        mSkip.setOnClickListener(view -> doOption());
//        mBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if(mImages != null) {
//                    mSkip.setVisibility((position == mImages.size() -1) ? View.VISIBLE : View.GONE);
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (!DqUtils.verifyPermissions(grantResults)) {
                CommDialogHelper.getInstance().showPermissionDialog(this);
            } else {
//                if (null != mUpdateHelper) {
//                    mUpdateHelper.checkVersion();
//                }
                initSplashData();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IConstant.PERMISSION_REQUEST_CODE) {
            if (DqUtils.checkPermissions(this, needPermissions)) {
//                if (null != mUpdateHelper) {
//                    mUpdateHelper.checkVersion();
//                }
                initSplashData();
            }
        }
    }

    @Override
    protected void onDestroy() {
//        if(mBanner != null) {
//            mBanner = null;
//        }
        super.onDestroy();
        CommDialogHelper.getInstance().onDestroy();
    }

    @Override
    public void updateError() {
        startActivity(mFlag);
    }

    @Override
    public void updateFailed(String msg) {
        startActivity(mFlag);
    }

    @Override
    public void updateSucceed(UpdateEntity entity) {
        ConfigManager.getInstance().saveRedEnvelopedRainSwitch(entity.redEnvelopedRainSwitch);
        doOption();
    }


    @Override
    public void updateUI(String status) {

    }

    @Override
    public void skipListener() {
        doOption();
    }

    private void doOption() {
        mFlag = getIntent().getFlags();
        String action = getIntent().getAction();
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
                NavUtils.gotoQCOpenLoginSdkActivity(this, shareBean);
                finish();
            }else {
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

    private void showProtocolDialog(){
        ProtocolDialog protocolDialog = new ProtocolDialog();
        protocolDialog.setDialogOperatorIpc(new ProtocolDialog.DialogOperatorIpc() {
            @Override
            public void sure() {
                ModuleMgr.getCenterMgr().putAgreeProtocolStatus(true);
                initCheck();
            }

            @Override
            public void cancel() {
                finish();
            }
        });
        protocolDialog.show(getSupportFragmentManager(),"对话框");
    }
}
