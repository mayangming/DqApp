package com.da.library.listener;

import android.view.View;

public interface ITabSelectListener {
    void onTabSelect(View view, int position);
    void onTabReselect(View view, int position);
}