package com.wd.daquan.contacts.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.da.library.constant.IConstant;
import com.da.library.widget.DragPointView;
import com.da.library.widget.SideBar;
import com.dq.im.type.ImType;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wd.daquan.DqApp;
import com.wd.daquan.MainActivity;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.fragment.BaseFragment;
import com.wd.daquan.common.helper.MainTitleRightHelper;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.contacts.adapter.ContactsFragmentAdapter;
import com.wd.daquan.contacts.helper.TransformFriendInfoHelper;
import com.wd.daquan.contacts.listener.IContactsFragmentAdapterListener;
import com.wd.daquan.contacts.listener.ISidebarListener;
import com.wd.daquan.contacts.presenter.ContactPresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;
import com.wd.daquan.third.session.SessionHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 方志
 * @Time: 2018/9/6 17:08
 * @Description: 联系人页面
 */
public class ContactsFragment extends BaseFragment<ContactPresenter, DataBean> implements OnRefreshListener, View.OnClickListener, QCObserver {

    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private SideBar mSideBar;
    private TextView mLetterTv;
    private ContactsFragmentAdapter mAdapter;
    private List<Friend> mFriendList = new ArrayList<>();
    private ImageView mTitleRightSearch;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contacts;
    }

    @Override
    public ContactPresenter createPresenter() {
        return new ContactPresenter();
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        TextView title = view.findViewById(R.id.toolbar_title);
        title.setText(DqApp.getStringById(R.string.main_tab_contacts));
        ImageView titleRightExtra = view.findViewById(R.id.toolbar_right_extra);
        mTitleRightSearch = view.findViewById(R.id.toolbar_right_search);
        MainTitleRightHelper.getInstance().init(getActivity(), titleRightExtra);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mRecyclerView = view.findViewById(R.id.contacts_fragment_recycler_view);
        mSideBar = view.findViewById(R.id.contacts_fragment_side_bar);
        mLetterTv = view.findViewById(R.id.contacts_fragment_letter);
    }

    @Override
    public void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ContactsFragmentAdapter();
        mRecyclerView.setAdapter(mAdapter);
