package com.wd.daquan.login.helper;

import android.app.Activity;
import android.util.Log;

import com.wd.daquan.R;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.login.listener.WXLoginListener;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * @Author: 方志
 * @Time: 2018/9/12 10:03
 * @Description:
 */
public class WXLoginHelper {

    public static void umWxLogin(Activity context, WXLoginListener loginListener) {
        UMShareConfig umShareConfig = new UMShareConfig();
        umShareConfig.isNeedAuthOnGetUserInfo(true);//设置需要每次登录都弹出授权页面
        UMShareAPI umShareAPI = UMShareAPI.get(context);
        umShareAPI.setShareConfig(umShareConfig);

        if (!umShareAPI.isInstall(context, SHARE_MEDIA.WEIXIN)) {
            DqToast.showShort(context.getString(R.string.you_not_installed_wechat_client));
            return;
        }

        umShareAPI.getPlatformInfo(context, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.e("weixin auth", "onStart");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                if(null != loginListener) {
                    loginListener.loginWX(map);
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        });
    }
}
