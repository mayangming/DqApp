package com.wd.daquan.contacts.activity;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.da.library.controls.smarttablayout.PagerItem;
import com.da.library.controls.smarttablayout.SmartTabLayout;
import com.da.library.controls.smarttablayout.SmartTabStrip;
import com.da.library.controls.smarttablayout.ViewFragmentPagerAdapter;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.contacts.fragment.NewFriendFragment;
import com.wd.daquan.contacts.fragment.TeamInviteFragment;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.contacts.presenter.ContactPresenter;
import com.wd.daquan.model.bean.UnreadNotifyEntity;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 方志
 * @Time: 2018/9/14 10:52
 * @Description: 新的朋友页面
 */
public class NewFriendActivity extends DqBaseActivity<ContactPresenter, DataBean> implements
        View.OnClickListener, ViewPager.OnPageChangeListener {


    private ViewPager mViewPager;
    private SmartTabLayout mTabLayout;
    private ViewFragmentPagerAdapter mAdapter;

    @Override
    protected ContactPresenter createPresenter() {
        return new ContactPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_new_friend);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        mViewPager = this.findViewById(R.id.new_friend_viewpager);
        mTabLayout = this.findViewById(R.id.new_friend_tablayout);

        mTabLayout.setCustomTabView(new NewFriendTab());
        mTabLayout.setCustomTabView(new NewFriendTab());

        List<PagerItem> list = new ArrayList<>();
        list.add(new PagerItem(DqApp.getStringById(R.string.friend_apply), new NewFriendFragment()));
        list.add(new PagerItem(DqApp.getStringById(R.string.team_invite), new TeamInviteFragment()));
        mAdapter = new ViewFragmentPagerAdapter(getSupportFragmentManager(), list);
        mViewPager.setAdapter(mAdapter);

        mTabLayout.setViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
        setDotVisible(false, 0);
        setDotVisible(ModuleMgr.getAppManager().isContainTeamInvite(), 1);
    }

    private class NewFriendTab implements SmartTabLayout.TabProvider{

        @Override
        public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
            View tabView = getLayoutInflater().inflate(R.layout.sliding_tab_layout_item, container, false);
            TextView tabTitleView = tabView.findViewById(R.id.tv_tab_title);
            tabTitleView.setText(adapter.getPageTitle(position));
            return tabView;
        }
    }

    private void setDotVisible(boolean visible, int position) {
        SmartTabStrip tabStrip = mTabLayout.getTabStrip();
        for (int i = 0; i < tabStrip.getChildCount(); ++i) {
            View tabView = tabStrip.getChildAt(i);
            boolean isSelect = i == position;
            if (isSelect) {
                // 更新小红点
                View tabDot = tabView.findViewById(R.id.tab_item_dot);
                if (tabDot != null) {
                    if (visible) {
                        tabDot.setVisibility(View.VISIBLE);
                    } else {
                        tabDot.setVisibility(View.GONE);
                    }
                }
                break;
            }
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {
        toolbarBack();
        mTabLayout.setOnPageChangeListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            setDotVisible(false, 0);
            ModuleMgr.getAppManager().clearUnreadNotify(UnreadNotifyEntity.UNREAD_ADD_FRIEND);
        } else if (position == 1) {
            setDotVisible(false, 1);
            ModuleMgr.getAppManager().clearUnreadNotify(UnreadNotifyEntity.UNREAD_TEAM_INVITE);
        }
        MsgMgr.getInstance().sendMsg(MsgType.MT_CONTACT_NOTIFY, null);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
