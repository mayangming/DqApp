package com.wd.daquan;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import androidx.multidex.MultiDex;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.ad.libary.AdSdkManager;
import com.ad.libary.config.SDKAdBuild;
import com.ad.libary.type.AdType;
import com.da.library.DqLibConfig;
import com.da.library.constant.IConstant;
import com.dq.im.util.oss.AliOssUtil;
import com.dq.im.util.oss.OssConfig;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.session.emoji.AndroidEmoji;
import com.netease.nim.uikit.common.util.notification.FrontNotificationFilter;
import com.netease.nim.uikit.common.util.notification.NotificationFilter;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.wd.daquan.common.constant.Config;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.imui.constant.Constant;
import com.wd.daquan.model.ModelConfig;
import com.wd.daquan.model.config.ActivityLifecycleConfig;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.net.OkHttpHelper;
import com.wd.daquan.third.preferences.UserPreferences;
import com.wd.daquan.third.session.SessionHelper;
import com.wd.daquan.util.TTAdManagerHolder;
import com.wd.daquan.util.system.SystemUtils;
import com.zlw.main.recorderlib.RecordManager;
import com.zlw.main.recorderlib.recorder.RecordConfig;

import java.io.File;
import java.util.concurrent.Executors;

/**
 * @author: dukangkang
 * @date: 2018/9/6 11:24.
 * @description: todo ...
 */
public class DqApp extends Application {
    public static Context sContext = null;

    public static DqApp sApplication;

    public static Handler sHandler = new Handler();

    private int chatTextSize = 16;//聊天界面字体默认大小
    private NotificationFilter filter;
    private WebView mWv;//WebView初始化很慢，所以在这里做成全局的WebView，然后用到的页面把它加载进去

    // 栈顶Activity
//    public static Activity sTopActivity = null;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sApplication = this;
        ModelConfig.init(this);
        DqLibConfig.init(this);
//        NIMClient.init(this, null, NimSDKOptionConfig.getSDKOptions(this));
        // 以下逻辑只在主进程初始化时执行
        if (SystemUtils.isMainProcess(this)) {
            UserPreferences.setNotificationToggle(false);//禁止消息通知
            //云信
            initNimiSDK(this);
            //友盟 正式key 5b8f7e07f43e483ba60000c0
            UMConfigure.init(this, BuildConfig.UMENG_KEY, BuildConfig.UMENG_CHANNEL, UMConfigure.DEVICE_TYPE_PHONE, "");
            MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
            //微信登录
            PlatformConfig.setWeixin(IConstant.WX.APP_ID, IConstant.WX.WXAPPSECRET);
            //okhttp
            OkHttpHelper.setCertificate(this, Config.CERTIFICATE_NAME);

            ModuleMgr.initModule(this);
            AndroidEmoji.init(this);
            //activity生命周期
            ActivityLifecycleConfig.init(this);
            filter = new FrontNotificationFilter();
            //        第三个参数为SDK调试模式开关，调试模式的行为特性如下：
//        输出详细的Bugly SDK的Log；
//        每一条Crash都会被立即上报；
//        自定义日志将会在Logcat中输出。
//        建议在测试阶段建议设置成true，发布时设置为false。
//            CrashReport.initCrashReport(getApplicationContext(), "3a5883212b", BuildConfig.IS_DUBUG);
            CrashReport.initCrashReport(getApplicationContext(), "c30355c0b0", BuildConfig.IS_DUBUG);
            initWebView();
            new Thread(new Runnable() {//放在子线程中初始化，因为临时token需要请求接口
                @Override
                public void run() {
                    AliOssUtil.initOss(getBaseContext(), OssConfig.endpoint,OssConfig.stsServer);
                }
            }).start();
            initFile();
            RecordManager.getInstance().init(this, false);
            RecordManager.getInstance().changeFormat(RecordConfig.RecordFormat.MP3);
        }

        initAd();
    }
    /**
     * 初始化文件夹
     */
    private void initFile(){
        File file = new File(Constant.getAudioRecordDir());
        if (!file.exists()){
            file.mkdirs();
        }
    }
    private void initAd(){
        //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常,穿山甲的广告需要在所有进程中初始化
//        TTAdManagerHolder.init(this);
        SDKAdBuild sdkAdBuild = new SDKAdBuild();
        sdkAdBuild.mAppName = IConstant.AD.APP_NAME;
        sdkAdBuild.type = AdType.AD_DQ;
        AdSdkManager.getInstance(this).initSDKAd(sdkAdBuild);
    }

    public  WebView getWebView(){
        return mWv;
    }

    public void initWebView(){
        mWv = new WebView(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWv.setLayoutParams(layoutParams);
    }
    public NotificationFilter getNotificationFilter(){
        return filter;
    }

    private void initNimiSDK(Context context) {
        // 注册自定义推送消息处理，这个是可选项
//        NIMPushClient.registerMixPushMessageHandler(new DemoMixPushMessageHandler());
        // 初始化UIKit模块
        initUIKit(context);
    }

    private void initUIKit(Context context) {
        // 初始化
        NimUIKit.init(context);
        // IM 会话窗口的定制初始化。
        SessionHelper.init();
    }

    public static void runOnUiThread(Runnable runnable) {
        sHandler.post(runnable);
    }

    public static void runOnUiThread(Runnable runnable, long delayTime) {
        sHandler.postDelayed(runnable, delayTime);
    }

    public static DqApp getInstance() {
        return sApplication;
    }

    public void runInUIThread(Runnable task) {
        sHandler.post(task);
    }

    /**
     * 获取线程池中线程
     */
    public void getThread(Runnable runnable) {
        Executors.newCachedThreadPool().execute(runnable);
    }
    /**
     * 获取线程池中线程
     */
    public void getSingleThread(Runnable runnable) {
         Executors.newSingleThreadExecutor().execute(runnable);
    }


    public static Object getSystemServiceByName(String name) {
        return DqApp.sContext.getSystemService(name);
    }

    //聊天界面字体大小 赋值
    public void setChatTextSize(int txtSize) {
        if (txtSize == 0) {
            QCSharedPrefManager manager = QCSharedPrefManager.getInstance();
            int textSize = manager.getKDPreferenceUserInfo().getInt(EBSharedPrefUser.TEXTSIZE, KeyValue.ZERO);
            if (textSize == 0) {
                chatTextSize = KeyValue.SIXTEEN;
            } else {
                chatTextSize = textSize;
            }
        } else {
            chatTextSize = txtSize;
        }
    }

    //聊天界面字体大小 取值
    public int getChatTextSize() {
        return chatTextSize;
    }

    public static String getStringById(int id) {
        return sContext.getResources().getString(id);
    }


    public static int getColorById(int id) {
        return sContext.getResources().getColor(id);
    }

    public static Drawable getDrawableById(int id) {
        return sContext.getResources().getDrawable(id);
    }

    public static float getDimenById(int id) {
        return sContext.getResources().getDimension(id);
    }

}
