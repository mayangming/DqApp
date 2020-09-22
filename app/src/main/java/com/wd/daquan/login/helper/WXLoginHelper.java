package com.wd.daquan.login.helper;

import android.app.Activity;
import android.text.TextUtils;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wd.daquan.BuildConfig;
import com.wd.daquan.login.listener.WXLoginListener;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.util.CallBackUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author: 方志
 * @Time: 2018/9/12 10:03
 * @Description:
 */
public class WXLoginHelper {

    private static IWXAPI api;
    //    public static void umWxLogin(Activity context, WXLoginListener loginListener) {
//        UMShareConfig umShareConfig = new UMShareConfig();
//        umShareConfig.isNeedAuthOnGetUserInfo(true);//设置需要每次登录都弹出授权页面
//        UMShareAPI umShareAPI = UMShareAPI.get(context);
//        umShareAPI.setShareConfig(umShareConfig);
//
//        if (!umShareAPI.isInstall(context, SHARE_MEDIA.WEIXIN)) {
//            DqToast.showShort(context.getString(R.string.you_not_installed_wechat_client));
//            return;
//        }
//
//        umShareAPI.getPlatformInfo(context, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
//            @Override
//            public void onStart(SHARE_MEDIA share_media) {
//                Log.e("weixin auth", "onStart");
//            }
//
//            @Override
//            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//                if(null != loginListener) {
//                    loginListener.loginWX(map);
//                }
//            }
//
//            @Override
//            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
//                throwable.printStackTrace();
//            }
//
//            @Override
//            public void onCancel(SHARE_MEDIA share_media, int i) {
//
//            }
//        });
//    }
    public static void umWxLogin(Activity context, WXLoginListener loginListener) {
        CallBackUtils.getInstance().setListener(MsgType.WX_LOGIN_RESULT, new CallBackUtils.MessageCallBack() {
            @Override
            public void callBack(String value) {
                DqLog.e("YM--------->接收的获取微信AccessToken的值:",value.toString());
                Map<String,String> map = new HashMap<>();
                try {
                    JSONObject jsonObject = new JSONObject(value);
                    Iterator<String> iterable = jsonObject.keys();
                    while (iterable.hasNext()){
                        String jsonKey = iterable.next();
                        String jsonValue = jsonObject.optString(jsonKey,"");
                        map.put(jsonKey,jsonValue);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(null != loginListener) {
                    loginListener.loginWX(map);
                }
            }
        });
        api = WXAPIFactory.createWXAPI(context, BuildConfig.WX_PAY_APPID, false);
        api.registerApp(BuildConfig.WX_PAY_APPID);
        if (!api.isWXAppInstalled()){
            DqToast.showShort("您还未安装微信客户端！");
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "dqApp";
        api.sendReq(req);
    }

}