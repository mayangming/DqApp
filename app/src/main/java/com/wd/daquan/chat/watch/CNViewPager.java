package com.wd.daquan.chat.watch;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CNViewPager extends ViewPager {


    private ViewPageHelper helper;

    public CNViewPager(Context context) {
        this(context, null);
    }

    public CNViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        helper = new ViewPageHelper(this);

    }

    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item, true);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        MScroller scroller = helper.getScroller();
        if (Math.abs(getCurrentItem() - item) > 1) {
            scroller.setNoDuration(true);
            super.setCurrentItem(item, smoothScroll);
            scroller.setNoDuration(false);
        } else {
            scroller.setNoDuration(false);
            super.setCurrentItem(item, smoothScroll);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException var3) {
            var3.printStackTrace();
            return false;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
