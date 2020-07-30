package com.da.library.view;

import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.view.View;

/**
 * 解决滑动冲突
 * BottomSheetDialog滑动消失和内部View滑动的问题，该方案导致BottomSheetDialog不可以滑动消失
 */
public class CustomBottomSheetDialog extends BottomSheetDialog{
    public CustomBottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    public CustomBottomSheetDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    public CustomBottomSheetDialog(@NonNull Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback
            = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet,
                                   @BottomSheetBehavior.State int newState) {
            if (newState == BottomSheetBehavior.STATE_DRAGGING) {//判断为向下拖动行为时，则强制设定状态为展开
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED );
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };
    BottomSheetBehavior bottomSheetBehavior;
    public void setmBottomSheetCallback(View sheetView) {
        if (bottomSheetBehavior == null) {
            bottomSheetBehavior = BottomSheetBehavior.from(sheetView);
        }
        bottomSheetBehavior.setBottomSheetCallback(mBottomSheetCallback);
    }
}