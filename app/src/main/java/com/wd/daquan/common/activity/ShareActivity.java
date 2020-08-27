package com.wd.daquan.common.activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.dq.im.model.HomeImBaseMode;
import com.dq.im.type.ImType;
import com.dq.im.viewmodel.HomeMessageViewModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.wd.daquan.R;
import com.wd.daquan.common.adapter.ShareAdapter;
import com.wd.daquan.common.bean.ShareItemBean;
import com.wd.daquan.common.listener.IShareAdapterClickListener;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.third.helper.TeamHelper;
import com.wd.daquan.third.helper.UserInfoHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 方志
 * @Time: 2018/9/21 13:52
 * @Description: 分享最近会话页面
 */
public class ShareActivity extends ShareBaseActivity {

    private TextView mCreateChatTv;
    private List<ShareItemBean> mChatList = new ArrayList<>();
    private ShareAdapter mAdapter;
    private IShareAdapterClickListener mShareListener = this::shareMessage;
    private HomeMessageViewModel homeMessageViewModel;
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_share);
    }

    @Override
    protected void initView() {
        super.initView();
        mCreateChatTv = findViewById(R.id.share_create_chat_tv);
    }

    @Override
    protected void initData() {
        super.initData();
        initTitle();
        initAdapter();
        homeMessageViewModel = new ViewModelProvider(this).get(HomeMessageViewModel.class);
        requestMessages();
    }

    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ShareAdapter();
        mRecyclerView.setAdapter(mAdapter);

    }
    private void requestMessages(){
        homeMessageViewModel.getAllMessage().observe(this, new Observer<List<HomeImBaseMode>>() {
            @Override
            public void onChanged(@Nullable List<HomeImBaseMode> homeImBaseModes) {
                if (null == mChatList){
                    mChatList = new ArrayList<>();
                }
                mChatList.clear();
                for (HomeImBaseMode mode : homeImBaseModes){
                    ShareItemBean bean = new ShareItemBean();
//                    bean.sessionId = mode.getMsgIdServer();
                    bean.sessionType = ImType.typeOfValue(mode.getType());
                    initUserData(mode,bean);
                    mChatList.add(bean);
                }
                updateData();
            }
        });
    }
    private void updateData() {
        if (null == mChatList) {
            return;
        }
        mNoDataTv.setVisibility(mChatList.size() > 0 ? View.GONE : View.VISIBLE);
        mAdapter.update(mChatList);
    }

    private void initTitle() {
        mTitleLayout.getRightTv().setVisibility(View.GONE);
        setTitleName(getString(R.string.choose_a_chat));
        mCreateChatTv.setText(getString(R.string.create_a_new_chat));

        mRefreshLayout.setEnableLoadMore(false);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mCreateChatTv.setOnClickListener(this);
        mAdapter.setListener(mShareListener);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == mCreateChatTv.getId()) {
            createNewChat();
        }
    }

    private void createNewChat() {
        NavUtils.gotoShareP2pOrNewTeamActivity(this, mShareBean);

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
    }
    /**
     * 更新用户数据
     * @param homeImBaseMode
     */
    private void initUserData(HomeImBaseMode homeImBaseMode, ShareItemBean shareItemBean){
        String type = homeImBaseMode.getType();
        if (ImType.P2P.getValue().equals(type)){
            String userId = ModuleMgr.getCenterMgr().getUID();
            String friendId = "";
            if (userId.equals(homeImBaseMode.getFromUserId())){
                friendId = homeImBaseMode.getToUserId();
            }else {
                friendId = homeImBaseMode.getFromUserId();
            }
            String headPic = UserInfoHelper.getHeadPic(friendId);
            shareItemBean.sessionPortrait = headPic;
            shareItemBean.sessionName = UserInfoHelper.getUserNickName(friendId);
            shareItemBean.sessionId = friendId;
        }else {
            String groupId = homeImBaseMode.getGroupId();
            String headPic = TeamHelper.getTeamHeadPic(groupId);
            shareItemBean.sessionPortrait = headPic;
            shareItemBean.sessionName = TeamHelper.getTeamName(groupId);
            shareItemBean.sessionId = groupId;
        }
    }
}
