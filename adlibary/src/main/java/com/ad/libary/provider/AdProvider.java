package com.ad.libary.provider;

import android.content.Context;

import androidx.core.content.FileProvider;

public class AdProvider extends FileProvider {
    public static Context context;
    public AdProvider() {
    }

    @Override
    public boolean onCreate() {
        context = getContext();
        return super.onCreate();
    }
}