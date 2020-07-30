package com.wd.daquan.chat.friend;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.da.library.constant.IConstant;
import com.da.library.listener.ISelectListener;
import com.da.library.widget.CommSearchEditText;
import com.da.library.widget.SideBar;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.contacts.helper.TransformFriendInfoHelper;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 选择好友基类
 */
public abstract class BaseSelectActivity extends DqBaseActivity<SelectFriendPresenter, DataBean>
        implements View.OnClickListener, ISelectListener<Friend> {
    // 请求页码
    protected int mCurPage = 1;
    // 选中成员列表
    protected ArrayList<Friend> mSelectList = new ArrayList<>();
    //所有数据
    protected List<Friend> mFriendList = new ArrayList<>();
    //所有不可选择数据
    protected List<Friend> mNoSelectList = new ArrayList<>();

    protected SideBar mSideBar = null;
    protected TextView mEmptyView = null;
    protected TextView mCenterTv = null;
    protected RecyclerView mRecyclerView = null;
    protected CommSearchEditText mSearchEditText = null;

    // 选择一个组
    protected RelativeLayout mSelectTeamRlyt = null;
    protected SelectFriendAdapter mFriendAdapter;
    protected TextView mRightTv;
    protected TextView mRightNumTv;
    private View mRightLl;
    private TextView mTitleTv;

    @Override
    protected SelectFriendPresenter createPresenter() {
        return new SelectFriendPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.select_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        mTitleTv = findViewById(R.id.toolbar_title_tv);
        mRightLl = findViewById(R.id.toolbar_right_ll);
        mRightTv = findViewById(R.id.toolbar_right_tv);
        mRightNumTv = findViewById(R.id.toolbar_right_num_tv);


        this.mEmptyView = this.findViewById(R.id.select_empty);
        this.mSearchEditText = this.findViewById(R.id.select_search_et);
        this.mRecyclerView = this.findViewById(R.id.select_recyclerview);
        this.mCenterTv = this.findViewById(R.id.select_center_tv);
        this.mSideBar = this.findViewById(R.id.select_sidebar);
        this.mSelectTeamRlyt = this.findViewById(R.id.select_team);


        setTitle(DqApp.getStringById(R.string.title_select_friend_shield));
        initTitleRight(DqApp.getColorById(R.color.text_blue_pressed),0);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void setTitle(String title) {
        mTitleTv.setText(title);
    }

    @SuppressLint("SetTextI18n")
    private void initTitleRight(int textColor, int num) {
        mRightTv.setTextColor(textColor);
        mRightNumTv.setText("(" + num + ")");
        mRightNumTv.setVisibility(num == 0 ? View.GONE : View.VISIBLE);
        mRightNumTv.setTextColor(textColor);
    }

    @Override
    protected void initData() {
        mSideBar.setTextView(mCenterTv);

        mFriendAdapter = new SelectFriendAdapter();
        mFriendAdapter.setMode(IConstant.Select.MORE);
        mRecyclerView.setAdapter(mFriendAdapter);
    }

    @Override
    protected void initListener() {
        toolbarBack();
        mRightLl.setOnClickListener(view -> onTitleRightClick());
        mFriendAdapter.setSelectListener(this);
        mSideBar.setOnTouchingLetterChangedListener(mSideBarListener);
        mSearchEditText.getEditText().addTextChangedListener(mTextWatcher);

    }

    protected abstract void onTitleRightClick();

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);

    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
    }


//    @Override
//    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//        mCurPage = 1;
//        requestData();
//    }
//
//    @Override
//    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//        mCurPage++;
//        requestData();
//    }

    /**
     * 请求数据
     */
    public abstract void requestData();

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


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
        for (Friend friend : mFriendList) {
            if (friend.getName().contains(str)) {
                lists.add(friend);
            }
        }
        return lists;
    }


    @Override
    public void onSelectList(@NonNull List<Friend> list) {
        mSelectList.clear();
        mSelectList.addAll(list);
        mRightLl.setEnabled(mSelectList.size() > 0);
        if(mSelectList.size() > 0) {
            initTitleRight(DqApp.getColorById(R.color.text_blue), mSelectList.size());
        }else {
            initTitleRight(DqApp.getColorById(R.color.text_blue_pressed), 0);
        }
    }

    /**
     * sidebar监听事件
     */
    private SideBar.OnTouchingLetterChangedListener mSideBarListener = letter -> {
        mSideBar.setTextView(mCenterTv);
        //该字母首次出现的位置
        int position = mFriendAdapter.getPositionForSection(letter.charAt(0));
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
            mFriendAdapter.update(filter(str));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 更新数据
     */
    protected void updateData(List<Friend> friendList) {

        //添加拼音
        List<Friend> list = TransformFriendInfoHelper.getInstance().setPinYin(friendList);
        if(null != list) {
            mFriendList.clear();
            mFriendList.addAll(list);
        }

        if(mFriendList.size() <= 0) {
            mEmptyView.setVisibility(View.VISIBLE);
        }else {
            mEmptyView.setVisibility(View.GONE);
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

        mFriendAdapter.update(mFriendList);
    }

}
