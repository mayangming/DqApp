package com.wd.daquan.util;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.da.library.tools.ActivitysManager;
import com.wd.daquan.common.utils.ForegroundCallbacks;
import com.wd.daquan.mine.dialog.RedPackageTipDialog;

/**
 * 全局系统提示
 */
public class ApplicationDialog {
    private static ApplicationDialog instance = new ApplicationDialog();

    private ApplicationDialog() {
    }

    public static ApplicationDialog getInstance(){
        return instance;
    }
    public void showRedRainTipDialog(){
        FragmentActivity activity = ActivitysManager.getInstance().currentActivity();
        if (ForegroundCallbacks.get().isBackground()){
            //后台的话不弹出红包雨到来的提示
            return;
        }
        RedPackageTipDialog redPackageTipDialog = new RedPackageTipDialog();
        Bundle bundle = new Bundle();
        bundle.putString(RedPackageTipDialog.ACTION_TIP_CONTENT,"半小时后红包雨开始，请做好准备");
        redPackageTipDialog.setArguments(bundle);
        redPackageTipDialog.show(activity.getSupportFragmentManager(),"");
    }
    public void showRedRainStartTipDialog(){
        FragmentActivity activity = ActivitysManager.getInstance().currentActivity();
        RedPackageTipDialog redPackageTipDialog = new RedPackageTipDialog();
        Bundle bundle = new Bundle();
        bundle.putString(RedPackageTipDialog.ACTION_TIP_CONTENT,"红包雨活动开始了，请前往首页参加红包雨活动~");
        redPackageTipDialog.setArguments(bundle);
        redPackageTipDialog.show(activity.getSupportFragmentManager(),"");
    }

}