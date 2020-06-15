package com.da.library.view.base;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.da.library.R;

/**
 * 基类
 * Created by Kind on 2018/11/6.
 */

public class BaseDialogFragment extends DialogFragment {
    private View contentView;
    private Dialog dialog;
    private int _width;
    private int _height;
    private int _gravity;
    public float dimAmount = -1;
    private int _animationsResId;
    private boolean _cancelable = true;
    private DialogInterface.OnDismissListener onDismissListener;
    private DialogInterface.OnCancelListener onCancelListener;

    private LayoutInflater inflater;
    private ViewGroup container;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;

        dialog = getDialog();
        dialog.setCanceledOnTouchOutside(_cancelable);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.Dialog);
        return dialog;
    }

    /**
     * 设置是否可以点击外侧消失
     *
     * @param _cancelable 是否可以点击外侧消失
     */
    public void setCanceledOnTouchOutside(boolean _cancelable) {
        this._cancelable = _cancelable;
    }

    /**
     * 设置透明度dialog的背景透明度
     *
     * @param dimAmount dialog背景透明度
     */
    public void setDimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
    }

    @Override
    public void onResume() {
        super.onResume();
        //设置参数
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();

        // 设置动画效果
        if (this._animationsResId > 0) {
            window.setWindowAnimations(this._animationsResId);
        }

        if (isExactSize) {
            lp.width = this._width;
            lp.height = this._height;
        } else {
            //设置宽度
            if (this.dialog_width_ratio > 0) {
                setWidthRatio(this.dialog_width_ratio);
                lp.width = this._width;
            } else if (this.dialog_width_ratio == -1) {
                lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
            } else {
                lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            }

            //设置高度
            if (this.dialog_height_ratio > 0) {
                setHeightRatio(this.dialog_height_ratio);
                lp.height = this._height;
            } else if (this.dialog_height_ratio == -1) {
                lp.height = LinearLayout.LayoutParams.MATCH_PARENT;
            } else {
                lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            }
        }
        //设置位置
        if (_gravity != 0) {
            lp.gravity = _gravity;
        }

        if (dimAmount != -1) {
            lp.dimAmount = dimAmount;
        }
        window.setAttributes(lp);
    }

    /**
     * 设置显示位置
     *
     * @param gravity Gravity.BOTTOM  or  Gravity.TOP  or Gravity.CENTER
     */
    public void setGravity(int gravity) {
        this._gravity = gravity;
    }

    /**
     * 设置运行动画
     *
     * @param animationsResId dialog进入退出动画
     */
    public void settWindowAnimations(int animationsResId) {
        this._animationsResId = animationsResId;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (this.onDismissListener != null) this.onDismissListener.onDismiss(dialog);
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (onCancelListener != null) onCancelListener.onCancel(dialog);
        super.onCancel(dialog);
    }

    private boolean isExactSize = false;
    private double dialog_width_ratio;
    private double dialog_height_ratio;

    /**
     * 直接设置页面的宽高，权重大于设置页面屏幕宽高比例的比例
     *
     * @param width  宽度
     * @param height 高度
     */
    public void setSize(int width, int height) {
        isExactSize = true;
        this._width = width;
        this._height = height;
    }

    /**
     * 设置Dialog的屏幕占比
     * 0为自适应高度，-1为全屏
     *
     * @param dialog_width_ratio  屏幕宽度比例
     * @param dialog_height_ratio 屏幕高度比例
     */
    public void setDialogSizeRatio(double dialog_width_ratio, double dialog_height_ratio) {
        this.dialog_width_ratio = dialog_width_ratio;
        this.dialog_height_ratio = dialog_height_ratio;
    }

    /**
     * 设置宽度屏占比
     *
     * @param dialog_width_ratio 屏幕宽度比例
     */
    public void setDialogWidthSizeRatio(double dialog_width_ratio) {
        this.dialog_width_ratio = dialog_width_ratio;
    }

    /**
     * 设置宽度屏占比
     */
    private void setWidthRatio(double dialog_width_ratio) {
        DisplayMetrics dm = getDisplayMetrics();
        _width = (int) (dm.widthPixels * dialog_width_ratio);
    }

    /**
     * 设置高度屏占比
     */
    private void setHeightRatio(double dialog_height_ratio) {
        DisplayMetrics dm = getDisplayMetrics();
        _height = (int) (dm.heightPixels * dialog_height_ratio);
    }

    /**
     * 获取宽高对象
     */
    private DisplayMetrics getDisplayMetrics() {
        WindowManager windowManager = getActivity().getWindow().getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 设置内容组件
     *
     * @param resourcesId 资源id
     */
    public View setContentView(int resourcesId) {
        contentView = inflater.inflate(resourcesId, container, false);
        return contentView;
    }

    public View setContent(View resourcesId) {
        container.addView(resourcesId);
        contentView = container.getRootView();
        return contentView;
    }

    /**
     * 获取内容组件
     *
     * @return view组件
     */
    public View getContentView() {
        return this.contentView;
    }

    /**
     * 查找组件
     *
     * @param id 控件id
     * @return view组件
     */
    public View findViewById(int id) {
        return contentView.findViewById(id);
    }

    @SuppressLint("LongLogTag")
    public void showDialog(FragmentActivity context) {
        try {
            if (!isAdded()) {
                FragmentTransaction transaction = context.getSupportFragmentManager().beginTransaction();
                transaction.add(this, context.toString());
                transaction.commitAllowingStateLoss();
            } else {
                Log.e("---BaseDialogFragment--->", "The BaseDialogFragment is already show.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets on key listener.
     *
     * @param onKeyListener the on key listener
     */
    public void setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        dialog.setOnKeyListener(onKeyListener);
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    public void dismiss() {
        dismissAllowingStateLoss();
    }


    /**
     * Gets window.
     *
     * @return the window
     */
    public Window getWindow() {
        return dialog.getWindow();
    }

    /**
     * Gets root view.
     *
     * @return the root view
     */
//    public ViewGroup getRootView() {
//        return contentLayout;
//    }

    /**
     * Is showing boolean.
     *
     * @return the boolean
     */
    public boolean isShowing() {
        return dialog.isShowing();
    }

    /**
     * Sets animation.
     *
     * @param animRes the anim res
     */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void setAnimationStyle(@StyleRes int animRes) {
        Window window = dialog.getWindow();
        if(window != null){
            window.setWindowAnimations(animRes);
        }
    }
}
