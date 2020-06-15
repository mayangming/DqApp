package com.wd.daquan.glide;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.wd.daquan.glide.constans.IConstans;

import java.io.File;
import java.math.BigDecimal;

/**
 * @Author: 方志
 * @Time: 2018/7/10 17:49
 * @Description: Glide缓存处理，取消请求, 使用单例
 */
public class GlideCacheUtil {

    private static GlideCacheUtil instance = null;

    private GlideCacheUtil(){
    }

    public static GlideCacheUtil getInstance() {
        synchronized (GlideCacheUtil.class) {
            if (instance == null) {
                instance = new GlideCacheUtil();
            }
        }
        return instance;
    }

    /**
     * 清理磁盘缓存
     *
     * @param context 引用对象
     */
    public void clearDiskCache(final Context context) {
        //理磁盘缓存 需要在子线程中执行
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Glide.get(context).clearDiskCache();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 清理内存缓存
     *
     * @param context 引用对象
     */
    public void clearMemoryCache(Context context) {
        //清理内存缓存  在UI主线程中进行
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停请求 正在滚动的时候
     * @param context 引用对象
     */
    public void pauseRequests(Context context) {
        Glide.with(context).pauseRequests();
    }

    /**
     * 取消请求
     *
     * @param context 引用对象
     */
    public void cancel(Context context) {
        Glide.with(context).onDestroy();
    }



    /**
     * 设置缓存大小
     *
     * @param context 引用对象
     * @param memoryCategory 缓存设置
     *                       MemoryCategory.HIGH   Glide的内存缓存和位图池最多使用它们的初始最大大小的1.5倍(1.5f)。
     *                       MemoryCategory.NORMAL  (1f)
     *                       MemoryCategory.LOW   Glide的内存缓存和位图池最多使用它们的初始最大大小的0.5倍(0.5f),
     */
    public void setMemoryCategoryS(Context context, MemoryCategory memoryCategory) {

        Glide.get(context).setMemoryCategory(memoryCategory);
    }


    /**
     * 清除图片所有缓存,包含文件夹
     */
    public void clearImageAllCache(Context context) {
        clearMemoryCache(context);
        clearDiskCache(context);
        String internalCache = context.getCacheDir() + "/"+ IConstans.DQ_GLIDE;
        String externalCache = context.getExternalCacheDir() + "/"+ IConstans.DQ_GLIDE;
        deleteFolderFile(internalCache, true);
        deleteFolderFile(externalCache, true);
    }

    /**
     * 是否有sd卡
     * @return true/false
     */
    private boolean isExternalStorage(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    /**
     * 获取Glide造成的缓存大小,磁盘中
     *
     * @return CacheSize
     */
    public String getDiskCacheSize(Context context) {
        try {
            if(isExternalStorage()) {
                return getFormatSize(getFolderSize(new File(context.getExternalCacheDir() + "/"+ IConstans.DQ_GLIDE)));
            }else {
                return getFormatSize(getFolderSize(new File(context.getCacheDir() + "/"+ IConstans.DQ_GLIDE)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 获取Glide造成的缓存大小,磁盘中
     *
     * @return CacheSize
     */
    public String getMemoryCacheSize(Context context) {
        try {
            if(isExternalStorage()) {
                return getFormatSize(getFolderSize(new File(context.getExternalCacheDir() + "/"+ IConstans.DQ_GLIDE)));
            }else {
                return getFormatSize(getFolderSize(new File(context.getCacheDir() + "/"+ IConstans.DQ_GLIDE)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception 异常
     */
    private long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath filePath
     * @param deleteThisPath deleteThisPath
     */
    private void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    private static String getFormatSize(double size) {

        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }
}
