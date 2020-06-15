package com.wd.daquan.chat.card;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.da.library.constant.IConstant;
import com.da.library.listener.IOnItemClickSelectListener;
import com.da.library.tools.Utils;
import com.da.library.widget.CommSearchEditText;
import com.da.library.widget.CommTitle;
import com.da.library.widget.SideBar;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.contacts.helper.TransformFriendInfoHelper;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.log.DqToast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: dukangkang
 * @date: 2018/9/26 14:44.
 * @description: todo ...
 */
public class CardActivity extends DqBaseActivity<CardPresenter, DataBean> implements View.OnClickListener, OnRefreshListener, OnLoadMoreListener, IOnItemClickSelectListener<Friend> {

    // 请求页码
    protected int mCurPage = 1;

    private List<Friend> mFriendList = new ArrayList<>();

    private TextView mCenterTv = null;
    private SideBar mSideBar = null;
    private RecyclerView mRecyclerView = null;

    private RefreshLayout mRefreshLayout = null;

    private CommTitle mCommTitle = null;
    private CommSearchEditText mSearchEditText = null;

    private CardAdapter mAdapter = null;

    @Override
    protected CardPresenter createPresenter() {
        return new CardPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.card_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        mCommTitle = this.findViewById(R.id.card_commtitle);
        mSearchEditText = this.findViewById(R.id.card_search_et);
        mRefreshLayout = this.findViewById(R.id.card_refreshlayout);
        mRecyclerView = this.findViewById(R.id.card_recyclerview);
        mCenterTv = this.findViewById(R.id.card_center_tv);
        mSideBar = this.findViewById(R.id.card_sidebar);

        mCommTitle.setTitle("选择联系人");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMore(false);
    }

    @Override
    protected void initData() {
        mSideBar.setTextView(mCenterTv);

        mAdapter = new CardAdapter();
        mAdapter.setMode(IConstant.Select.SINGLE);
        mRecyclerView.setAdapter(mAdapter);

        FriendDbHelper.getInstance().getAllFriend(friends -> {
            if(friends != null && friends.size() > 0) {
                updateData(friends);
            }else {
                requestData();
            }
        });
    }

    @Override
    protected void initListener() {
        mCommTitle.getLeftIv().setOnClickListener(this);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);

        mAdapter.setItemClickListener(this);
        mSideBar.setOnTouchingLetterChangedListener(mSideBarListener);
        mSearchEditText.getEditText().addTextChangedListener(mTextWatcher);
    }

    private void requestData() {
        if (!Utils.isNetworkConnected(DqApp.sContext)) {
            DqToast.showShort(DqApp.getStringById(R.string.comm_network_error));
            return;
        }

        if (mPresenter == null) {
            return;
        }

        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(IConstant.Contact.WHETHER_HELPER, "1");
        hashMap.put(IConstant.Contact.SHOW_SELF, "1");
        hashMap.put(IConstant.Contact.LAST_TIME, "0");
        mPresenter.getFriend(DqUrl.url_friend_list, hashMap);
    }

    /**
     * 更新数据
     */
    private void updateData(List<Friend> tmpList) {
        //添加拼音
        List<Friend> list = TransformFriendInfoHelper.getInstance().setPinYin(tmpList);
        if(null != list) {
            mFriendList.clear();
            mFriendList.addAll(list);
        }

        //按拼音排序
        Collections.sort(mFriendList, (o1, o2) -> {
            if (o1.pinYin.equals("@") || o2.pinYin.equals("#")) {
                return -1;
            } else if (o1.pinYin.equals("#") || o2.pinYin.equals("@")) {
                return 1;
            } else {
                return o1.pinYin.compareTo(o2.pinYin);
            }
        });

        mAdapter.update(mFriendList);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mCurPage = 1;
        requestData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mCurPage++;
        requestData();
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();

        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        if(entity == null) {
            return;
        }

        if (DqUrl.url_friend_list.equals(url)) {
            updateData((List<Friend>) entity.data);
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
    }

    /**
     * sidebar监听事件
     */
    private SideBar.OnTouchingLetterChangedListener mSideBarListener = letter -> {
        mSideBar.setTextView(mCenterTv);
        //该字母首次出现的位置
        int position = mAdapter.getPositionForSection(letter.charAt(0));
        if (position != -1) {
            mRecyclerView.scrollToPosition(position);
            LinearLayoutManager mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            mLayoutManager.scrollToPositionWithOffset(position, 0);
        }
    };

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String str = mSearchEditText.getEditText().getText().toString();
            mAdapter.update(filter(str));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 过滤数据
     * @param str
     * @return
     */
    protected List<Friend> filter(String str) {
        List<Friend> lists = new ArrayList<>();
        if(mFriendList == null || mFriendList.size() == 0) {
            return null;
        }
        int size = mFriendList.size();
        for (int i = 0; i < size; i++) {
            Friend mFriend = mFriendList.get(i);
            if (mFriend.nickname.contains(str) || mFriend.phone.contains(str) || mFriend.remarks.contains(str)) {
                lists.add(mFriend);
            }
        }
        return lists;
    }

    @Override
    public void onItemClick(@NonNull Friend friend) {
        if (friend == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(KeyValue.Ait.ENTITY, friend);
        setResult(RESULT_OK, intent);
        finish();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mCommTitle.getLeftIvId()) {
            finish();
        }
    }
}
