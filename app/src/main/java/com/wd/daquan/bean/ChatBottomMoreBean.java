package com.wd.daquan.bean;


import android.support.annotation.IdRes;

/**
 * 聊天页面底部更多的布局item
 */
public class ChatBottomMoreBean {
    private @IdRes
    int resId = -1;
    private String title;
    public ChatBottomMoreBean() {
    }

    public ChatBottomMoreBean(int resId, String title) {
        this.resId = resId;
        this.title = title;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}