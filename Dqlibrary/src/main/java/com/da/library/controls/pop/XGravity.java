package com.da.library.controls.pop;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Kind on 2019-05-21.
 */
@IntDef({
        XGravity.CENTER,
        XGravity.LEFT,
        XGravity.RIGHT,
        XGravity.ALIGN_LEFT,
        XGravity.ALIGN_RIGHT,
})
@Retention(RetentionPolicy.SOURCE)
public @interface XGravity {
    int CENTER = 0;
    int LEFT = 1;
    int RIGHT = 2;
    int ALIGN_LEFT = 3;
    int ALIGN_RIGHT = 4;
}
