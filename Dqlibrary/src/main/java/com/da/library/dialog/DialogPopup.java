package com.da.library.dialog;

import android.content.DialogInterface;
import androidx.annotation.CallSuper;
import androidx.annotation.StyleRes;
import androidx.fragment.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.wd.daquan.model.mgr.ModuleMgr;


/**
 * Created by Kind on 2019/3/29.
 */
public abstract class DialogPopup<V extends View> implements DialogInterface.OnKeyListener {

    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    private FragmentActivity activity;
    private int screenWidthPixels;
    private int screenHeightPixels;
    private Popup popup;
    private int width = 0, height = -1;
    private boolean isFillScreen = false;
    private boolean isHalfScreen = false;

    public DialogPopup(FragmentActivity activity) {
        this.activity = activity;
        screenWidthPixels = ModuleMgr.getAppMgr().getScreenWidth(activity);
        screenHeightPixels = ModuleMgr.getAppMgr().getScreenHeight(activity);

        popup = new Popup(activity);
        popup.setOnKeyListener(this);
    }

    protected abstract V makeContentView();

    /**
     * 弹出窗显示之前调用，初始化窗体大小等
     */
    private void onShowPrepare() {
        setContentViewBefore();
        V view = makeContentView();
        popup.setContentView(view);// 设置弹出窗体的布局
        if (width == 0 && height == -1) {
            //未明确指定宽高，宽度优先考虑屏幕的4/5，高度优先考虑全屏再考虑半屏然后再考虑包裹内容
            width = (int) (screenWidthPixels * 0.8);
            if (isFillScreen) {
                height = MATCH_PARENT;
            } else if (isHalfScreen) {
                height = screenHeightPixels / 2;
            } else {
                height = WRAP_CONTENT;
            }
        }
        popup.setSize(width, height);
    }

    /**
     * @return 当前显示的view
     * 获取当前Dialog的View
     */
    public View getContentView() {
        return popup.getContentView();
    }

    protected void setContentViewBefore() {
    }

    /**
     * 固定高度为屏幕的高
     */
    public void setFillScreen(boolean fillScreen) {
        isFillScreen = fillScreen;
    }

    /**
     * 固定高度为屏幕的一半
     */
    public void setHalfScreen(boolean halfScreen) {
        isHalfScreen = halfScreen;
    }

    /**
     * 设置动画
     *
     * @param animRes
     */
    public void setAnimationStyle(@StyleRes int animRes) {
        popup.setAnimationStyle(animRes);
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        popup.setOnDismissListener(onDismissListener);
    }

    /**
     * 设置宽高.
     *
     * @param width
     * @param height
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isShowing() {
        return popup.isShowing();
    }

    @CallSuper
    public void show() {
        onShowPrepare();
        popup.show();
    }

    /**
     * Dismiss.
     */
    public void dismiss() {
        popup.dismissAllowingStateLoss();
    }

    /**
     * On key down boolean.
     *
     * @param keyCode the key code
     * @param event   the event
     * @return the boolean
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public final boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            return onKeyDown(keyCode, event);
        }
        return false;
    }

    public Window getWindow() {
        return popup.getWindow();
    }

    public ViewGroup getRootView() {
        return popup.getRootView();
    }

    /**
     * 设置弹框是否点击返回键消失
     *
     * @param dialogCancelable 是否可以点击返回键消失
     */
    public void setDialogCancelable(boolean dialogCancelable) {
        if (popup != null) popup.setCancelable(dialogCancelable);
    }

    /**
     * 设置弹框是否点击窗体外侧消失
     *
     * @param dialogCancelable 是否可以点击窗体外侧消失
     */
    public void setCancelableOutTouch(boolean dialogCancelable) {
        if (popup != null) popup.getDialog().setCanceledOnTouchOutside(dialogCancelable);
    }

    /**
     * TODO 当activity为null将引起奔溃
     *
     * @return
     */
    public FragmentActivity getActivity() {
        return activity;
    }
}
