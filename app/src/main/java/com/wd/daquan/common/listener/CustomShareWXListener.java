package com.wd.daquan.common.listener;

import android.app.Activity;

import com.wd.daquan.model.log.DqToast;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.lang.ref.WeakReference;

public class CustomShareWXListener implements UMShareListener {
    public WeakReference<Activity> mActivity;

    public CustomShareWXListener(Activity activity) {
        mActivity = new WeakReference(activity);
    }

    @Override
    public void onStart(SHARE_MEDIA platform) {

    }

    @Override
    public void onResult(SHARE_MEDIA platform) {

        if (platform.name().equals("WEIXIN_FAVORITE")) {
//            CNToastUtil.makeText(mActivity.get(),platform + "收藏成功啦");
        } else {
//            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
//                    && platform != SHARE_MEDIA.EMAIL
//                    && platform != SHARE_MEDIA.FLICKR
//                    && platform != SHARE_MEDIA.FOURSQUARE
//                    && platform != SHARE_MEDIA.TUMBLR
//                    && platform != SHARE_MEDIA.POCKET
//                    && platform != SHARE_MEDIA.PINTEREST
//
//                    && platform != SHARE_MEDIA.INSTAGRAM
//                    && platform != SHARE_MEDIA.GOOGLEPLUS
//                    && platform != SHARE_MEDIA.YNOTE
//                    && platform != SHARE_MEDIA.EVERNOTE) {

            if (platform == SHARE_MEDIA.WEIXIN) {
                DqToast.showShort("微信分享成功");
            } else if (platform == SHARE_MEDIA.WEIXIN_CIRCLE) {
                DqToast.showShort("朋友圈分享成功");
            } else if (platform == SHARE_MEDIA.QQ) {
                DqToast.showShort("QQ分享成功");
            } else if (platform == SHARE_MEDIA.QZONE) {
                DqToast.showShort("QQ空间分享成功");
            }

//            }

        }
    }

    @Override
    public void onError(SHARE_MEDIA platform, Throwable t) {
//        if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
//                && platform != SHARE_MEDIA.EMAIL
//                && platform != SHARE_MEDIA.FLICKR
//                && platform != SHARE_MEDIA.FOURSQUARE
//                && platform != SHARE_MEDIA.TUMBLR
//                && platform != SHARE_MEDIA.POCKET
//                && platform != SHARE_MEDIA.PINTEREST
//
//                && platform != SHARE_MEDIA.INSTAGRAM
//                && platform != SHARE_MEDIA.GOOGLEPLUS
//                && platform != SHARE_MEDIA.YNOTE
//                && platform != SHARE_MEDIA.EVERNOTE) {
//            QcToastUtil.makeText(mActivity.get(),platform + "分享失败啦");
//            if (t != null) {
//                Log.d("throw", "throw:" + t.getMessage());
//            }
//        }
        if (platform == SHARE_MEDIA.WEIXIN) {
            DqToast.showShort("微信分享失败啦");
        } else if (platform == SHARE_MEDIA.WEIXIN_CIRCLE) {
            DqToast.showShort("朋友圈分享失败啦");
        } else if (platform == SHARE_MEDIA.QQ) {
            DqToast.showShort("QQ分享分享失败啦");
        } else if (platform == SHARE_MEDIA.QZONE) {
            DqToast.showShort("QQ空间分享失败啦");
        }

    }

    @Override
    public void onCancel(SHARE_MEDIA platform) {
//        QcToastUtil.makeText(mActivity.get(),platform + "分享取消了");
        if (platform == SHARE_MEDIA.WEIXIN) {
            DqToast.showShort("微信分享取消了");
        } else if (platform == SHARE_MEDIA.WEIXIN_CIRCLE) {
            DqToast.showShort("朋友圈分享取消了");
        } else if (platform == SHARE_MEDIA.QQ) {
            DqToast.showShort("QQ分享取消了");
        } else if (platform == SHARE_MEDIA.QZONE) {
            DqToast.showShort("QQ分享取消了");
        }
    }

}
