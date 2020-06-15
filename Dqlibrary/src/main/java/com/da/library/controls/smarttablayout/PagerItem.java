package com.da.library.controls.smarttablayout;

import android.support.v4.app.Fragment;

/**
 * Created by Kind on 16/10/14.
 */

public class PagerItem {

    private int id;             //标识
    private String title;
    private Fragment fragment;

    public PagerItem() {
        super();
    }

    public PagerItem(String title, Fragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }

    public PagerItem(int id, String title, Fragment fragment) {
        this.id = id;
        this.title = title;
        this.fragment = fragment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
