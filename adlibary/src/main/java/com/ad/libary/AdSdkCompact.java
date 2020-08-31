package com.ad.libary;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.ad.libary.bean.AdCodeInfoBean;
import com.ad.libary.config.SDKAdBuild;
import com.ad.libary.config.UrlConfig;
import com.ad.libary.http.DqAdSdkHttpUtils;
import com.ad.libary.provider.AdProvider;
import com.ad.libary.type.AdType;
import com.dq.sdk.ad.http.bean.HttpBaseBean;
import com.dq.sdk.ad.http.callback.HttpBaseBeanCallBack;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.Call;

/**
 *
 */
public class AdSdkCompact {
    private static AdSdkCompact adSdkCompact;
    private AdSwitchResultIpc adSwitchResultIpc;

    static {
        adSdkCompact = new AdSdkCompact();
    }

    public static AdSdkCompact getInstance(){
        return adSdkCompact;
    }

    /**
     * 任务切换
     * @param adSwitchResultIpc
     */
    public void switchAd(final SDKAdBuild sdkAdBuild,final AdSwitchResultIpc adSwitchResultIpc){
        this.adSwitchResultIpc = adSwitchResultIpc;
        String sha1 = sHA1(AdProvider.context);
        HashMap<String,Object> params = new HashMap<>();
        params.put("appId","1");
        params.put("codeId","1");
        params.put("sha",sha1);

        DqAdSdkHttpUtils.postJson(UrlConfig.GET_AD_TERRACE, params, new HttpBaseBeanCallBack<String>() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                adSwitchResultIpc.switchFail();
            }

            @Override
            public void onResponse(HttpBaseBean<String> response, int id) {
                Log.e("YM","获取的数据内容:"+response);
                if (0 != response.result){
                    adSwitchResultIpc.switchFail();
                    return;
                }

//                val data = response?.data ?: return
//                        val reward = Gson().fromJson<RewardBean>(data,RewardBean::class.java)
                JsonElement content = response.data;
                if (null == content){
                    return;
                }
                AdCodeInfoBean adCodeInfoBean = new Gson().fromJson(content,AdCodeInfoBean.class);
                sdkAdBuild.type = AdType.getAdTypeForValue(adCodeInfoBean.getTerrace());
                sdkAdBuild.mAppId = String.valueOf(adCodeInfoBean.getAppId());
                AdSdkManager.getInstance(AdProvider.context).initSDKAd(sdkAdBuild);
                adSwitchResultIpc.switchSuccess(adCodeInfoBean,sdkAdBuild.type);
            }
        });
    }


    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 广告平台切换结果
     */
    public  interface AdSwitchResultIpc{
        void switchSuccess(AdCodeInfoBean adCodeInfoBean,AdType type);
        void switchFail();
    }
}