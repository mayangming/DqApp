package com.da.library.controls.smarttablayout;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * viewGroupPagerAdapter
 * Created by Kind on 2018/10/25.
 */
public class ViewFragmentPagerAdapter extends FragmentPagerAdapter {

    public List<PagerItem> pagerItems;

    @Override
    public Fragment getItem(int position) {
        return pagerItems == null ? null : pagerItems.get(position).getFragment();
    }

    public void add(PagerItem pagerItem) {
        if (this.pagerItems == null) {
            pagerItems = new ArrayList<>();
        }
        pagerItems.add(pagerItem);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pagerItems == null ? 0 : pagerItems.size();
    }

    public ViewFragmentPagerAdapter(FragmentManager fm, List<PagerItem> views) {
        super(fm);
        this.pagerItems = views;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return getPagerItem(position).getTitle();
    }

    public PagerItem getPagerItem(int position) {
        return pagerItems.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       // super.destroyItem(container, position, object);
    }
}
