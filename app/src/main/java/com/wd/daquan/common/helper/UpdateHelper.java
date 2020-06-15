package com.wd.daquan.common.helper;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.util.Log;

import com.da.library.constant.IConstant;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.model.bean.UpdateEntity;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.model.log.DqToast;
import com.da.library.listener.DialogListener;
import com.wd.daquan.mine.presenter.MinePresenter;

import java.util.LinkedHashMap;

/**
 * @author: dukangkang
 * @date: 2018/7/16 17:49.
 * @description: todo ...
 */
public class UpdateHelper implements Presenter.IView<DataBean> {
    public static final String TAG = "chuiniu";
    // 检查版本升级接口，3次之后，切换域名
//    private int count = 1;

    // 用户ID
    private String mUid = "";
    // 是否显示升级对话框
    private boolean isShowDialog = true;

    private Activity mActivity = null;
    private Dialog mDialog = null;

    private MinePresenter mMainPresenter = null;
    private EBSharedPrefUser mSharedPrefUser = null;

    public UpdateHelper(Activity context) {
        this.mActivity = context;
        QCSharedPrefManager sharedPrefManager = QCSharedPrefManager.getInstance();
        mSharedPrefUser = sharedPrefManager.getKDPreferenceUserInfo();
        mUid = mSharedPrefUser.getString(EBSharedPrefUser.uid, "");


        mMainPresenter = new MinePresenter();
        mMainPresenter.attachView(this);
    }

    public void checkVersion() {
        if (!DqUtils.isNetworkConnected(mActivity)) {
            DqToast.showShort(mActivity.getResources().getString(R.string.network_unavailable));
            error();
            return;
        }
        //Log.e(TAG, "检查版本升级地址...");
        //Log.e(TAG, "URL.." + DqUrl.url_version_change);
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put("client_type", "android");
//        hashMap.put("client_version", DqUtils.getVersion(DqApp.getInstance()));
        hashMap.put("client_version_code", String.valueOf(DqUtils.getVersionCode(DqApp.getInstance())));
        hashMap.put("userPhone", ModuleMgr.getCenterMgr().getPhoneNumber());

        mMainPresenter.checkVersion(DqUrl.url_version_change, hashMap);
    }

    public void setShowDialog(boolean showDialog) {
        isShowDialog = showDialog;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        // 去掉域名切换
        error();
//        // 测试或预上线不做域名切换
//        if (Host.DEFAULT_HOST.equals(Host.DEBUG) || Host.DEFAULT_HOST.equals(Host.PRE_LINE)) {
//            CNLog.e(TAG, "onFailed default = debug | pre_line");
//            count = 1;
//            error();
//            return;
//        }
//        Log.w(TAG, "onFailed url = " + url);
//
//        String saveHost = mSharedPrefApp.getString(EBSharedPrefApp.DEF_HOST, Host.DEFAULT_HOST);
//        if (Host.SPARE_2.equals(saveHost)) {
////            count = 1;
//            error();
//            return;
//        }
//
//        if (URLUtil.url_version_change.equals(url) || TextUtils.isEmpty(url)) {
//            count++;
//            if (count <= 3) {
//                checkVersion();
//            } else if (count <= 5) {
//                Log.d(TAG, "原始域名: " + URLUtil.url_version_change);
//                // TODO: 2018/7/9 切换域名
//                AESEncrypt.changeHost(Host.SPARE_2);
//                mSharedPrefApp.saveString(EBSharedPrefApp.DEF_HOST, Host.SPARE_2);
//
//                String mKey1 = mSharedPrefApp.getString(EBSharedPrefApp.KEY_1, "");
//                ServerConfig.SERVER = AESEncrypt.getHost(EBApplication.sContext, mUid, mKey1);
//                ServerConfig.CERTIFICATE_NUMBER = AESEncrypt.getCertificateName();
//                URLUtil.reset();
//                Log.d(TAG, "切换到域名: " + URLUtil.url_version_change);
//
//                checkVersion();
//            } else {
//                count = 1;
//                error();
//            }
//        } else {
//            count = 1;
//            error();
//        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        Log.e(TAG, "检查版本升级完成...");
//        count = 1;
        if (null == entity) {
            // 直接进入到主页
            success((UpdateEntity) entity.data);
            Log.e(TAG, "onSuccess entity = null");
            return;
        }

        checkHost(entity);
    }

