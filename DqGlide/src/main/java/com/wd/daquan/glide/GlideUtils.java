package com.wd.daquan.glide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.wd.daquan.glide.constans.IConstans;
import com.wd.daquan.glide.helper.CustomTransformation;
import com.wd.daquan.glide.listener.ILoadBitmapListener;
import com.wd.daquan.glide.listener.ILoadFileListener;
import com.wd.daquan.glide.progress.GlideBuild;
import com.wd.daquan.glide.progress.OnProgressListener;

import java.io.File;
import java.util.concurrent.Executors;

/**
 * Created by 方志 on 2018/4/19.
 * glide4.7封装工具类
 *
 * DiskCacheStrategy.ALL 既缓存原始图片，也缓存转换过后的图片。
 * DiskCacheStrategy.NONE 不缓存任何内容
 * DiskCacheStrategy.DATA 在资源解码前就将原始数据写入磁盘缓存（即只缓存原始图片）
 * DiskCacheStrategy.RESOURCE 在资源解码后将数据写入磁盘缓存，即经过缩放等转换后的图片资源（即只缓存转换过后的图片）。
 * DiskCacheStrategy.AUTOMATIC 让Glide根据图片资源智能地选择使用哪一种缓存策略。
 * 默认采用DiskCacheStrategy.AUTOMATIC策略

 * ------------------------------------------- 动画-----------------------------------------------
 * 使用自定义的动画，可以使用GenericTransitionOptions.with(int viewAnimationId)
 * 或者BitmapTransitionOptions.withCrossFade(int animationId, int duration)
 * 或者DrawableTransitionOptions.withCrossFade(int animationId, int duration)
 * GenericTransitionOptions.with(viewAnimation)
 * 
 * git动态静态加载
 */
public class GlideUtils {


    /**
     * Glide 加入请求回调监听
     *  BitmapDrawable bd = (BitmapDrawable) drawable;
     *  Bitmap bitmap = bd.getBitmap();
     *
     * @param context 引用对象
     * @param path 可传入手机图片路径、文件，工程资源文件，网络url，视频文件
     */
    public static void loadBitmapListener(Context context, Object path, final ILoadBitmapListener listener) {
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.loading_small)
                .error(R.drawable.loading_error);
        Glide.with(context).asBitmap().load(path).apply(options).into(new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                if(null != listener) {
                    listener.onResourceReady(bitmap);
                }
            }
        });
    }

    /**
     * 设置加载中以及加载失败图片
     *
     * @param context 引用对象
     * @param path 可传入手机图片路径、文件，工程资源文件，网络url，视频文件
     * @param imageView 视图
     */
    public static GlideBuild getGlide(@NonNull Context context, @NonNull Object path, @NonNull ImageView imageView){
        return new GlideBuild().with(context).load(path).into(imageView);
    }


    public static void load(Context context, Object path, ImageView imageView) {
        getGlide(context, path, imageView).placeholder(R.drawable.loading_small).error(R.drawable.loading_error).build();
    }

    public static void loadThumbnail(Context context, Object path, ImageView imageView) {
        getGlide(context, path, imageView).placeholder(R.drawable.loading_small).error(R.drawable.loading_error)
                .thumbnail(0.1f).build();
    }

    public static int dp2px(int radius){
        if(radius <= 0) {
            return 1;
        }
        return (int) (Resources.getSystem().getDisplayMetrics().density * radius);
    }


    /**
     * 自动填充
     */
    public static void loadCenterCrop(Context context, Object path, ImageView imageView) {
        getGlide(context, path, imageView).placeholder(R.drawable.loading_small)
                .error(R.drawable.loading_error).scaleType(ImageView.ScaleType.CENTER_CROP).build();
    }

    /**
     * 设置加载中以及加载失败图片并且指定大小, 按宽高压缩
     *
     */
    public static void load(Context context, Object path, ImageView imageView, int loadingImage, int errorImage,
                            int width, int height) {
        getGlide(context, path, imageView).placeholder(loadingImage).error(errorImage).override(width, height).build();
    }

    /**
     * 加载自定义占位图错误图
     */
    public static void load(Context context, Object path, ImageView imageView, int loadingImage, int errorImage) {
        getGlide(context, path, imageView).placeholder(loadingImage).error(errorImage).build();
    }



