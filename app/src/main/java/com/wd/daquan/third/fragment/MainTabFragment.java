package com.wd.daquan.third.fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.wd.daquan.R;
import com.netease.nim.uikit.common.fragment.TabFragment;
import java.lang.ref.WeakReference;

public abstract class MainTabFragment extends TabFragment {

    private boolean loaded = false;

    private int layoutId = -1;

    protected abstract void onInit();

    protected abstract void setContentView();

    protected boolean inited() {
        return loaded;
    }

    private WeakReference<Activity> mWeakReference = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        mWeakReference = new WeakReference<>(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_tab_fragment_container, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCurrent();
    }

    public void setContentView(int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    public void onCurrent() {
        super.onCurrent();

        if (!loaded && loadRealLayout()) {
            loaded = true;
            onInit();
        }
    }

    private boolean loadRealLayout() {
        ViewGroup root = (ViewGroup) getView();
        if (root != null) {
            root.removeAllViewsInLayout();
            View.inflate(root.getContext(), layoutId, root);
        }
        return root != null;
    }

    public Activity getCurrentActivity() {
        if (mWeakReference == null) {
            return null;
        }
        return mWeakReference.get();
    }
}