    private void checkHost(DataBean<UpdateEntity> entity) {
        // TODO: 2018/7/9 获取域名
        // 重置Host
        // 测试和预上线不切换host
//        if (!Host.DEFAULT_HOST.equals(Host.DEBUG) && !Host.DEFAULT_HOST.equals(Host.PRE_LINE)) {
//            if (entity.intentUrl.key2.equals("1")) {
//                AESEncrypt.changeHost(Host.SPARE_1);
//                mSharedPrefApp.saveString(EBSharedPrefApp.DEF_HOST, Host.SPARE_1);
//            }
//        }

//        mSharedPrefApp.saveString(EBSharedPrefApp.KEY_1, entity.intentUrl.key1);
////        mSharedPrefApp.saveString(EBSharedPrefApp.KEY_2, entity.intentUrl.key2);
//
//        ServerConfig.SERVER = AESEncrypt.getHost(EBApplication.sContext, mUid, entity.intentUrl.key1);
//        ServerConfig.CERTIFICATE_NUMBER = AESEncrypt.getCertificateName();
//        URLUtil.reset();
//
//        Log.w("chuiniu", "local key1 = " + entity.intentUrl.key1);
//        Log.w("chuiniu", "local key2 = " + entity.intentUrl.key2);
//        Log.w("chuiniu", "local host = " + ServerConfig.SERVER);
//        Log.w("chuiniu", "local certName = " + ServerConfig.CERTIFICATE_NUMBER);

        if (IConstant.OK.equals(entity.status)) {
            UpdateEntity updateEntity = entity.data;
            if ("1".equals(updateEntity.update_status)) {
                update(updateEntity);
            } else {
                success(updateEntity);
            }
            mSharedPrefUser.saveString(EBSharedPrefUser.UPDATE_STATUS, updateEntity.update_status);
        } else {
            failed(entity.content);
            mSharedPrefUser.saveString(EBSharedPrefUser.UPDATE_STATUS, KeyValue.ZERO_STRING);
        }
    }

    /**
     * 升级弹窗
     * @param updateEntity
     */
    private void update(UpdateEntity updateEntity) {
        updateUi(updateEntity.update_status);

        if (!isShowDialog) {
            success(updateEntity);
            return;
        }

        mDialog = DialogUtils.showCheckDialogs(new DialogListener() {
            @Override
            public void onCancel() {
                success(updateEntity);
            }

            @Override
            public void onOk() {//待添加 9/14

            }
        }, mActivity, updateEntity.appVersion, updateEntity.versionName,
                updateEntity.content, updateEntity.upgradeType, updateEntity.url);

        if ("2".equals(updateEntity.upgradeType)) {
            mDialog.show();
        } else {
            String update = mSharedPrefUser.getString(EBSharedPrefUser.UPDATE, "");
            if (TextUtils.isEmpty(update)) {
                mDialog.show();
                mSharedPrefUser.saveString(EBSharedPrefUser.UPDATE, String.valueOf(DqUtils.getDates()));
            } else {
                if (!String.valueOf(DqUtils.getDates()).equals(update)) {
                    mDialog.show();
                    mSharedPrefUser.saveString(EBSharedPrefUser.UPDATE, String.valueOf(DqUtils.getDates()));
                } else {
                    success(updateEntity);
                }
            }
        }
    }

    public void destory() {
        if(mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }

        mMainPresenter.detachView();
    }

    private void updateUi(String status) {
        if (null != mUpdateListener) {
            mUpdateListener.updateUI(status);
        }
    }

    private void success(UpdateEntity updateEntity) {
        if (null != mUpdateListener) {
            mUpdateListener.updateSucceed(updateEntity);
        }
    }

    private void failed(String msg) {
        if (null != mUpdateListener) {
            mUpdateListener.updateFailed(msg);
        }
    }

    private void error() {
        if (null != mUpdateListener) {
            mUpdateListener.updateError();
        }
    }

    private UpdateListener mUpdateListener = null;

    public void setUpdateListener(UpdateListener updateListener) {
        mUpdateListener = updateListener;
    }
}
