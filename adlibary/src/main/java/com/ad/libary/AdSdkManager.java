package com.ad.libary;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ad.libary.config.SDKAdBuild;
import com.ad.libary.type.AdType;
import com.dq.sdk.ad.DqAdSdk;
import com.dq.sdk.ad.config.DqAdConfig;

public class AdSdkManager {
    private static Context mContext;
    private SDKAdBuild sdkAdBuild;
    private static AdSdkManager adSdkManager;

    private AdSdkManager(Context context) {
        mContext = context;
    }

    public static AdSdkManager getInstance(Context context) {
        if (null == adSdkManager){
            adSdkManager = new AdSdkManager(context);
        }
        return adSdkManager;
    }

    /**
     * 初始化广告SDK
     * 需要在使用之前进行初始化
     * */
    public void initSDKAd(@NonNull SDKAdBuild sdkAdBuild){
        this.sdkAdBuild = sdkAdBuild;
        switch (sdkAdBuild.type){
            case AD_TT:
                Log.e("YM------>","初始化穿山甲----------");
                TTAdManagerHolder.init(mContext,sdkAdBuild);
                break;
            case AD_GDT:
                GDTADManagerHolder.get(mContext,sdkAdBuild.mAppId);
            case AD_DQ:
                DqAdConfig.Build dqConfigBuild = new DqAdConfig().getBuild();
                dqConfigBuild.setAppId(sdkAdBuild.mAppId);
                DqAdSdk.Companion.init(dqConfigBuild.build());
        }
    }

    /**
     * 切换SDK平台
     * @param sdkAdBuild
     */
    public void switchSDKAd(@NonNull SDKAdBuild sdkAdBuild){
        this.sdkAdBuild = sdkAdBuild;
        switch (sdkAdBuild.type){
            case AD_TT:
                TTAdManagerHolder.init(mContext,sdkAdBuild);
                break;
            case AD_GDT:
                GDTADManagerHolder.get(mContext,sdkAdBuild.mAppId);
            case AD_DQ:
                DqAdConfig.Build dqConfigBuild = new DqAdConfig().getBuild();
                DqAdSdk.Companion.init(dqConfigBuild.setAppId("1").build());
        }
    }

    public AdType getAdType(){
        return sdkAdBuild.type;
    }
}