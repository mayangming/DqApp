package com.wd.daquan.chat.ait;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @ 选择人员列表
 */
public class AitActivity extends DqBaseActivity<AitPresenter, DataBean> implements View.OnClickListener, OnRefreshListener, OnLoadMoreListener, IOnItemClickSelectListener<Friend> {

    // 请求页码
    protected int mCurPage = 1;
    protected String mTeamId = "";

    private List<Friend> mFriendList = new ArrayList<>();

    private View mEmptyLine = null;
    private ImageView mAllIcon;
    private TextView mCenterTv = null;
    private SideBar mSideBar = null;
    private RelativeLayout mAllRlyt = null;
    private RecyclerView mRecyclerView = null;

    private RefreshLayout mRefreshLayout = null;

    private CommTitle mCommTitle = null;
    private CommSearchEditText mSearchEditText = null;

    private AitAdapter mAdapter = null;

    @Override
    protected AitPresenter createPresenter() {
        return new AitPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.ait_activity);
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        if (intent != null) {
            mTeamId = intent.getStringExtra(KeyValue.GROUP_ID);
        }
    }

    @Override
    protected void initView() {
        mCommTitle = this.findViewById(R.id.ait_commtitle);
        mSearchEditText = this.findViewById(R.id.ait_search_et);
        mEmptyLine = this.findViewById(R.id.ait_empty_line);
        mAllRlyt = this.findViewById(R.id.ait_all_rlyt);
        mAllIcon = this.findViewById(R.id.ait_all_icon);
        mRefreshLayout = this.findViewById(R.id.ait_refreshlayout);
        mRecyclerView = this.findViewById(R.id.ait_recyclerview);
        mCenterTv = this.findViewById(R.id.ait_center_tv);
        mSideBar = this.findViewById(R.id.ait_sidebar);

        mCommTitle.setTitle("选择成员");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {
        mSideBar.setTextView(mCenterTv);

        mAdapter = new AitAdapter();
        mAdapter.setMode(IConstant.Select.SINGLE);
        mRecyclerView.setAdapter(mAdapter);

        requestData();
    }

    @Override
    protected void initListener() {
        mAllRlyt.setOnClickListener(this);
        mCommTitle.getLeftIv().setOnClickListener(this);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);

        mAdapter.setItemClickListener(this);
        mSideBar.setOnTouchingLetterChangedListener(mSideBarListener);
        mSearchEditText.getEditText().addTextChangedListener(mTextWatcher);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mCommTitle.getLeftIvId()) {
            finish();
        } else if (id == mAllRlyt.getId()) {
            Friend friend = new Friend();
            friend.nickname = "所有人";
            Intent intent = new Intent();
            intent.putExtra(KeyValue.Ait.ENTITY, friend);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void requestData() {
        if (!Utils.isNetworkConnected(DqApp.sContext)) {
            DqToast.showShort(DqApp.getStringById(R.string.comm_network_error));
            return;
        }

        if (mPresenter == null) {
            return;
        }



        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put("group_id", "" + mTeamId);
        mPresenter.getTeamMember(DqUrl.url_select_group_user, hashMap);
    }

    /**
     * 更新数据
     */
    private void updateData(List<Friend> tmpList) {
        if(tmpList == null) return;
        // 检查是否显示所有人
        for (Friend friend : tmpList) {
            if (ModuleMgr.getCenterMgr().getUID().equals(friend.uid)) {
                if (KeyValue.Role.ADMIN.equals(friend.role) || KeyValue.Role.MASTER.equals(friend.role)) {
                    mAllRlyt.setVisibility(View.VISIBLE);
                    mEmptyLine.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }

        //添加拼音
        List<Friend> list = TransformFriendInfoHelper.getInstance().setPinYin(tmpList);
        if(null != list) {
            mFriendList.clear();
            mFriendList.addAll(list);
        }

        for (Friend friend : list) {
            if (ModuleMgr.getCenterMgr().getUID().equals(friend.uid)) {
                if (KeyValue.Role.ADMIN.equals(friend.role) || KeyValue.Role.MASTER.equals(friend.role)) {
                    mAllRlyt.setVisibility(View.VISIBLE);
                    mEmptyLine.setVisibility(View.VISIBLE);
                    break;
                }
            }
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
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();

        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        if (DqUrl.url_select_group_user.equals(url)) {
            updateData((List<Friend>) entity.data);
        }

    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
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
}
