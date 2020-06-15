package com.da.library.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.da.library.R;

/**
 * 加栽框
 */
public class Popup extends DialogFragment {

    private Dialog dialog;
    private FrameLayout contentLayout;
    private FragmentActivity activity;

    private DialogInterface.OnDismissListener dismissListener;

    public Popup() {

    }

    @SuppressLint("ValidFragment")
    public Popup(FragmentActivity activity) {
        this.activity = activity;
        init(activity);
    }

    private void init(Context context) {
        contentLayout = new FrameLayout(context);
        contentLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        contentLayout.setFocusable(true);
        contentLayout.setFocusableInTouchMode(true);

        dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setCanceledOnTouchOutside(false);//触摸屏幕取消窗体
        dialog.setCancelable(true);//按返回键取消窗体

        Window window = dialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);//位于屏幕中间
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //android.util.AndroidRuntimeException: requestFeature() must be called before adding content


            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.setContentView(contentLayout);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return dialog;
    }

    @Override
    public Dialog getDialog() {
        return dialog;
    }

    public Context getContext() {
        return contentLayout.getContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void setAnimationStyle(@StyleRes int animRes) {
        Window window = dialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(animRes);
        }
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }

    @CallSuper
    public void show() {
        synchronized (this) {
            if (activity == null || activity.isFinishing() || isAdded()) {
                Log.e("Popup", "show: ------>activity is finish or the popup dialog has added to window, it's returned without showing.");
                return;
            }
            try {
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.add(Popup.this, activity.toString());
                transaction.commitAllowingStateLoss();
                activity.getSupportFragmentManager().executePendingTransactions();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setContentView(View view) {
        contentLayout.removeAllViews();
        contentLayout.addView(view);
    }

    public View getContentView() {
        return contentLayout.getChildAt(0);
    }

    /**
     * 设置宽高
     *
     * @param width  the width
     * @param height the height
     */
    public void setSize(int width, int height) {
        ViewGroup.LayoutParams params = contentLayout.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(width, height);
        } else {
            params.width = width;
            params.height = height;
        }
        contentLayout.setLayoutParams(params);
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.dismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dismissListener != null) {
            dismissListener.onDismiss(dialog);
        }
    }

    public void setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        dialog.setOnKeyListener(onKeyListener);
    }

    public Window getWindow() {
        return dialog.getWindow();
    }

    public ViewGroup getRootView() {
        return contentLayout;
    }
}
