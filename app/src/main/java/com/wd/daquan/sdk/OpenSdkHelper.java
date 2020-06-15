package com.wd.daquan.sdk;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.da.library.listener.DialogListener;
import com.da.library.tools.ActivitysManager;
import com.da.library.view.CommDialog;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.bean.ShareItemBean;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.helper.DialogHelper;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.login.helper.LoginHelper;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.sdk.bean.SdkShareBean;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DELL on 2018/11/20.
 */

public class OpenSdkHelper implements Presenter.IView<DataBean>{
//    //声明成员变量
//    private static OpenSdkHelper singletonInstance = new OpenSdkHelper();
//    //对外提供接口获取该实例
//    public static OpenSdkHelper getSingletonInstance(){
//        return singletonInstance ;
//    }
    private SdkPresenter mOpenSdkPresenter;
    private Context mContext;

    public OpenSdkHelper(Context mContext){
        this.mContext = mContext;
        mOpenSdkPresenter = new SdkPresenter();
        mOpenSdkPresenter.attachView(this);
    }
    //发送要分享消息的弹窗
    public void showDialog(DqBaseActivity activity,ShareItemBean shareItemBean, SdkShareBean mShareBean) {
        if (TextUtils.isEmpty(mShareBean.appName)){
            DqToast.showShort("app名不能为空");
            return;
        }
        CommDialog mCommDialog = DialogHelper.getInstance().showSDKShareDialog(activity, shareItemBean, mShareBean.appName);
        mCommDialog.show();
        mCommDialog.setDialogListener(new DialogListener() {
            @Override
            public void onCancel() {
                mCommDialog.dismiss();
            }

            @Override
            public void onOk() {
                mCommDialog.dismiss();
                DqLog.e("YM------->分享功能无法使用---->缺乏云信支持");
            }
        });
    }

    /**
     * 链接分享
     */
    private void shareLink(SdkShareBean mShareBean, ShareItemBean shareItemBean, DqBaseActivity activity) {
        DqLog.e("YM------->shareLink分享");
        DqLog.e("YM------->SdkShareBean_url:"+mShareBean.url);
        DqLog.e("YM------->SdkShareBean_appLogo:"+mShareBean.appLogo);
    }

    private void shareLnkAfterUploadFile(SdkShareBean mShareBean, ShareItemBean shareItemBean, DqBaseActivity activity){
        DqLog.e("YM------->shareLnkAfterUploadFile分享---->云信功能不存在，无法分享");
//        SdkShareAttachment msgContent = SdkShareAttachment.obtain(mShareBean.type.value , mShareBean.title, mShareBean.content,
//                mShareBean.url, mShareBean.intentUrl, mShareBean.appId, mShareBean.appSecret,
//                mShareBean.appName, mShareBean.appLogo, mShareBean.backInfo, mShareBean.packageName, mShareBean.extra);
//
//        IMMessage message = MessageBuilder.createCustomMessage(shareItemBean.sessionId,
//                shareItemBean.sessionType, "链接", msgContent);
//        NIMClient.getService(MsgService.class).sendMessage(message, false).setCallback(new RequestCallback<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                requestShareStatistics(mShareBean,true);
//                LoadingDialog.closeLoadingDialog();
//                showConfirmDialog(activity, mShareBean);
//            }
//
//            @Override
//            public void onFailed(int i) {
//                requestShareStatistics(mShareBean,false);
//                LoadingDialog.closeLoadingDialog();
//                DqToast.showShort("分享失败");
//                sendThirdBroadcast(activity, mShareBean, KeyValue.SDKShare.FAIL, String.valueOf(i));
//            }
//
//            @Override
//            public void onException(Throwable throwable) {
//
//            }
//        });
    }

    private void shareImage(File file, ShareItemBean shareItemBean, DqBaseActivity activity, SdkShareBean mShareBean) {
    }

    private void showConfirmDialog(final Activity activity, SdkShareBean mShareBean){

        if (null == activity) {
            return;
        }
        DialogUtils.showShareConfirmDialog(activity, "返回" + mShareBean.appName, new DialogListener() {
            @Override
            public void onCancel() {//退出斗圈
                sendThirdBroadcast(activity, mShareBean, KeyValue.SDKShare.SUCCESS, "0");
                MsgMgr.getInstance().sendMsg(KeyValue.SDK_MAIN_FINISH, null);
                ActivitysManager.getInstance().finishAll();
            }

            @Override
            public void onOk() {
                //留在斗圈
                LoginHelper.gotoMain(activity);
            }
        });
    }


    //分享成功后给第三方返回code
    private void sendThirdBroadcast(Activity activity, SdkShareBean mShareBean, int code, String msg){
        try {
            Intent intent = new Intent(KeyValue.RECEIVER_ACTION);
            intent.setComponent(new ComponentName(mShareBean.packageName, KeyValue.RECEIVER_PATH));
            Log.i("dq", "mShareBean.packageName : " + mShareBean.packageName);
            intent.putExtra("code", code+"");
            intent.putExtra("msg", msg);
            intent.putExtra("type", 2);//1登录2分享
            activity.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onSuccess(String url, int code, DataBean entity) {
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        DqUtils.bequit(entity, mContext);
    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showLoading() {

    }
    /**
     * 分享统计
     * */
    private void requestShareStatistics(SdkShareBean mShareBean,boolean isSuccess){
        String uid = ModuleMgr.getCenterMgr().getUID();
        Map<String, String> hashMap = new HashMap<>();
        Log.e("YM","appId-->"+mShareBean.appId);
        Log.e("YM","type-->"+mShareBean.type.value);
        hashMap.put("appId", mShareBean.appId);
        hashMap.put("type", mShareBean.type.value+"");
        hashMap.put("uid", uid);
        hashMap.put("shareStatus", isSuccess ? "1" : "2");
        mOpenSdkPresenter.sdkShareRecord(DqUrl.url_sdk_get_share_statistics, hashMap);
    }


}
