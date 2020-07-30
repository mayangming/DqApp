package com.netease.nim.uikit.business.session.module;

import android.app.Activity;
import android.content.Context;
import androidx.fragment.app.Fragment;

import com.dq.im.type.ImType;

/**
 * Created by zhoujianghua on 2015/7/6.
 */
public class Container {
    public Activity activity = null;
    public Fragment fragment = null;
    public final String account;
    public final ImType sessionType;
    public final ModuleProxy proxy;

    public Container(Activity activity, String account, ImType sessionType, ModuleProxy proxy) {
        this.activity = activity;
        this.account = account;
        this.sessionType = sessionType;
        this.proxy = proxy;
    }
    public Container(Fragment fragment, String account, ImType sessionType, ModuleProxy proxy) {
        this.fragment = fragment;
        this.account = account;
        this.sessionType = sessionType;
        this.proxy = proxy;
    }

    public Context getContext(){
        Context context = activity;
        if (null == context){
            context = fragment.getContext();
        }
        return context;
    }

    public Activity getActivity(){
        Activity act = activity;
        if (null == act){
            act = fragment.getActivity();
        }
        return act;
    }

}
