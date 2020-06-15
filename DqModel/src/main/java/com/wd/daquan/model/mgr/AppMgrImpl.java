package com.wd.daquan.model.mgr;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;

import com.wd.daquan.model.ModelConfig;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.model.utils.UIUtil;

/**
 * Created by Kind on 2019/3/8.
 */
public class AppMgrImpl implements AppMgr {

    private static final String DEVICE_SCREEN_WIDTH = "DEVICE_SCREEN_WIDTH";
    private static final String DEVICE_SCREEN_HEIGHT = "DEVICE_SCREEN_HEIGHT";

    private String packageName = "";    // 软件包名
    private int versionCode = 0;        // 软件版本号
    private String versionName = "";    // 软件版本名称
    private int labelRes = 0;           //应用名称

//    private String imei = "";           // 国际识别码, Android O 版本被废弃，使用以下方式接入
//    private String androidId = "";           // Android的唯一设备Id
//    String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    @SuppressLint("HardwareIds")
    @Override
    public void init() {
        try {
            PackageManager packageManager = ModelConfig.getContext().getPackageManager();
            PackageInfo packInfo;
            packInfo = packageManager.getPackageInfo(ModelConfig.getContext().getPackageName(), 0);
            // 获取软件基本信息
            versionCode = packInfo.versionCode;
            versionName = packInfo.versionName;
            packageName = packInfo.packageName;
            labelRes = packInfo.applicationInfo.labelRes;
//            // 获取硬件信息
//            if (ActivityCompat.checkSelfPermission(ModelConfig.getContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//                TelephonyManager mTm = (TelephonyManager) ModelConfig.getContext().getSystemService(Context.TELEPHONY_SERVICE);
//                if(mTm != null){
//                    imei = mTm.getDeviceId();
//                }
//            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void release() {

    }

    /**
     * 用户信息缓存
     * @return
     */
    private EBSharedPrefUser getKDPreferenceUserInfo() {
        return QCSharedPrefManager.getInstance().getKDPreferenceUserInfo();
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public int getVerCode() {
        return versionCode;
    }

    @Override
    public String getVerName() {
        return versionName;
    }

//    @Override
//    public String getIMEI() {
//        return imei;
//    }

    @Override
    public String getAppName() {
        if (labelRes == 0) {
            return "";
        }
        try {
            return Resources.getSystem().getString(labelRes);
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public int getScreenWidth(FragmentActivity activity) {
        int screenWidth = getKDPreferenceUserInfo().getInt(DEVICE_SCREEN_WIDTH, -1);
        if (screenWidth <= 0) {
            try {
                screenWidth = UIUtil.getScreenWidth(activity);
                getKDPreferenceUserInfo().saveInt(DEVICE_SCREEN_WIDTH, screenWidth);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return screenWidth;
    }

    @Override
    public int getScreenHeight(FragmentActivity activity) {
        int screenHeight = getKDPreferenceUserInfo().getInt(DEVICE_SCREEN_HEIGHT, -1);
        if (screenHeight <= 0) {
            try {
                screenHeight = UIUtil.getScreenHeight(activity);
                getKDPreferenceUserInfo().saveInt(DEVICE_SCREEN_HEIGHT, screenHeight);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return screenHeight;
    }
}
