package com.wd.daquan.common.utils;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.da.library.tools.ActivitysManager;
import com.wd.daquan.DqApp;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.log.DqToast;

import java.io.File;

/**
 * 描述:应用升级工具类
 */
public class InstallApkUtil {
    private static final String MIME_TYPE_APK = "application/vnd.android.package-archive";


    /**
     * 安装 apk 文件
     *
     */
    public static void installApk(File apkFile) {
        Intent installApkIntent = new Intent();
        installApkIntent.setAction(Intent.ACTION_VIEW);
        installApkIntent.addCategory(Intent.CATEGORY_DEFAULT);
        installApkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        String fileProviderAuthority = getFileProviderAuthority();
        if(TextUtils.isEmpty(getFileProviderAuthority())) {
            DqToast.showShort("安装失败");
            ActivitysManager.getInstance().finishAll();
            return;
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Uri apkUri = FileProvider.getUriForFile(DqApp.getInstance(), fileProviderAuthority, apkFile);
            DqLog.i("apkFile : " + apkFile.getPath());// /storage/emulated/0/qingchat/15396006456598.apk
            DqLog.i("apkUri : " + apkUri);//content://com.meetqs.qingchat.qcprovider/external_files/qingchat/15396006456598.apk
            installApkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            installApkIntent.setDataAndType(apkUri, MIME_TYPE_APK);
        } else {
            installApkIntent.setDataAndType(Uri.fromFile(apkFile), MIME_TYPE_APK);
        }

        if (DqApp.getInstance().getPackageManager().queryIntentActivities(installApkIntent, 0).size() > 0) {
            DqApp.getInstance().startActivity(installApkIntent);
        }
    }

    /**
     * 获取FileProvider的auth
     *
     * @return
     */
    private static String getFileProviderAuthority() {
        try {
            ProviderInfo[] providers = DqApp.getInstance().getPackageManager().
                    getPackageInfo(DqApp.getInstance().getPackageName(), PackageManager.GET_PROVIDERS).providers;
            for (ProviderInfo provider : providers) {
                if (provider.authority.endsWith("dqprovider")) {
                    return provider.authority;
                }
            }
        } catch (PackageManager.NameNotFoundException ignore) {
        }
        return "";
    }
}