//        mRefreshLayout.setEnableRefresh(false);
        getContacts(false);
    }

    public void getContacts(boolean isShowLoading) {
        Boolean isExitHelp = FriendDbHelper.getInstance().isExitHelp();
        Boolean isExitSelf = FriendDbHelper.getInstance().isExitSelf();

        long listTime = ModuleMgr.getCenterMgr().getFriendListTime();
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(IConstant.Contact.WHETHER_HELPER, isExitHelp ? "1" : "0");
        hashMap.put(IConstant.Contact.SHOW_SELF, isExitSelf ? "1" : "0");
        hashMap.put(IConstant.Contact.LAST_TIME, String.valueOf(listTime));
        mPresenter.getContacts(DqUrl.url_friend_list, hashMap, isShowLoading);
    }

    private void updateData(List<Friend> friendList) {

        //添加拼音
        List<Friend> list = TransformFriendInfoHelper.getInstance().setPinYin(friendList);
//        mRefreshLayout.setEnableRefresh(mFriendList.size() > 0);
        //本地数据缓存
        FriendDbHelper.getInstance().update(list, friends -> useCacheData());

        // 检查首次是否有好用请求和邀请进群
        ModuleMgr.getAppManager().getUnreadNotifyList();
        MsgMgr.getInstance().sendMsg(MsgType.MT_CONTACT_NOTIFY, null);

        //记录请求的时间
        ModuleMgr.getCenterMgr().setFriendListTime(System.currentTimeMillis());
    }


    @Override
    public void initListener() {
        mTitleRightSearch.setOnClickListener(this);
        mRefreshLayout.setOnRefreshListener(this);
        mAdapter.setListener(mContactAdapterListener);
        mSideBar.setOnTouchingLetterChangedListener(mSideBarListener);

        MainActivity activity = (MainActivity) getActivity();
        if(null != activity) {
            activity.setSidebarListener(mSidebarListener);
        }

        MsgMgr.getInstance().attach(this);

    }

    private ISidebarListener mSidebarListener = new ISidebarListener() {

        @Override
        public void scroll(boolean b) {
            if (null != mSideBar) {
                mSideBar.refreshUI(b);
            }
        }
    };

    private SideBar.OnTouchingLetterChangedListener mSideBarListener = letter -> {
        mSideBar.setTextView(mLetterTv);
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();

        if("↑".equals(letter)) {
            mRecyclerView.scrollToPosition(0);
            layoutManager.scrollToPositionWithOffset(0, 0);
            return;
        }

        //该字母首次出现的位置
        int position = mAdapter.getPositionForSection(letter.charAt(0));
        int newPos = position + 1;
        if(position != -1) {
            mRecyclerView.scrollToPosition(newPos);
            layoutManager.scrollToPositionWithOffset(newPos, 0);
        }
    };


    private DragPointView mNewFriendNotify;
    private IContactsFragmentAdapterListener mContactAdapterListener = new IContactsFragmentAdapterListener() {
        @Override
        public void onItemClick(int position) {
            Friend friend = mAdapter.getItem(position);
            if ("1".equals(friend.uid)) {
                //小助手直接进会话
                SessionHelper.startP2PSession(getActivity(), friend.uid);
            } else {
                NavUtils.gotoUserInfoActivity(getActivity(), friend.uid, ImType.P2P.getValue());
            }
        }

        @Override
        public void onFresh() {
            getContacts(true);
        }

        @Override
        public void onHeaderNewFriend(DragPointView newFriendNotify) {
            mNewFriendNotify = newFriendNotify;
        }
    };

    public void updateNotify(String num) {
        if (TextUtils.isEmpty(num)) {
            if(null != mNewFriendNotify) {
                mNewFriendNotify.setVisibility(View.GONE);
            }
            return;
        }
        if(null != mNewFriendNotify) {
            mNewFriendNotify.setVisibility(View.VISIBLE);
            mNewFriendNotify.setText(num);
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getContacts(false);
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        mRefreshLayout.finishRefresh();
        if(DqUrl.url_friend_list.equals(url)) {
            List<Friend> friendList = (List<Friend>) entity.data;
            if(null != friendList && friendList.size() > 0) {
                updateData(friendList);
            }else {
                useCacheData();
            }
        }
    }

    /**
     * 使用数据库缓存数据
     */
    public void useCacheData() {
        FriendDbHelper.getInstance().getAll(friendList -> {
            if(friendList.size() > 0) {
                List<Friend> list = TransformFriendInfoHelper.getInstance().setPinYin(friendList);
                updateAdapter(list);

            }
        });

    }

    private void updateAdapter(List<Friend> list) {
        mFriendList.clear();
        mFriendList.addAll(list);
        //按拼音排序
        Collections.sort(mFriendList, (o1, o2) -> {
            if ("@".equals(o1.getPinYin()) || "#".equals(o2.getPinYin())) {
                return -1;
            } else if ("#".equals(o1.getPinYin()) || "@".equals(o2.getPinYin())) {
                return 1;
            } else {
                return o1.getPinYin().compareTo(o2.getPinYin());
            }
        });
        //Log.e("dq ", " updateAdapter ：" + mFriendList.size());
        mAdapter.update(mFriendList);
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        mRefreshLayout.finishRefresh();
//        mRefreshLayout.setEnableRefresh(mFriendList.size() > 0);
        if(DqUrl.url_friend_list.equals(url)) {
            useCacheData();
        }
        if(entity == null) return;
        DqUtils.bequit(entity, getActivity());
        //DqToast.showShort(entity.content);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mTitleRightSearch.getId()) {
            NavUtils.gotoSearchFriendActivity(getActivity());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MsgMgr.getInstance().detach(this);
    }

    /**
     * 订阅消息处理
     * @param key   事件key
     * @param value 事件传递值
     */
    @Override
    public void onMessage(String key, Object value) {
        switch (key) {
            case MsgType.MT_FRIEND_REMARKS_CHANGE:
            case MsgType.MT_FRIEND_REMOVE_BLACK_LIST:
            case MsgType.MT_FRIEND_ADD_BLACK_LIST:
            case MsgType.MT_FRIEND_REMOVE_FRIEND:
            case MsgType.MT_CENTER_PERSONALINFO_CHANGE:
                //getContacts(false);
                useCacheData();
                break;
            case MsgType.MT_FRIEND_ADD_FRIEND://收到删除消息时将上次更新时间置为0
                //记录请求的时间
                ModuleMgr.getCenterMgr().setFriendListTime(0);
                getContacts(false);
                break;
        }
    }
}
