package com.da.library.dialog;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.da.library.R;
import com.da.library.utils.TimerUtil;
import com.wd.daquan.model.rxbus.MsgMgr;


/**
 * Created by fangzhi on 2019/3/29.
 */
public class LoadingDialog extends DialogPopup {

    private static String loadingTxt;
    private static LoadingDialog loadingDialog;

    public LoadingDialog(FragmentActivity activity) {
        super(activity);
    }

    /**
     * 显示loading
     */
    public static synchronized void show(FragmentActivity activity, String text) {
        loadingTxt = text;

        if (loadingDialog != null) {
            if (loadingDialog.isShowing()) loadingDialog.dismiss();
            loadingDialog = null;
        }
        loadingDialog = new LoadingDialog(activity);
        loadingDialog.setWidth(-1);

        MsgMgr.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadingDialog != null) {
                    loadingDialog.show();
                }
            }
        });
    }

    public static synchronized void show(FragmentActivity activity) {
        loadingTxt = "";

        if (loadingDialog != null) {
            if (loadingDialog.isShowing()) loadingDialog.dismiss();
            loadingDialog = null;
        }
        loadingDialog = new LoadingDialog(activity);
        loadingDialog.setWidth(-1);

        MsgMgr.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadingDialog != null) {
                    loadingDialog.show();
                }
            }
        });
    }

    /**
     * 关闭loading
     */
    public static synchronized void closeLoadingDialog() {
        MsgMgr.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadingDialog != null) {
                    if (loadingDialog.isShowing()) loadingDialog.dismiss();
                    loadingDialog = null;
                }
            }
        });
    }

    /**
     * 关闭loading
     *
     * @param tm 延迟关闭时间
     */
    public static void closeLoadingDialog(int tm) {
        closeLoadingDialog(tm, new TimerUtil.CallBack() {
            @Override
            public void call() {
                closeLoadingDialog();
            }
        });
    }

    /**
     * 关闭loading
     *
     * @param tm       延迟关闭时间
     * @param callBack 延迟结束后进行的操作回调
     */
    public static void closeLoadingDialog(int tm, final TimerUtil.CallBack callBack) {
        MsgMgr.getInstance().delay(new Runnable() {
            @Override
            public void run() {
                closeLoadingDialog();
                callBack.call();
            }
        }, tm);
    }

    @Override
    protected View makeContentView() {
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.loading_dialog, null);
        TextView loading_txt = inflate.findViewById(R.id.tipTextView);

        loading_txt.setText(loadingTxt);
        loading_txt.setVisibility(TextUtils.isEmpty(loadingTxt) ? View.GONE : View.VISIBLE);

        return inflate;
    }
}
