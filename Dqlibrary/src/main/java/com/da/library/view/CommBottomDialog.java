package com.da.library.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.da.library.R;

import java.lang.ref.WeakReference;

/**
 * @author: dukangkang
 * @date: 2018/8/22 11:38.
 * @description: todo ...
 */
public class CommBottomDialog extends Dialog{

    private int mWidth = 0;
    private View mView;
    private WeakReference<Activity> mWeakReference = null;

    public CommBottomDialog(Activity activity) {
        super(activity, R.style.conversationsa_dialog);
        mWeakReference = new WeakReference<>(activity);

        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mWidth = outMetrics.widthPixels;
    }

    public CommBottomDialog setView(View view) {
        this.mView = view;
        setContentView(mView);
        return this;
    }

    public CommBottomDialog build() {
        Activity activity = mWeakReference.get();
        if (null == activity) {
            return this;
        }
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.dialog_style); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        mView.measure(0, 0);
        lp.width = mWidth; // 宽度
        lp.height = mView.getMeasuredHeight();

        Log.w("xxxx", "mView.getMeasuredHeight() = " + mView.getMeasuredHeight());
        dialogWindow.setAttributes(lp);
        return this;
    }

    public void showDialog() {
        if (!isShowing()) {
            show();
        }
    }

    public void dismissDialog() {
        dismiss();
    }
}
