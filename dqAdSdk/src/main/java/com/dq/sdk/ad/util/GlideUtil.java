package com.dq.sdk.ad.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dq.sdk.ad.R;

import java.io.File;

public class GlideUtil {
    /** 加载网络图片 */
    public static void loadNormalImgByNet(Context context, String netUrl, ImageView imageView){
        Glide.with(context).load(netUrl).apply(normalOption()).into(imageView);
    }
    /** 加载网络圆角图片 */
    public static void loadCircleImgByNet(Context context,@NonNull String netUrl, ImageView imageView){
        Glide.with(context).load(netUrl).apply(circleOption()).into(imageView);
    }
    /** 加载本地图片 */
    public static void loadCircleImgByLocal(Context context, String localUrl, ImageView imageView){
        Glide.with(context).load(Uri.fromFile(new File(localUrl))).apply(circleOption()).into(imageView);
    }

    /** 加载资源图片 */
    public static void loadCircleImgByResource(Context context, @RawRes @DrawableRes @Nullable int resourceId, ImageView imageView){
        Glide.with(context).load(resourceId).apply(normalOption()).into(imageView);
    }
    /** 加载本地圆角图片
     *  圆角加载有问题，CustomViewTarget可参考
     * */
    public static void loadCornersImgByLocal(Context context, @RawRes @DrawableRes @Nullable int localRes, View view, int roundingRadius){
        Glide.with(context).load(localRes).apply(cornersOption(roundingRadius)).into(new CustomViewTarget<View,Drawable>(view){
            @Override
            protected void onResourceCleared(@Nullable Drawable placeholder) {

            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {

            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                view.setBackground(resource);
            }
        });
    }

    /** 圆形头像配置 */
    private static RequestOptions circleOption(){
        return RequestOptions.bitmapTransform(new CircleCrop())
                .placeholder(R.mipmap.ic_launcher)//占位图
                .error(R.mipmap.ic_launcher)//错误图
                .diskCacheStrategy(DiskCacheStrategy.NONE); //不缓存到SD卡
    }
    /** 圆角图片配置 */
    private static RequestOptions cornersOption(int roundingRadius){
        return RequestOptions.bitmapTransform(new RoundedCorners(roundingRadius))
                .placeholder(R.mipmap.ic_launcher)//占位图
                .error(R.mipmap.ic_launcher)//错误图
                .diskCacheStrategy(DiskCacheStrategy.NONE); //不缓存到SD卡
    }
    /** 普通图片配置 */
    private static RequestOptions normalOption(){
        return RequestOptions
                .placeholderOf(R.mipmap.ic_launcher)//占位图
                .error(R.mipmap.ic_launcher)//错误图
                .diskCacheStrategy(DiskCacheStrategy.NONE); //不缓存到SD卡
    }

}