package com.da.library.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.da.library.DqLibConfig;

public abstract class DialogComponents extends Dialog {

//    public DialogComponents(Context context) {
//        super(context, 0);
//    }

    public DialogComponents(Context context, int theme) {
        super(context, theme);
        initDialogAttrs();
    }

    public void configAttrs(LayoutParams params, Window window, boolean isTransparent) {
        if (isTransparent) window.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    // 设置dialog参数.
    private void initDialogAttrs() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setLayout((int) (getScreenWidth() * 0.85), LayoutParams.WRAP_CONTENT);
        configAttrs(params, window, false);
    }

    public int getScreenWidth() {
        WindowManager wm = (WindowManager) DqLibConfig.sContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
}
