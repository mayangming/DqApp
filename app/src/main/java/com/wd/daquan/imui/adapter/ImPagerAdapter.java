//package com.wd.daquan.imui.adapter;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentPagerAdapter;
//
//import com.example.qcsdk.fragment.BaseFragment;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ImPagerAdapter extends FragmentPagerAdapter{
//    private List<BaseFragment> fragmentList = new ArrayList<>();
//
//    public ImPagerAdapter(@NonNull FragmentManager fm, int behavior, List<BaseFragment> fragmentList) {
//        super(fm, behavior);
//        this.fragmentList = fragmentList;
//    }
//
//    @NonNull
//    @Override
//    public Fragment getItem(int position) {
//        return fragmentList.get(position);
//    }
//
//    @Override
//    public int getCount() {
//        return fragmentList.size();
//    }
//
//}