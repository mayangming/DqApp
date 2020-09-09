package com.ad.libary.simple_iml;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.dq.sdk.ad.listener.DqAppDownloadListener;
import com.dq.sdk.ad.listener.RewardAdListener;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.comm.util.AdError;

import org.jetbrains.annotations.NotNull;

/**
 * 自定义激励视频监听
 */
public abstract class RewardVideoListenerIpc {
    private TTAdNative.RewardVideoAdListener rewardVideoAdListener;
    private RewardVideoADListener rewardVideoADListener;
    private TTRewardVideoAd mttRewardVideoAd;
    private RewardAdListener rewardAdListener;
    public abstract void rewardVideoOnError(int i, String s);
    public abstract void rewardVideoOnRewardVideoAdLoad();
    public abstract void rewardVideoOnRewardVideoAdLoad(View view);
    public abstract void rewardVideoOnRewardVideoCached();
    public abstract void rewardVideoComplete();//视频播放结束
    public abstract void rewardAdClose();//广告关闭
    public RewardVideoListenerIpc(){
        rewardVideoAdListener = new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int i, String s) {
                Log.e("YM","穿山甲加载错误code:"+i);
                Log.e("YM","穿山甲加载错误message:"+s);
                rewardVideoOnError(i,s);
            }

            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ttRewardVideoAd) {
                mttRewardVideoAd = ttRewardVideoAd;
                rewardVideoOnRewardVideoAdLoad();
                bindRewardVideoListener();

            }

            @Override
            public void onRewardVideoCached() {
                rewardVideoOnRewardVideoCached();
            }
        };

        rewardVideoADListener = new RewardVideoADListener(){
            @Override
            public void onADLoad() {
                Log.e("YM","onADLoad");
                rewardVideoOnRewardVideoAdLoad();
            }

            @Override
            public void onVideoCached() {
                Log.e("YM","onVideoCached");
            }

            @Override
            public void onADShow() {
                Log.e("YM","onADShow");
            }

            @Override
            public void onADExpose() {
                Log.e("YM","onADExpose");
            }

            @Override
            public void onReward() {
                Log.e("YM","onReward");
            }

            @Override
            public void onADClick() {
                Log.e("YM","onADClick");
            }

            @Override
            public void onVideoComplete() {
                Log.e("YM","onVideoComplete");
            }

            @Override
            public void onADClose() {
                Log.e("YM","onADClose");
                rewardAdClose();
            }

            @Override
            public void onError(AdError adError) {
                Log.e("YM","onError:"+ adError.getErrorMsg());
            }
        };
        rewardAdListener = new RewardAdListener() {
            @Override
            public void loadAdSuccess(@NotNull View view) {
                rewardVideoOnRewardVideoAdLoad(view);
            }

            @Override
            public void ide() {

            }

            @Override
            public void onComplete() {
                rewardVideoComplete();
            }

            @Override
            public void onError() {

            }

            @Override
            public void adClose() {
                rewardAdClose();
            }

            @Override
            public void downloadListener(@NotNull DqAppDownloadListener downLoadListener) {

            }
        };
    }

    public TTAdNative.RewardVideoAdListener getRewardVideoAdListener() {
        return rewardVideoAdListener;
    }

    public RewardVideoADListener getRewardVideoADListener() {
        return rewardVideoADListener;
    }

    public RewardAdListener getDqRewardAdListener(){
        return rewardAdListener;
    }

    /**
     * 显示视频
     * @param activity
     */
    public void showRewardAd(Activity activity){
        mttRewardVideoAd.showRewardVideoAd(activity, TTAdConstant.RitScenes.CUSTOMIZE_SCENES,"scenes_test");
    }

    private boolean mHasShowDownloadActive = false;
    private void bindRewardVideoListener(){
        mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

            @Override
            public void onAdShow() {
                Log.e("YM","rewardVideoAd show");
            }

            @Override
            public void onAdVideoBarClick() {
                Log.e("YM","rewardVideoAd bar click");
            }

            @Override
            public void onAdClose() {
                Log.e("YM","rewardVideoAd close");
                rewardAdClose();
            }

            //视频播放完成回调
            @Override
            public void onVideoComplete() {
                Log.e("YM","rewardVideoAd complete");
                rewardVideoComplete();
            }

            @Override
            public void onVideoError() {
                Log.e("YM","rewardVideoAd error");
            }

            //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
            @Override
            public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                Log.e("YM", "verify:" + rewardVerify + " amount:" + rewardAmount +
                        " name:" + rewardName);
            }

            @Override
            public void onSkippedVideo() {
                Log.e("YM","rewardVideoAd has onSkippedVide");
            }
        });
        mttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
                mHasShowDownloadActive = false;
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
                    Log.e("YM","下载中，点击下载区域暂停");
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                Log.e("YM","下载暂停，点击下载区域继续");
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                Log.e("YM","下载失败，点击下载区域重新下载");
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                Log.e("YM","下载完成，点击下载区域重新下载");
            }

            @Override
            public void onInstalled(String fileName, String appName) {
                Log.e("YM","安装完成，点击下载区域打开");
            }
        });
    }

    /**
     * 销毁广告
     */
    public void destroy(){

    }
}