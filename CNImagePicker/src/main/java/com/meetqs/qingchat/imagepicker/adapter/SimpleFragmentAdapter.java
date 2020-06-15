package com.meetqs.qingchat.imagepicker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meetqs.qingchat.imagepicker.PictureVideoPlayActivity;
import com.meetqs.qingchat.imagepicker.R;
import com.meetqs.qingchat.imagepicker.config.PictureConfig;
import com.meetqs.qingchat.imagepicker.config.PictureMimeType;
import com.meetqs.qingchat.imagepicker.entity.LocalMedia;
import com.meetqs.qingchat.imagepicker.photoview.OnViewTapListener;
import com.meetqs.qingchat.imagepicker.photoview.PhotoView;
import com.meetqs.qingchat.imagepicker.widget.longimage.ImageSource;
import com.meetqs.qingchat.imagepicker.widget.longimage.ImageViewState;
import com.meetqs.qingchat.imagepicker.widget.longimage.SubsamplingScaleImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

/**
 * @author：luck
 * @data：2018/1/27 下午7:50
 * @描述:图片预览
 */

public class SimpleFragmentAdapter extends PagerAdapter {
    private List<LocalMedia> images;
    private Context mContext;
    private OnCallBackActivity onBackPressed;

    public interface OnCallBackActivity {
        /**
         * 关闭预览Activity
         */
        void onActivityBackPressed();
    }

    public SimpleFragmentAdapter(List<LocalMedia> images, Context context,
                                 OnCallBackActivity onBackPressed) {
        super();
        this.images = images;
        this.mContext = context;
        this.onBackPressed = onBackPressed;
    }

    @Override
    public int getCount() {
        if (images != null) {
            return images.size();
        }
        return 0;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        (container).removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View contentView = LayoutInflater.from(container.getContext())
                .inflate(R.layout.picture_image_preview, container, false);
        // 常规图控件
        final PhotoView imageView = (PhotoView) contentView.findViewById(R.id.preview_image);
        // 长图控件
        final SubsamplingScaleImageView longImg = (SubsamplingScaleImageView) contentView.findViewById(R.id.longImg);
        TextView gifSizeTv = (TextView) contentView.findViewById(R.id.gif_size_tv);

        ImageView iv_play = (ImageView) contentView.findViewById(R.id.iv_play);
        LocalMedia media = images.get(position);
        if (media != null) {
            final String pictureType = media.getPictureType();
            boolean eqVideo = pictureType.startsWith(PictureConfig.VIDEO);
            iv_play.setVisibility(eqVideo ? View.VISIBLE : View.GONE);
            final String path;
            if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                path = media.getCutPath();
            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                path = media.getCompressPath();
            } else {
                path = media.getPath();
            }
            boolean isGif = PictureMimeType.isGif(pictureType);
            final boolean eqLongImg = PictureMimeType.isLongImg(media);
            imageView.setVisibility(eqLongImg && !isGif ? View.GONE : View.VISIBLE);
            longImg.setVisibility(eqLongImg && !isGif ? View.VISIBLE : View.GONE);
            gifSizeTv.setVisibility(isGif ? View.VISIBLE : View.GONE);
            // 压缩过的gif就不是gif了
            if (isGif && !media.isCompressed()) {
                RequestOptions gifOptions = new RequestOptions()
                        .override(480, 800)
                        .priority(Priority.HIGH)
                        .diskCacheStrategy(DiskCacheStrategy.NONE);
                Glide.with(contentView.getContext())
                        .asGif()
                        .load(path)
                        .apply(gifOptions)
                        .into(imageView);

                String fileSize = getTotalSelectedSize(media.getFileSize());
                gifSizeTv.setText(mContext.getString(R.string.gif_size) + fileSize);

            } else {
                RequestOptions options = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(contentView.getContext())
                        .asBitmap()
                        .load(path)
                        .apply(options)
                        .into(new SimpleTarget<Bitmap>(480, 800) {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                if (eqLongImg) {
                                    displayLongPic(resource, longImg);
                                } else {
                                    imageView.setImageBitmap(resource);
                                }
                            }
                        });
            }
            imageView.setOnViewTapListener(new OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    if (onBackPressed != null) {
                        onBackPressed.onActivityBackPressed();
                    }
                }
            });
            longImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBackPressed != null) {
                        onBackPressed.onActivityBackPressed();
                    }
                }
            });
            iv_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("video_path", path);
                    intent.putExtras(bundle);
                    intent.setClass(mContext, PictureVideoPlayActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
        container.addView(contentView, 0);
        return contentView;
    }

    private String getTotalSelectedSize(float size) {

        String totalSize;
        float temp = 1024.0F * 1024.0F;
        if(size < temp ) {
            totalSize = String.format("%.0fK", new Object[]{Float.valueOf(size / 1024.0F)});
        } else {
            totalSize = String.format("%.1fM", new Object[]{Float.valueOf(size / temp)});
        }

        return totalSize;
    }


    /**
     * 加载长图
     *
     * @param bmp
     * @param longImg
     */
    private void displayLongPic(Bitmap bmp, SubsamplingScaleImageView longImg) {
        longImg.setQuickScaleEnabled(true);
        longImg.setZoomEnabled(true);
        longImg.setPanEnabled(true);
        longImg.setDoubleTapZoomDuration(100);
        longImg.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
        longImg.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
        longImg.setImage(ImageSource.cachedBitmap(bmp), new ImageViewState(0, new PointF(0, 0), 0));
    }
}
