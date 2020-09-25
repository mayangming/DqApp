package com.wd.daquan.common.helper;

import android.app.Activity;
import android.widget.ImageView;

import com.da.library.widget.MorePopWindow;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.log.DqToast;

import java.util.List;

public class MainTitleRightHelper {
    /**
     * 基本权限管理
     */
    private final String[] BASIC_PERMISSIONS = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
//            android.Manifest.permission.READ_PHONE_STATE,
//            android.Manifest.permission.RECORD_AUDIO,
    };
    private static volatile MainTitleRightHelper mInstance;
    private MainTitleRightHelper(){}
    public static MainTitleRightHelper getInstance(){
        if(mInstance == null) {
            synchronized (MainTitleRightHelper.class){
                if(mInstance == null) {
                    mInstance = new MainTitleRightHelper();
                }
            }
        }
        return mInstance;
    }


    private MorePopWindow mPopWindow;

    public void init(Activity activity, ImageView rightExtra) {

        rightExtra.setOnClickListener(view -> showMoreDialog(activity, rightExtra));
    }

    private void showMoreDialog(Activity activity, ImageView rightExtra){
        if (activity == null) {
            return;
        }
        if(mPopWindow == null) {
            mPopWindow = new MorePopWindow(activity);
        }

        mPopWindow.setOnItemClick(type -> {
            switch (type) {
                case MorePopWindow.ItemType.TEAM:
                    NavUtils.gotoSelectedActivity(activity);
                    break;
                case MorePopWindow.ItemType.ADD_FRIEND:
                    NavUtils.gotoAddFriendActivity(activity);
                    break;
                case MorePopWindow.ItemType.SCAN_CODE:
                    requestPermission(activity);
                    break;
                case MorePopWindow.ItemType.MIND_QRCODE:
                    NavUtils.gotoQRCodeActivity(activity);
                    break;
            }
        });
        mPopWindow.showPopupWindow(rightExtra);
    }

    private void requestPermission(Activity activity){
        XXPermissions.with(activity)
                // 申请安装包权限
                //.permission(Permission.REQUEST_INSTALL_PACKAGES)
                // 申请悬浮窗权限
                //.permission(Permission.SYSTEM_ALERT_WINDOW)
                // 申请通知栏权限
                //.permission(Permission.NOTIFICATION_SERVICE)
                // 申请系统设置权限
                //.permission(Permission.WRITE_SETTINGS)
                // 申请单个权限
                .permission(Permission.CAMERA)
                // 申请多个权限
//                .permission(Permission.Group.CALENDAR)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                        if (all) {
                            DqLog.e("获取拍照权限成功");
                            NavUtils.gotoScanQRCodeActivity(activity);
                        } else {
                            DqLog.e("获取拍照权限成功,部分权限未正常授予");

                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean never) {
                        if (never) {
                            DqToast.showShort("被永久拒绝授权，请手动授予存储和拍照权限");
                            DqLog.e("被永久拒绝授权，请手动授予存储和拍照权限");
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(activity, denied);
                        } else {
                            DqLog.e("获取拍照权限失败");
                            DqToast.showShort("获取拍照权限失败");
                        }
                    }
                });
    }

    public void onDestroy(){
        if(mPopWindow != null) {
            mPopWindow.dismiss();
        }
    }
}
