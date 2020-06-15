package com.wd.daquan.glide;


import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.wd.daquan.glide.constans.IConstans;
import com.wd.daquan.glide.progress.ProgressManager;

import java.io.InputStream;


/**
 * Created by 方志 on 2018/4/19.
 * glide初始化，配置缓存路径，大小, 使用okhttp请求数据，监听进度
 */
@GlideModule
public class CustomGlideModule extends AppGlideModule {

    /**
     * 初始化glide配置
     * @param context 引用对象
     * @param builder builder
     */
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        int diskCacheSizeBytes = 1024 * 1024 * 50; // 50 MB
        //设置缓存路径内存中
        builder.setMemoryCache(new LruResourceCache(diskCacheSizeBytes));

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //sd卡中
            builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context, IConstans.DQ_GLIDE, diskCacheSizeBytes * 2));
            //Log.e("TAG", "CustomGlideModule 外部存储：");
        }else {
            //app内部
            builder.setDiskCache(new InternalCacheDiskCacheFactory(context, IConstans.DQ_GLIDE, diskCacheSizeBytes * 2));
            //Log.e("TAG", "CustomGlideModule 内存存储：");
        }


//         默认内存和图片池大小
//        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
//        int defaultMemoryCacheSize = calculator.getMemoryCacheSize(); // 默认内存大小
//        int defaultBitmapPoolSize = calculator.getBitmapPoolSize(); // 默认图片池大小
//        builder.setMemoryCache(new LruResourceCache(defaultMemoryCacheSize)); // 该两句无需设置，是默认的
//        builder.setBitmapPool(new LruBitmapPool(defaultBitmapPoolSize));
//
//        // 自定义内存和图片池大小 为 默认1.2倍
//        long memorySize = (long) (defaultMemoryCacheSize * 1.2);
//        long poolSize = (long) (defaultBitmapPoolSize * 1.2);
//        builder.setMemoryCache(new LruResourceCache(memorySize));
//        builder.setBitmapPool(new LruBitmapPool(poolSize));

    }


    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(ProgressManager.getOkHttpClient()));
    }
//
//    //完全禁用清单解析
//    @Override
//    public boolean isManifestParsingEnabled() {
//        return false;
//    }

}
