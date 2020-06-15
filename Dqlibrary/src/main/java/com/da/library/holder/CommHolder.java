package com.da.library.holder;

import android.view.View;

/**
 * @author: dukang
 * @date: 2018/4/18 18:10.
 * @description: 公共Holder
 */
public class CommHolder {
    public View mView;

    public CommHolder(View view) {
        this.mView = view;
        view.setTag(this);
    }

    public View getView() {
        return mView;
    }
}
