package com.wd.daquan.common.helper;

import android.app.Activity;
import android.widget.ImageView;

import com.da.library.widget.MorePopWindow;
import com.wd.daquan.common.utils.NavUtils;

public class MainTitleRightHelper {
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
                    NavUtils.gotoScanQRCodeActivity(activity);
                    break;
                case MorePopWindow.ItemType.MIND_QRCODE:
                    NavUtils.gotoQRCodeActivity(activity);
                    break;
            }
        });
        mPopWindow.showPopupWindow(rightExtra);
    }

    public void onDestroy(){
        if(mPopWindow != null) {
            mPopWindow.dismiss();
        }
    }
}
