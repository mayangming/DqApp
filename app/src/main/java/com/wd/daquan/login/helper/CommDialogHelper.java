package com.wd.daquan.login.helper;

import android.app.Activity;
import android.graphics.Color;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.utils.NavUtils;
import com.da.library.listener.DialogListener;
import com.da.library.view.CommDialog;

/**
 * @Author: 方志
 * @Time: 2018/9/14 18:15
 * @Description:
 */
public class CommDialogHelper {

    private CommDialog commDialog;
    private static CommDialogHelper instance = null;

    private CommDialogHelper() {
    }

    public static CommDialogHelper getInstance() {
        synchronized (CommDialogHelper.class) {
            if (instance == null) {
                instance = new CommDialogHelper();
            }
        }
        return instance;
    }

    /**
     * 权限dialog
     */
    public void showPermissionDialog(Activity activity) {
        commDialog = new CommDialog(activity);
        commDialog.setTitleVisible(false);
        commDialog.setDesc(activity.getString(R.string.comm_please_add_permissions));
        commDialog.setDescCenter();
        commDialog.setOkTxt(activity.getString(R.string.comm_setting));
        commDialog.show();

        commDialog.setDialogListener(new DialogListener() {
            @Override
            public void onCancel() {
                activity.finish();
            }

            @Override
            public void onOk() {
                NavUtils.startAppSettings(activity);
            }
        });
    }

    public void onDestroy() {
        if (null != commDialog) {
            commDialog.dismiss();
            commDialog = null;
        }
    }

    /**
     * 非WiFi下载提示dialog
     */
    public CommDialog showNotWifiDialog(Activity activity) {
        commDialog = new CommDialog(activity);
        commDialog.setTitleVisible(true);
        commDialog.setTitleCenter();
        commDialog.setTitle(activity.getString(R.string.sweet_hint));
        commDialog.setDesc(activity.getString(R.string.not_wifi_down));
        commDialog.setDescCenter();
        commDialog.setCancelTxt(activity.getString(R.string.about_logout));
        commDialog.setOkTxt(activity.getString(R.string.download));
        commDialog.show();

        return commDialog;
    }

    /**
     * 密码错误1-4次
     */
    public CommDialog showPasswordErrorDialog(Activity activity, String content) {
        commDialog = new CommDialog(activity);
        commDialog.setTitleVisible(false);
        commDialog.setDesc(content);
        commDialog.setCancelTxt(activity.getString(R.string.retry));
        commDialog.setOkTxt(activity.getString(R.string.forget_password));
        commDialog.show();

        return commDialog;
    }

    /**
     * 密码错误5次
     */
    public CommDialog showPasswordErrorMaxDialog(Activity activity, String content) {
        commDialog = new CommDialog(activity);
        commDialog.setTitleVisible(false);
        commDialog.setDesc(content);
        commDialog.setCancelTxt(activity.getString(R.string.close));
        commDialog.setOkTxt(activity.getString(R.string.code_login));
        commDialog.show();

        return commDialog;
    }


    public CommDialog show(Activity activity, String desc) {
        commDialog = new CommDialog(activity);
        commDialog.setTitleGone();
        commDialog.setDesc(desc);
        commDialog.setCancelTxt(DqApp.getStringById(R.string.cancel));
        commDialog.setOkTxt(DqApp.getStringById(R.string.confirm));
        commDialog.setOkTxtColor(Color.RED);
        commDialog.setWidth();
        commDialog.show();

        return commDialog;
    }
}
