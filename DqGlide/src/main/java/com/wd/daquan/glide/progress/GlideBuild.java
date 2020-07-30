package com.wd.daquan.glide.progress;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class GlideBuild {

    /**
     * 占位图ID
     */
    private int mPlaceholderId = -1;
    /**
     * 错误图ID
     */
    private int mErrorId = -1;

    private float mThumbnail = 0.0f;
    /**
     *
     */
    private Transformation<Bitmap>[] mTransformation = null;
    /**
     * 图片请求地址
     */
    private Object mPath = null;
    /**
     * 存储类型
     */
    private DiskCacheStrategy mDiskCacheStrategy = null;

    private Context mContext;
    private ImageView mImageView;
    /**
     * 压缩图片宽高
     */
    private int mWidth;
    private int mHeight;

    /**
     * 图片拉伸样式
     */
    private ImageView.ScaleType mScaleType;
    /**
     * 圆形图
     */
    private boolean isCircle;
    private Target mTarget;

    public GlideBuild() {

    }

    public GlideBuild with(Context context) {
        this.mContext = context;
        return this;
    }

    public GlideBuild load(Object path) {
        this.mPath = path;
        return this;
    }

    public GlideBuild scaleType(ImageView.ScaleType scaleType) {
        this.mScaleType = scaleType;
        return this;
    }

    public GlideBuild override(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        return this;
    }

    public GlideBuild circle(boolean isCircle) {
        this.isCircle = isCircle;
        return this;
    }

    public GlideBuild placeholder(int id) {
        this.mPlaceholderId = id;
        return this;
    }

    public GlideBuild error(int id) {
        this.mErrorId = id;
        return this;
    }

    public GlideBuild thumbnail(float thumbnail) {
        this.mThumbnail = thumbnail;
        return this;
    }

    public GlideBuild diskCacheStrategy(DiskCacheStrategy diskCacheStrategy) {
        this.mDiskCacheStrategy = diskCacheStrategy;
        return this;
    }

    public GlideBuild into(ImageView imageView) {
        this.mImageView = imageView;
        return this;
    }

    public GlideBuild into(Target target) {
        this.mTarget = target;
        return this;
    }

    public GlideBuild listener(Object path, OnProgressListener listener) {
        ProgressManager.addListener(path.toString(), listener);
        return this;
    }

    @SafeVarargs
    public final GlideBuild transform(@NonNull Transformation<Bitmap>... transformation) {
        this.mTransformation = transformation;
        return this;
    }

    @SuppressLint("CheckResult")
    public void build() {

        RequestOptions requestOptions = new RequestOptions();
        if (mDiskCacheStrategy != null) {
            requestOptions.diskCacheStrategy(mDiskCacheStrategy);
        } else {
            requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        }
        if (mPlaceholderId != -1) {
            requestOptions.placeholder(mPlaceholderId);
        }
        if (mErrorId != -1) {
            requestOptions.error(mErrorId);
        }

        if (mWidth > 0 && mHeight > 0) {
            requestOptions.override(mWidth, mHeight);
        }

        if(mScaleType != null) {
            if(ImageView.ScaleType.FIT_CENTER == mScaleType) {
                requestOptions.fitCenter();
            }else if(ImageView.ScaleType.CENTER_CROP == mScaleType){
                requestOptions.centerCrop();
            }
        }

        if(isCircle) {
            requestOptions.circleCrop();
        }

        if (mTransformation != null) {
            requestOptions.transforms(mTransformation);
        }

        if (mPath == null) {
            Glide.with(mContext).load("").apply(requestOptions).listener(mRequestListener).into(mImageView);
            return;
        }

        if (mThumbnail != 0.0) {
            Glide.with(mContext).load(mPath).apply(requestOptions).listener(mRequestListener).thumbnail(mThumbnail).into(mImageView);
        } else {
            Glide.with(mContext).load(mPath).apply(requestOptions).listener(mRequestListener).into(mImageView);
        }
    }

    private RequestListener<Drawable> mRequestListener = new RequestListener<Drawable>() {


        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object o, Target target, boolean b) {
            if (mPath != null) {
                OnProgressListener onProgressListener = ProgressManager.getProgressListener(mPath.toString());
                if (onProgressListener != null) {
                    onProgressListener.onProgress(true, 100, 0, 0);
                    ProgressManager.removeListener(mPath.toString());
                }
            }
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            if (mPath != null) {
                OnProgressListener progressListener = ProgressManager.getProgressListener(mPath.toString());
                if (progressListener != null) {
                    progressListener.onProgress(true, 100, 0, 0);
                    ProgressManager.removeListener(mPath.toString());
                }
            }
            return false;
        }
    };
}
