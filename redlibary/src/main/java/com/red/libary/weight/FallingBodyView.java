package com.red.libary.weight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 *  飘落物
 * Created by ys on 2017/2/7 0007.
 */

public class FallingBodyView extends AppCompatImageView {


    public FallingBodyView(Context context) {
        super(context);
    }

    public FallingBodyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FallingBodyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDrawable(int resourceId){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),resourceId);
        setImageDrawable(new BitmapDrawable(getResources(),bitmap));
    }
}
