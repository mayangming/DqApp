package com.wd.daquan.common.utils;

import android.app.Activity;
import android.graphics.Bitmap;



public class ShareUtil {
//    public static ShareAction mShareAction;

//    public static ShareAction openShare(Activity activity, String url, String Title, String mDescription, String mThumb) {
//        if (mShareAction != null) {
//            mShareAction.close();
//        }
//        CustomShareWXListener mShareListener = new CustomShareWXListener(activity);
//        UMImage image = new UMImage(activity, mThumb);//网络图片
//        UMWeb web = new UMWeb(url);
//        web.setTitle(Title);//标题
//        web.setThumb(image);  //缩略图
//        web.setDescription(mDescription);//描述
//        mShareAction = new ShareAction((Activity) activity).setDisplayList(
//                SHARE_MEDIA.WEIXIN).withMedia(web)
//                .withText("title")
//                .setCallback(mShareListener);
//        ShareBoardConfig config = new ShareBoardConfig();
//        config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_CENTER);
//        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR);
//        mShareAction.open();
//        return mShareAction;
//    }
//
//
//    public static ShareAction openWEIXINShare(Activity activity, String url, String Title, String mDescription, String mThumb) {
//        if (mShareAction != null) {
//            mShareAction.close();
//        }
//        CustomShareWXListener mShareListener = new CustomShareWXListener(activity);
//        UMImage image = new UMImage(activity, mThumb);//网络图片
//        UMWeb web = new UMWeb(url);
//        web.setTitle(Title);//标题
//        web.setThumb(image);  //缩略图
//        web.setDescription(mDescription);//描述
//        mShareAction = new ShareAction(activity)
//                .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
//                .withText("hello").withMedia(web)//分享内容
//                .setCallback(mShareListener)//回调监听器
//        ;
//        mShareAction.share();
//        return mShareAction;
//    }
//
//    /**
//     * 分享到微信
//     * @param activity
//     * @param bitmap
//     * @return
//     */
//    public static ShareAction shareWx(Activity activity, Bitmap bitmap) {
//        if (mShareAction != null) {
//            mShareAction.close();
//        }
//        CustomShareWXListener mShareListener = new CustomShareWXListener(activity);
//        UMImage image = new UMImage(activity, bitmap); //内存中图片
//
//        //分享二维码到微信设置略缩图
//        UMImage thumb =  new UMImage(activity, bitmap);
//        image.setThumb(thumb);
//        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
//        image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
//        image.compressFormat = Bitmap.CompressFormat.PNG; //压缩格式设置
//
//        mShareAction = new ShareAction(activity)
//                .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
//                .withText("hello").withMedia(image)//分享内容
//                .setCallback(mShareListener)//回调监听器
//        ;
//        mShareAction.share();
//
//        return mShareAction;
//    }
}
