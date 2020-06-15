package com.wd.daquan.third.helper;

import android.Manifest;
import android.media.MediaScannerConnection;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.utils.DqUtils;
import com.da.library.constant.DirConstants;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.glide.listener.ILoadFileListener;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.da.library.tools.FileUtils;
import java.io.File;

/**
 * @author: dukangkang
 * @date: 2019/3/21 17:47.
 * @description: todo ...
 */
public class ThirdHelper {


    /**
     * 有权限的保存文件
     *
     * @param activity
     * @param file
     */
    public static void savePermUrl(FragmentActivity activity, File file) {
        if (!checkSavePermission(activity)) {
            return;
        }

        saveImage(file);
    }

    public static void savePermUrl(FragmentActivity activity, String url) {
        if (!checkSavePermission(activity)) {
            return;
        }

        if (TextUtils.isEmpty(url)) {
            DqToast.showShort("保存文件不能为空！");
            return;
        }

        File cacheFile;
        if (url.startsWith("http") || url.startsWith("https")) {
            MsgMgr.getInstance().runOnChildThread(new Runnable() {
                @Override
                public void run() {
                    GlideUtils.loadFile(activity, url, new ILoadFileListener() {
                        @Override
                        public void loadFile(@NonNull File cacheFile) {
                            if (cacheFile == null) {
                                DqToast.showShort("保存失败，请重试！");
                            }

                            saveImage(cacheFile);
                        }
                    });
                }
            });
            return;
        }
        cacheFile = new File(url);
        saveImage(cacheFile);
    }

    /**
     * 是否有权限
     */
    private static boolean checkSavePermission(FragmentActivity activity) {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        try {
            if (null == activity || activity.isFinishing()) {
                return false;
            }
            if (!DqUtils.checkPermissions(activity, permissions)) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 保存图片
     *
     * @param file
     */
    public static void saveImage(File file) {
        if (file == null || !file.exists()) {
            DqToast.showShort(R.string.src_file_not_found);
            return;
        }

        // TODO: 2019/3/13 统一图片目录
        File dir = new File(DirConstants.DIR_IMAGES);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String name = System.currentTimeMillis() + ".jpg";
        FileUtils.copyFile(DqApp.sContext, file.getAbsolutePath(), DirConstants.DIR_IMAGES);
        MediaScannerConnection.scanFile(DqApp.sContext, new String[]{dir.getPath() + File.separator + name}, null, null);
        DqToast.showShort(R.string.save_picture_suc);
    }

}
