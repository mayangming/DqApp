package com.wd.daquan.contacts.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.da.library.constant.IConstant;
import com.da.library.listener.ITextChangedListener;
import com.da.library.tools.ActivitysManager;
import com.da.library.widget.CommSearchEditText;
import com.netease.nim.uikit.business.session.activity.P2PMessageActivity;
import com.wd.daquan.MainActivity;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.SpannableStringUtils;
import com.wd.daquan.contacts.adapter.SearchFriendAdapter;
import com.wd.daquan.contacts.helper.TransformFriendInfoHelper;
import com.wd.daquan.contacts.presenter.ContactPresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.utils.GsonUtils;
import com.wd.daquan.third.session.SessionHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 方志
 * @Time: 2018/9/26 16:36
 * @Description: 添加朋友页面
 */
public class SearchFriendActivity extends DqBaseActivity<ContactPresenter, DataBean> {

    private CommSearchEditText mSearchLayout;
    private RecyclerView mRecyclerView;
    private View mSearchHintTv;
    private TextView mNoDataTv;
    private SearchFriendAdapter mAdapter;
    private List<Friend> mFriendList = new ArrayList<>();

    @Override
    protected ContactPresenter createPresenter() {
        return new ContactPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_search_friend);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        mSearchLayout = findViewById(R.id.search_layout);
        mRecyclerView = findViewById(R.id.search_recycler_view);
        mSearchHintTv = findViewById(R.id.search_hint_tv);
        mNoDataTv = findViewById(R.id.no_data_tv);
    }

    @Override
    protected void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchFriendAdapter();
        mRecyclerView.setAdapter(mAdapter);

        List<Friend> friendList = FriendDbHelper.getInstance().getAllFriendAndSelf();
        if(friendList.size() > 0) {
            useCacheData(friendList);
        }else {
            getContacts();
        }
    }

    private void useCacheData(List<Friend> friendList) {
        //添加拼音
        List<Friend> list = TransformFriendInfoHelper.getInstance().setPinYin(friendList);
        if(null != list) {
            mFriendList.clear();
            mFriendList.addAll(list);
        }

        // 根据a-z进行排序源数据
        Collections.sort(mFriendList, (o1, o2) -> {
            if (o1.pinYin.equals("@") || o2.pinYin.equals("#")) {
                return -1;
            } else if (o1.pinYin.equals("#") || o2.pinYin.equals("@")) {
                return 1;
            } else {
                return o1.pinYin.compareTo(o2.pinYin);
            }
        });
    }


    private void getContacts() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(IConstant.Contact.PAGE, "1");
        hashMap.put(IConstant.Contact.LENGTH, "3000");
        hashMap.put(IConstant.Contact.WHETHER_HELPER, "1");
        hashMap.put(IConstant.Contact.SHOW_SELF, "0");
        hashMap.put(IConstant.Contact.LAST_TIME, "0");
        mPresenter.getContacts(DqUrl.url_friend_list, hashMap, false);
    }

    @Override
    protected void initListener() {
        mSearchLayout.setChangedListener(mTextChangedListener);
        mAdapter.setListener(friend -> {
            SessionHelper.startP2PSession(getActivity(), friend.uid);
            ActivitysManager.getInstance().finishAllFilterMore(MainActivity.class, P2PMessageActivity.class);
        });
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if(entity == null) return;
        if(DqUrl.url_friend_list.equals(url)) {
            String data = GsonUtils.toJson(entity.data);
            List<Friend> friendList = GsonUtils.fromJsonList(data, Friend.class);
            if(null != friendList && friendList.size() > 0) {
                useCacheData(friendList);
            }
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        if(entity == null) return;
        DqToast.showShort(entity.content);
    }

    private ITextChangedListener mTextChangedListener = new ITextChangedListener() {
        @Override
        public void beforeChange() {

        }

        @Override
        public void textChanged(String content) {

            List<Friend> filter = filter(content);
            SpannableString spannableString = SpannableStringUtils.addTextColor(getString(R.string.search_contact_no_result, content),
                    5, content.length() + 5, getResources().getColor(R.color.text_blue));
            mNoDataTv.setText(spannableString);
            mNoDataTv.setVisibility(filter.size() > 0 ? View.GONE : View.VISIBLE);

            if (TextUtils.isEmpty(content)) {
                mSearchHintTv.setVisibility(View.VISIBLE);
                mNoDataTv.setVisibility(View.GONE);
            } else {
                mSearchHintTv.setVisibility(View.GONE);
            }

            mAdapter.update(filter);
        }
    };

    private List<Friend> filter(String str){
        List<Friend> lists = new ArrayList<>();
        if (TextUtils.isEmpty(str)) {
            return lists;
        }
        if(mFriendList != null && mFriendList.size() > 0) {
            for (Friend friend : mFriendList) {
                if (friend.nickname.contains(str) || friend.getFriend_remarks().contains(str)) {
                    lists.add(friend);
                }
            }
        }
        return lists;
    }
}
