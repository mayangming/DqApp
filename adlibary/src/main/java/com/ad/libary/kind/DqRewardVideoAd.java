package com.ad.libary.kind;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.ad.libary.simple_iml.RewardVideoListenerIpc;
import com.dq.sdk.ad.ad.DqRewardAd;

/**
 * 斗圈激励视频广告集成
 */
public class DqRewardVideoAd extends AdRewardVideoIpc {
    private DqRewardAd dqRewardAd;
    public DqRewardVideoAd(Context context) {
        super(context);
        if(context instanceof FragmentActivity){
            FragmentActivity activity = (FragmentActivity)context;
            dqRewardAd = new DqRewardAd(activity);
        }

    }

    @Override
    public void loadAd(String codeId, int orientation, RewardVideoListenerIpc rewardVideoListenerIpc) {
        dqRewardAd.loadAd(codeId);
        dqRewardAd.setOnRewardAdListener(rewardVideoListenerIpc.getDqRewardAdListener());
    }

    @Override
    public void showAd(Activity activity) {
        dqRewardAd.showAd();
    }

    @Override
    public void destroy() {

    }

}