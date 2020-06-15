package com.wd.daquan.third.helper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.wd.daquan.R;
import com.meetqs.qingchat.imagepicker.PictureSelector;
import com.meetqs.qingchat.imagepicker.config.PictureConfig;
import com.meetqs.qingchat.imagepicker.config.PictureMimeType;
import com.meetqs.qingchat.imagepicker.entity.LocalMedia;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * @author: dukangkang
 * @date: 2019/4/2 09:20.
 * @description: todo ...
 */
public class SelectPictureHelper {
    /**
     * 选择图片
     *
     * @param activity
     */
    public static void selectedImage(Activity activity) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(true) // 是否可播放音频
                .isCamera(false)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .synOrAsy(false)//同步true或异步false 压缩 默认同步
                .compress(true)// 是否压缩 true or false
                .setBtnDesc("完成")
//                .sizeMultiplier(0.4f)
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .isGif(true)// 是否显示gif图片
                .openClickSound(false)// 是否开启点击声音
                .minimumCompressSize(1024)// 小于1M的图片不压缩
                //.selectionMedia(mSelectList)// 是否传入已选图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                //.recordVideoSecond()//录制视频秒数 默认60s
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    /**
     * 获取本地相册选择地址
     * @param data
     * @return
     */
    public static Uri getImageUri(Intent data) {
        List<LocalMedia> fileList = PictureSelector.obtainMultipleResult(data);
        if (fileList.size() <= 0) {
            return null;
        }

        boolean isOriginal = data.getBooleanExtra(PictureConfig.IS_ORIGINAL, false);
        LocalMedia localMedia = fileList.get(0);
        String compressPath = localMedia.getCompressPath();
        if (TextUtils.isEmpty(compressPath)) {
            compressPath = localMedia.getPath();
        }

        File thumFile = new File(compressPath);
        File localFile = new File(localMedia.getPath());
        Uri thumUri = Uri.fromFile(thumFile);
        Uri localUri = Uri.fromFile(localFile);

        try {
            //解决路径bug，编码统一
            String thumUrl = URLDecoder.decode(String.valueOf(thumUri), "utf-8");
            String localUrl = URLDecoder.decode(String.valueOf(localUri), "utf-8");

            thumUri = Uri.parse(thumUrl);
            localUri = Uri.parse(localUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (isOriginal) {
            return localUri;
        } else {
            return thumUri;
        }
    }
}