/**
* --------------------------------------------- gif图片加载 -------------------------------------------------------------------------------------------
*/

    /**
     * 设置动态GIF加载方式
     *
     */
    public static void loadGif(Context context, Object path, ImageView imageView) {
        getGlide(context, path, imageView).placeholder(R.drawable.loading_small).error(R.drawable.loading_error).build();
    }

    public static void loadGif(Context context, Object path, ImageView imageView, int radius) {
        getGlide(context, path, imageView).placeholder(R.drawable.loading_small)
                .error(R.drawable.loading_error).transform(new RoundedCorners(dp2px(radius))).build();
    }

    /**
     * 加载指定大小的gif
     */
    public static void loadGif(Context context, Object path, ImageView imageView, int loadingImage, int errorImage, int width, int height) {
        getGlide(context, path, imageView).placeholder(loadingImage).error(errorImage).override(width, height).build();
    }

    /**
     * 显示圆形图片
     *
     */
    public static void loadCircle(Context context, Object path, ImageView imageView) {
        getGlide(context, path, imageView).placeholder(R.drawable.loading_small).error(R.drawable.loading_error).circle(true).build();
    }

    /**
     * 加载圆角图片 默认4dp
     *
     */
    public static void loadRound(Context context, Object path, ImageView imageView) {
        loadRound(context, path, imageView, IConstans.RADIUS);
    }

    /**
     * 加载圆角图片
     *
     */
    public static void loadRound(Context context, Object path, ImageView imageView, int radius) {
        loadRound(context, path, imageView, radius, R.drawable.loading_small, R.drawable.loading_error);
    }

    /**
     * 加载圆角图片
     *
     */
    public static void loadRound(Context context, Object path, ImageView imageView, int radius, int loadingImage, int errorImage) {
        getGlide(context, path, imageView).placeholder(loadingImage).error(errorImage)
                .transform(new RoundedCorners(dp2px(radius))).build();
    }

    /**
     * 加载联系人列表头像,圆角图片
     *
     */
    public static void loadHeader(Context context, Object path, ImageView imageView) {
        loadRound(context, path, imageView, IConstans.RADIUS, R.drawable.user_avatar, R.drawable.user_avatar);
    }

    /**
     * 加载联系人列表头像默认圆角图片
     *
     */
    public static void loadHeader(Context context, Object path, ImageView imageView, int loadingImage) {
        loadRound(context, path, imageView, IConstans.RADIUS_10, loadingImage, loadingImage);
    }

    /**
     * 加载联系人列表头像,圆形
     *
     */
    public static void loadCircle(Context context, Object path, ImageView imageView, int loadingImage) {
        getGlide(context, path, imageView).placeholder(loadingImage).error(loadingImage)
                .circle(true).build();
    }

    /**
     *
     * 加载圆角图片，按宽高压缩
     *
     */
    public static void loadRound(Context context, Object path, ImageView imageView, int loadingImage,
                                 int errorImage, int width, int height, int radius) {
        getGlide(context, path, imageView).placeholder(loadingImage).error(errorImage).override(width, height)
                .transform(new RoundedCorners(dp2px(radius))).build();
    }

    /**
     *  获取一个图片文件, 必须运行在子线程
     */
    @SuppressLint("CheckResult")
    public static void loadFile(final Context context, final Object path, final ILoadFileListener listener){
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = Glide.with(context)
                            .download(path)
                            .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                    if(listener != null && file != null) {
                        listener.loadFile(file);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void loadRotate(Context context, Object path, ImageView imageView, int rotate, int radius) {
        getGlide(context, path, imageView).error(R.drawable.user_avatar).transform(new CustomTransformation(rotate, radius)).build();
    }

    public static void loadFitCenter(Context context, Object path, ImageView imageView, int width, int height) {
        getGlide(context, path, imageView).placeholder(R.drawable.loading_small)
                .error(R.drawable.loading_error).scaleType(ImageView.ScaleType.FIT_CENTER).override(width, height).build();
    }

    /**
     * 加载进度条
     */
    public static void loadProgress(Context context, Object path, ImageView imageView, final OnProgressListener listener){
        getGlide(context, path, imageView).placeholder(R.drawable.loading_small)
                .error(R.drawable.loading_error).listener(path, listener).transform(new RoundedCorners(1)).build();
    }

    /**
     * 加载进度条
     */
    public static void loadProgress(Context context, Object path, ImageView imageView, int rotation, final OnProgressListener listener){
        getGlide(context, path, imageView).placeholder(R.drawable.loading_small)
                .error(R.drawable.loading_error).listener(path, listener).transform(new CustomTransformation(rotation, 0)).build();
    }
}

