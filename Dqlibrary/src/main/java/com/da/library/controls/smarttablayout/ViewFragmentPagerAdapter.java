package com.da.library.controls.smarttablayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
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
